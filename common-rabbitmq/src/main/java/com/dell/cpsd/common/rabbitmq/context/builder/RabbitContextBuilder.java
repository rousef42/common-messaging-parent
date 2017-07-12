/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.support.RetryTemplate;

import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.ApplicationConfiguration;
import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import com.dell.cpsd.common.rabbitmq.context.RabbitContextAware;
import com.dell.cpsd.common.rabbitmq.context.RequestReplyKey;
import com.dell.cpsd.common.rabbitmq.message.DefaultMessageConverterFactory;
import com.dell.cpsd.common.rabbitmq.retrypolicy.DefaultRetryPolicyFactory;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * <p>
 * This RabbitContextBuilder is an opinionated builder that builds up a set of queues, exchanges, binding, and message descriptions
 * to provide some conformity and convention to the creation of rabbit contexts.
 * <p>
 * This is not a complete builder and will ideally get added as the norms evolve
 *
 * @since SINCE-TBD
 */
public class RabbitContextBuilder
{
    final Map<String, Exchange>           exchanges             = new HashMap<>();
    final Map<String, Queue>              queues                = new HashMap<>();
    final Map<String, Binding>            bindings              = new HashMap<>();
    final Map<String, MessageDescription> descriptions          = new HashMap<>();
    final List<MessageListenerContainer>  containers            = new ArrayList<>();
    final Map<String, ContainerQueueData> containerQueueDataMap = new HashMap<>();
    final List<RabbitContextAware>        contextAwares         = new ArrayList<>();
    final Map<RequestReplyKey, String>    replyToMap            = new HashMap<>();

    private MessageDescriptionFactory messageDescriptionFactory = null;
    private ContainerFactory          containerFactory          = null;

    private ConnectionFactory        rabbitConnectionFactory;
    private ApplicationConfiguration applicationConfiguration;
    private String                   consumerPostfix;

    /**
     * Constructor
     *
     * @param rabbitConnectionFactory
     * @param configuration
     */
    public RabbitContextBuilder(ConnectionFactory rabbitConnectionFactory, ApplicationConfiguration configuration)
    {
        this(rabbitConnectionFactory, configuration, Collections.emptyList());
    }

    /**
     * Constructor
     *
     * @param rabbitConnectionFactory
     * @param configuration
     */
    public RabbitContextBuilder(ConnectionFactory rabbitConnectionFactory, ApplicationConfiguration configuration, File file)
    {
        this(rabbitConnectionFactory, configuration, new MessageMetaDataReader().read(file));
    }

    /**
     * Constructor
     *
     * @param rabbitConnectionFactory
     * @param configuration
     * @param metaDatas
     */
    public RabbitContextBuilder(ConnectionFactory rabbitConnectionFactory, ApplicationConfiguration configuration,
            Collection<MessageMetaData> metaDatas)
    {
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.applicationConfiguration = configuration;
        this.consumerPostfix = configuration.getApplicationName() + "." + configuration.getHostName();

        this.messageDescriptionFactory = new MessageDescriptionFactory(configuration, metaDatas);
        this.containerFactory = new ContainerFactory();
    }

    /**
     * Register a message class that is produced
     *
     * @param produceClass
     * @return
     */
    public <P> RabbitContextBuilder produces(Class<P> produceClass)
    {
        // Create durable exchanage by default
        return produces(produceClass, true);
    }

    /**
     * Register a message class that is produced
     *
     * @param produceClass
     * @return
     */
    public <P> RabbitContextBuilder produces(Class<P> produceClass, boolean durable)
    {
        MessageDescription<P> produceDescription = messageDescriptionFactory.createDescription(produceClass);
        descriptions.put(produceDescription.getType(), produceDescription);
        MessageExchangeBuilder builder = new MessageExchangeBuilder(this, produceDescription.getExchange(),
                produceDescription.getExchangeType());

        if (durable)
        {
            builder.durable();
        }
        builder.exchange();
        return this;
    }

    /**
     * Register a message class that is consumed
     *
     * @param messageClass
     * @param queueName
     * @param durable
     * @param listener
     * @return
     */
    public <C> RabbitContextBuilder consumes(String queueName, boolean durable, Object listener, Class<C> messageClass)
    {
        return consumes(queueName, durable, null, listener, messageClass);
    }

    /**
     * Register a message class that is consumed
     *
     * @param messageClass
     * @param queueName
     * @param durable
     * @param containerAlias
     * @param listener
     * @return
     */
    public <C> RabbitContextBuilder consumes(String queueName, boolean durable, String containerAlias, Object listener,
            Class<C> messageClass)
    {
        MessageDescription<C> description = messageDescriptionFactory.createDescription(messageClass);
        descriptions.put(description.getType(), description);

        //If the container alias happens to be null, then all queues will get the same 'null' container
        addQueueData(containerAlias, queueName, listener);

        MessageBindingBuilder bindingBuilder = new MessageBindingBuilder(this, resolveRoutingKey(description, consumerPostfix));
        bindingBuilder.fromExchange(description.getExchange(), description.getExchangeType()).toQueue(queueName, durable);
        bindingBuilder.bind();

        return this;
    }

    /**
     * Register a produce message class and consumme class pair. This is useful because a listener queue will be bound with the
     * producer routing key for replies
     *
     * @param requestClass
     * @param replyClasses
     * @param queueName
     * @param durable
     * @param containerAlias
     * @param listener
     * @return
     */
    public <P> RabbitContextBuilder requestsAndReplies(Class<P> requestClass, String queueName, boolean durable, String containerAlias,
            Object listener, Class<?>... replyClasses)
    {
        produces(requestClass);

        MessageDescription<P> requestDescription = messageDescriptionFactory.createDescription(requestClass);
        descriptions.put(requestDescription.getType(), requestDescription);

        for (Class<?> replyClass : replyClasses)
        {
            MessageDescription replyDescription = messageDescriptionFactory.createDescription(replyClass);
            descriptions.put(replyDescription.getType(), replyDescription);

            addQueueData(containerAlias, queueName, listener);

            MessageBindingBuilder replyBindingBuilder = new MessageBindingBuilder(this,
                    resolveRoutingKey(replyDescription.getStereotype(), requestDescription.getRoutingKey(), consumerPostfix));

            Binding replyBinding = replyBindingBuilder.fromExchange(replyDescription.getExchange(), replyDescription.getExchangeType())
                    .toQueue(queueName, durable).bind();

            replyToMap.put(new RequestReplyKey(requestClass, replyClass), replyBinding.getRoutingKey());
        }

        return this;
    }

    /**
     * Register a produce message class and consumme class pair. This is useful because a listener queue will be bound with the
     * producer routing key for replies
     *
     * @param requestClass
     * @param replyClasses
     * @param queueName
     * @param durable
     * @param listener
     * @return
     */
    public <P> RabbitContextBuilder requestsAndReplies(Class<P> requestClass, String queueName, boolean durable, Object listener,
            Class<?>... replyClasses)
    {
        return requestsAndReplies(requestClass, queueName, durable, null, listener, replyClasses);
    }

    /**
     * Add a context aware class that will get populed with the built RabbitContext
     *
     * @param contextAware
     * @return
     */
    public RabbitContextBuilder addContextAware(RabbitContextAware contextAware)
    {
        this.contextAwares.add(contextAware);
        return this;
    }

    public RabbitContext build()
    {
        RabbitAdmin admin = new RabbitAdmin(rabbitConnectionFactory);

        ClassMapper mapper = createClassMapper();
        AbstractJsonMessageConverter converter = createMessageConverter(mapper);

        RetryTemplate retryTemplate = createRetryTemplate();
        RabbitTemplate rabbitTemplate = createRabbitTemplate(converter, retryTemplate);

        // Create containers for queues based on the containerAlias value on the message.
        // Default behaviour will be to add all queues to a single container
        containerQueueDataMap.forEach((containerAlias, containerData) ->
        {
            SimpleMessageListenerContainer container = containerFactory
                    .createDefaultContainer(consumerPostfix + "-" + containerAlias, rabbitConnectionFactory, converter,
                            containerData.getListener());
            containerData.getQueueNames().forEach(q -> container.addQueues(queues.get(q)));
            containers.add(container);
        });

        RabbitContext context = new RabbitContext(consumerPostfix, admin, rabbitTemplate, converter, exchanges.values(), queues.values(),
                bindings.values(), descriptions.values(), containers, replyToMap);

        // Set the context in anything that has been added as a RabbitContextAware
        contextAwares.forEach(contextAware -> contextAware.setRabbitContext(context));

        return context;
    }

    /**
     * Add a binding
     *
     * @param binding
     * @return
     */
    public RabbitContextBuilder add(Binding binding)
    {
        bindings.put(Arrays.toString(new String[] {binding.getExchange(), binding.getExchange(), binding.getRoutingKey()}), binding);
        return this;
    }

    /**
     * Add an exchange
     *
     * @param exchange
     * @return
     */
    public RabbitContextBuilder add(Exchange exchange)
    {
        exchanges.put(exchange.getName(), exchange);
        return this;
    }

    /**
     * Add a queue
     *
     * @param queue
     * @return
     */
    public RabbitContextBuilder add(Queue queue)
    {
        queues.put(queue.getName(), queue);
        return this;
    }

    /**
     * Add a container
     *
     * @param container
     * @return
     */
    public RabbitContextBuilder add(MessageListenerContainer container)
    {
        containers.add(container);
        return this;
    }

    private void addQueueData(String containerAlias, String queueName, Object listener)
    {
        //If the container alias happens to be null, then all queues will get the same 'null' container
        ContainerQueueData queueData = containerQueueDataMap.get(containerAlias);
        if (queueData == null)
        {
            queueData = new ContainerQueueData(containerAlias, queueName, listener);
            containerQueueDataMap.put(containerAlias, queueData);
        }
        else
        {
            // This logic should probably get expanded/hardened a bit more
            if (queueData.getListener() != listener)
            {
                throw new IllegalStateException("Handler references need to be the same for any given container alias");
            }
            queueData.addQueueName(queueName);
        }
    }

    private RabbitTemplate createRabbitTemplate(MessageConverter messageConverter, RetryTemplate retryTemplate)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(messageConverter);
        template.setRetryTemplate(retryTemplate);
        return template;
    }

    private RetryTemplate createRetryTemplate()
    {
        return DefaultRetryPolicyFactory.makeRabbitTemplateRetry();
    }

    private AbstractJsonMessageConverter createMessageConverter(ClassMapper mapper)
    {
        return (AbstractJsonMessageConverter) DefaultMessageConverterFactory.makeMessageConverter(mapper);
    }

    private ClassMapper createClassMapper()
    {
        final Map<String, Class<?>> classMappings = new HashMap<>();
        descriptions.forEach((k, v) -> classMappings.put(v.getType(), v.getMessageClass()));

        final DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(classMappings);
        return classMapper;
    }

    private <M> String resolveRoutingKey(MessageDescription<M> messageDescription, String consumerPostfix)
    {
        String routingKey = messageDescription.getRoutingKey();
        if (routingKey == null)
        {
            routingKey = messageDescription.getType();
        }

        MessageStereotype stereotype = messageDescription.getStereotype();
        return resolveRoutingKey(stereotype, routingKey, consumerPostfix);
    }

    private String resolveRoutingKey(MessageStereotype stereotype, String routingKey, String consumerPostfix)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(routingKey);

        if (MessageStereotype.REPLY == stereotype || MessageStereotype.ERROR == stereotype)
        {
            builder.append("." + consumerPostfix);
        }
        return builder.toString();
    }

    private static class ContainerQueueData
    {
        private String containerAlias;
        private Set<String> queueNames = new HashSet<>();
        private Object listener;

        ContainerQueueData(String containerAlias, String queueName, Object listener)
        {
            this.containerAlias = containerAlias;
            this.queueNames.add(queueName);
            this.listener = listener;
        }

        public boolean addQueueName(String s)
        {
            return queueNames.add(s);
        }

        public String getContainerAlias()
        {
            return this.containerAlias;
        }

        public Collection<String> getQueueNames()
        {
            return queueNames;
        }

        public Object getListener()
        {
            return listener;
        }
    }

    public static class MessageQueueBuilder
    {
        private RabbitContextBuilder contextBuilder;
        private QueueBuilder         nativeBuilder;

        public MessageQueueBuilder(RabbitContextBuilder contextBuilder, String name, boolean durable)
        {
            this.contextBuilder = contextBuilder;
            if (durable)
            {
                nativeBuilder = QueueBuilder.durable(name);
            }
            else
            {
                nativeBuilder = QueueBuilder.nonDurable(name);
            }
        }

        public MessageQueueBuilder exclusive()
        {
            nativeBuilder.exclusive();
            return this;
        }

        public MessageQueueBuilder autoDelete()
        {
            nativeBuilder.autoDelete();
            return this;
        }

        public MessageQueueBuilder withArgument(String key, Object value)
        {
            nativeBuilder.withArgument(key, value);
            return this;
        }

        public MessageQueueBuilder withArguments(Map<String, Object> arguments)
        {
            nativeBuilder.withArguments(arguments);
            return this;
        }

        public RabbitContextBuilder context()
        {
            queue();
            return contextBuilder;
        }

        public Queue queue()
        {
            Queue queue = nativeBuilder.build();
            contextBuilder.add(queue);
            return queue;
        }
    }

    public static final class MessageExchangeBuilder
    {
        private RabbitContextBuilder contextBuilder;
        private ExchangeBuilder      nativeBuilder;

        private MessageExchangeBuilder(RabbitContextBuilder builder, String name, MessageExchangeType type)
        {
            this.contextBuilder = builder;
            this.nativeBuilder = createBuilder(name, type);
        }

        private ExchangeBuilder createBuilder(String name, MessageExchangeType type)
        {
            switch (type)
            {
                case TOPIC:
                    return ExchangeBuilder.topicExchange(name);
                case DIRECT:
                    return ExchangeBuilder.directExchange(name);
                case HEADERS:
                    return ExchangeBuilder.headersExchange(name);
                case FANOUT:
                    return ExchangeBuilder.fanoutExchange(name);
            }
            return null;
        }

        public MessageExchangeBuilder autoDelete()
        {
            nativeBuilder.autoDelete();
            return this;
        }

        public MessageExchangeBuilder durable()
        {
            nativeBuilder.durable();
            return this;
        }

        public MessageExchangeBuilder withArgument(String key, Object value)
        {
            nativeBuilder.withArgument(key, value);
            return this;
        }

        public MessageExchangeBuilder withArguments(Map<String, Object> arguments)
        {
            nativeBuilder.withArguments(arguments);
            return this;
        }

        public MessageExchangeBuilder internal()
        {
            nativeBuilder.internal();
            return this;
        }

        public MessageExchangeBuilder delayed()
        {
            nativeBuilder.delayed();
            return this;
        }

        public RabbitContextBuilder context()
        {
            exchange();
            return contextBuilder;
        }

        public Exchange exchange()
        {
            Exchange exchange = nativeBuilder.build();
            contextBuilder.add(exchange);
            return exchange;
        }
    }

    public static class MessageBindingBuilder
    {
        private RabbitContextBuilder   contextBuilder;
        private MessageExchangeBuilder exchangeBuilder;
        private MessageQueueBuilder    queueBuilder;
        private String                 bindingBase;

        public MessageBindingBuilder(RabbitContextBuilder builder, String bindingBase)
        {
            this.contextBuilder = builder;
            this.bindingBase = bindingBase;
        }

        public MessageBindingBuilder fromTopicExchange(String exchangeName)
        {
            return fromExchange(exchangeName, MessageExchangeType.TOPIC);
        }

        public MessageBindingBuilder fromDirectExchange(String exchangeName)
        {
            return fromExchange(exchangeName, MessageExchangeType.DIRECT);
        }

        public MessageBindingBuilder fromHeadersExchange(String exchangeName)
        {
            return fromExchange(exchangeName, MessageExchangeType.HEADERS);
        }

        public MessageBindingBuilder fromFanoutExchange(String exchangeName)
        {
            return fromExchange(exchangeName, MessageExchangeType.FANOUT);
        }

        public MessageBindingBuilder fromExchange(String exchangeName, MessageExchangeType exchangeType)
        {
            // Durable by default
            return fromExchange(exchangeName, exchangeType, true);
        }

        public MessageBindingBuilder fromExchange(String exchangeName, MessageExchangeType exchangeType, boolean durable)
        {
            exchangeBuilder = new MessageExchangeBuilder(contextBuilder, exchangeName, exchangeType);
            if (durable)
            {
                exchangeBuilder.durable();
            }
            return this;
        }

        public MessageBindingBuilder toQueue(String queueName, boolean durable)
        {
            queueBuilder = new MessageQueueBuilder(contextBuilder, queueName, durable);
            return this;
        }

        public MessageBindingBuilder toDurableQueue(String queueName)
        {
            return toQueue(queueName, true);
        }

        public MessageBindingBuilder toNonDurableQueue(String queueName)
        {
            return toQueue(queueName, false);
        }

        public MessageExchangeBuilder exchange()
        {
            return exchangeBuilder;
        }

        public MessageQueueBuilder queue()
        {
            return queueBuilder;
        }

        public RabbitContextBuilder context()
        {
            bind();
            return contextBuilder;
        }

        public Binding bind()
        {
            return bind(queueBuilder.queue(), exchangeBuilder.exchange());
        }

        private Binding bind(Queue queue, Exchange exchange)
        {
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(bindingBase).noargs();
            contextBuilder.add(binding);
            return binding;
        }

    }
}

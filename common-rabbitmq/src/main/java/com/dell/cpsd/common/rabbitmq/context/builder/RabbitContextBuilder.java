/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchangeType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import com.dell.cpsd.common.rabbitmq.context.RabbitContextAware;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * This RabbitContextBuilder is an opinionated builder that builds up a set of queues, exchanges, binding, and message descriptions
 * to provide some conformity and convention to the creation of rabbit contexts.
 *
 * This is not a complete builder and will ideally get added as the norms evolve
 *
 * @since SINCE-TBD
 */
public class RabbitContextBuilder
{
    private static final ILogger LOGGER           = RabbitMQLoggingManager.getLogger(RabbitContextBuilder.class);
    /*
     * The retry template information for the client.
     */
    private static final int     MAX_ATTEMPTS     = 10;
    private static final int     INITIAL_INTERVAL = 100;
    private static final double  MULTIPLIER       = 2.0;
    private static final int     MAX_INTERVAL     = 50000;

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";

    final Map<String, Exchange>              exchanges     = new HashMap<>();
    final Map<String, Queue>                 queues        = new HashMap<>();
    final Map<String, Binding>               bindings      = new HashMap<>();
    final Map<String, MessageDescription<?>> descriptions  = new HashMap<>();
    final Map<String, ContainerQueueData>    containerMap  = new HashMap<>();
    final List<RabbitContextAware>           contextAwares = new ArrayList<>();

    private MessageDescriptionFactory messageDescriptionFactory = new MessageDescriptionFactory();
    private ContainerFactory          containerFactory          = new ContainerFactory();

    private ConnectionFactory rabbitConnectionFactory;
    private String            consumerPostfix;

    /**
     * Constructor
     *
     * @param rabbitConnectionFactory
     * @param applicationName
     */
    public RabbitContextBuilder(ConnectionFactory rabbitConnectionFactory, String applicationName)
    {
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.consumerPostfix = hostName() + "-" + applicationName;
    }

    /**
     * Register a message class that is produced
     *
     * @param produceClass
     * @return
     */
    public <P> RabbitContextBuilder produces(Class<P> produceClass)
    {
        MessageDescription<P> produceDescription = messageDescriptionFactory.createDescription(produceClass);
        descriptions.put(produceDescription.getType(), produceDescription);
        MessageExchangeBuilder builder = new MessageExchangeBuilder(this, produceDescription.getExchange(),
                produceDescription.getExchangeType());
        return add(builder.exchange());
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
    public <C> RabbitContextBuilder consumes(Class<C> messageClass, String queueName, boolean durable, Object listener)
    {
        MessageDescription<C> description = messageDescriptionFactory.createDescription(messageClass);
        descriptions.put(description.getType(), description);

        //If the container alias happens to be null, then all queues will get the same 'null' container
        addQueueData(description.getContainerAlias(), queueName, listener);

        MessageBindingBuilder builder = new MessageBindingBuilder(this, binding(description, consumerPostfix));
        return add(builder.fromExchange(description.getExchange(), description.getExchangeType()).toQueue(queueName, durable).bind());
    }

    /**
     * Register a produce message class and consumme class pair. This is useful because a listener queue will be bound with the
     * producer routing key for replies
     *
     * @param produceClass
     * @param consumeClass
     * @param queueName
     * @param durable
     * @param listener
     * @return
     */
    public <P, C> RabbitContextBuilder producesAndConsumes(Class<P> produceClass, Class<C> consumeClass, String queueName, boolean durable,
            Object listener)
    {
        MessageDescription<P> produceDescription = messageDescriptionFactory.createDescription(produceClass);
        MessageDescription<C> consumeDescription = messageDescriptionFactory.createDescription(consumeClass);

        descriptions.put(produceDescription.getType(), produceDescription);
        descriptions.put(consumeDescription.getType(), consumeDescription);

        addQueueData(consumeDescription.getContainerAlias(), queueName, listener);

        MessageBindingBuilder builder = new MessageBindingBuilder(this,
                binding(MessageStereotype.REPLY, produceDescription.getProducerRoutingKey(), consumerPostfix));

        return produces(produceClass).add(builder.fromExchange(consumeDescription.getExchange(), consumeDescription.getExchangeType())
                .toQueue(queueName, durable).bind());
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
        MessageConverter converter = createMessageConverter(mapper);

        RetryTemplate retryTemplate = createRetryTemplate();
        RabbitTemplate rabbitTemplate = createRabbitTemplate(converter, retryTemplate);

        // Create containers for queues based on the containerAlias value on the message.
        // Default behaviour will be to add all queues to a single container
        List<SimpleMessageListenerContainer> containers = new ArrayList<>();
        containerMap.forEach((containerAlias, containerData) ->
        {
            SimpleMessageListenerContainer container = containerFactory
                    .createDefaultContainer(consumerPostfix + "-" + containerAlias, rabbitConnectionFactory, converter,
                            containerData.getListener());
            containerData.getQueueNames().forEach(q -> container.addQueues(queues.get(q)));
            containers.add(container);
        });

        RabbitContext context = new RabbitContext(consumerPostfix, admin, rabbitTemplate, exchanges.values(), queues.values(), bindings.values(),
                descriptions.values(), containers);

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

    private void addQueueData(String containerAlias, String queueName, Object listener)
    {
        //If the container alias happens to be null, then all queues will get the same 'null' container
        ContainerQueueData queueData = containerMap.get(containerAlias);
        if (queueData == null)
        {
            queueData = new ContainerQueueData(containerAlias, queueName, listener);
            containerMap.put(containerAlias, queueData);
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
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    private MessageConverter createMessageConverter(ClassMapper mapper)
    {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(mapper);
        messageConverter.setCreateMessageIds(true);

        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(ISO8601_DATE_FORMAT));

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        return messageConverter;
    }

    private ClassMapper createClassMapper()
    {
        final Map<String, Class<?>> classMappings = new HashMap<>();
        descriptions.forEach((k, v) -> classMappings.put(v.getType(), v.getMessageClass()));

        final DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(classMappings);
        return classMapper;
    }

    private <M> String binding(MessageDescription<M> messageDescription, String consumerPostfix)
    {
        String consumerBindingBase = messageDescription.getConsumerBindingBase();
        if (consumerBindingBase == null)
        {
            consumerBindingBase = messageDescription.getType();
        }

        MessageStereotype stereotype = messageDescription.getStereotype();
        return binding(stereotype, consumerBindingBase, consumerPostfix);
    }

    private String binding(MessageStereotype stereotype, String consumerBindingBase, String consumerPostfix)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(consumerBindingBase);

        if (MessageStereotype.REPLY == stereotype)
        {
            builder.append("." + consumerPostfix);
        }
        return builder.toString();
    }

    private String hostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException("Unable to identify hostname", e);
        }
    }

    private static class ContainerQueueData
    {
        private String containerAlias;
        private Set<String> queueNames = new HashSet<>();
        private Object listener;

        public ContainerQueueData(String containerAlias, String queueName, Object listener)
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

    public static class MessageExchangeBuilder
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
                    return ExchangeBuilder.headersExchange(name);
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
            exchangeBuilder = new MessageExchangeBuilder(contextBuilder, exchangeName, exchangeType);
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

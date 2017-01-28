/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.producer;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

import org.springframework.amqp.AmqpException;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessageProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesHelper;

import com.dell.cpsd.common.rabbitmq.processor.PropertiesPostProcessor;

/**
 * This is the service message producer.
 * 
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @since    1.1
 */
public abstract class AbstractAmqpMessageProducer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            RabbitMQLoggingManager.getLogger(AbstractAmqpMessageProducer.class);
    
    /*
     * The exchanges used by this producer to send messages.
     */
    private Map<String, Exchange> exchanges;

    /*
     * The <code>RabbitTemplate</code> used by this producer.
     */
    private RabbitTemplate rabbitTemplate;
    
    /*
     * The <code>Calendar</code> used for timestamps.
     */
    private Calendar calendar = null;
    
    /*
     * The host name of the service.
     */
    private String hostname = null;

    
    /**
     * AbstractAmqpMessageProducer constructor.
     * 
     * @param   rabbitTemplate  The RabbitMQ templage used by this producer.
     * @param   hostname        The host name of the service.
     * 
     * @throws  IllegalArgumentException  Thrown if the parameters are null.
     * 
     * @since   1.0
     */
    public AbstractAmqpMessageProducer(final RabbitTemplate rabbitTemplate, 
            final String hostname)
    {
        super();
        
        this.exchanges = new HashMap<String, Exchange>();
        
        this.calendar = Calendar.getInstance();
        
        this.setRabbitTemplate(rabbitTemplate);
        
        this.setHostname(hostname);
    }

    
    /**
     * This returns the <code>Exchange</code>s for this producer.
     * 
     * @return  The <code>Exchange</code>s for this producer.
     * 
     * @since    1.0
     */
    public Map<String, Exchange> getExchanges()
    {
        return this.exchanges;
    }
    
    
    /**
     * This adds an <code>Exchange</code> for this producer.
     * 
     * @param   name        The name of the exchange.
     * @param   exchange    The <code>Exchange</code> to add.
     * 
     * @throws  IllegalArgumentException  Thrown if the arguments are null.
     * 
     * @since    1.0
     */
    public void addExchange(final String name, final Exchange exchange)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("The exchange name is not set.");
        }
        
        if (exchange == null)
        {
            throw new IllegalArgumentException("The exchange is not set.");
        }
        
        this.exchanges.put(name, exchange);
    }
    
    
    /**
     * This removes an <code>Exchange</code> for this producer.
     * 
     * @param   name    The name of the exchange.
     * 
     * @since    1.0
     */
    public Exchange removeExchange(final String name)
    {   
        return this.exchanges.remove(name);
    }
    
    
    /**
     * This returns the <code>Exchange</code> with the specified name.
     * 
     * @param   name    The name of the exchange.
     * 
     * @since    1.0
     */
    public Exchange lookupExchange(final String name)
    {   
        return this.exchanges.get(name);
    }


    /**
     * This returns the <code>RabbitTemplate</code> for this producer.
     * 
     * @return  The <code>RabbitTemplate</code> for this producer.
     * 
     * @since    1.0
     */
    public RabbitTemplate getRabbitTemplate()
    {
        return this.rabbitTemplate;
    }
    
    
    /**
     * This sets the <code>RabbitTemplate</code> for this producer.
     * 
     * @param   rabbitTemplate  The <code>RabbitTemplate</code> for this producer.
     * 
     * @throws  IllegalArgumentException  Thrown if the template is null.
     * 
     * @since    1.0
     */
    public void setRabbitTemplate(final RabbitTemplate rabbitTemplate)
    {
        if (rabbitTemplate == null)
        {
            throw new IllegalArgumentException("The rabbit template is not set.");
        }
        
        this.rabbitTemplate = rabbitTemplate;
    }
    
    
    /**
     * This returns the host name of the service.
     * 
     * @return  The host name of the service.
     * 
     * @since    1.0
     */
    public String getHostname()
    {
        return this.hostname;
    }
    
    
    /**
     * This sets the host name of the service.
     * 
     * @param   hostname  The host name of the service.
     * 
     * @throws  IllegalArgumentException  Thrown if the host name is null.
     * 
     * @since    1.0
     */
    public void setHostname(String hostname)
    {
        if (hostname == null)
        {
            throw new IllegalArgumentException("The host name is not set.");
        }
        
        this.hostname = hostname;
    }
    
    
    /**
     * This returns the <code>Calendar</code> used by this message producer.
     * 
     * @return  The <code>Calendar</code> used by this message producer.
     * 
     * @since   1.0
     */
    public Calendar getCalendar()
    {
        return this.calendar;
    }
    
    
    /**
     * This publishes a message to a specified exchange.
     * 
     * @param   correlationId   The correlation identifier.
     * @param   replyTo         The reply to destination.
     * @param   exchangeName    The exchange to publish on.
     * @param   routingKey      The routing key.
     * @param   message         The message to publish.
     * 
     * throws   AmqpException   Thrown if the message fails to publish.
     * 
     * @since   1.0
     */
    protected void publishMessage(final String correlationId, final String replyTo,
            final String exchangeName, final String routingKey, final Object message)
        throws AmqpException
    {        
        final MessageProperties messageProperties = 
                MessagePropertiesHelper.makeMessageProperties(
                        this.getCalendar().getTime(), correlationId, replyTo);
        
        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);
        
        final Exchange exchange = this.lookupExchange(exchangeName);
        
        if (exchange == null)
        {
            final Object[] logParams = {exchangeName};
            final String logMessage = 
                    LOGGER.error(RabbitMQMessageCode.NO_EXCHANGE_FOUND_E.getMessageCode(), logParams);
            
            throw new AmqpException(logMessage);
        }
        
        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishMessage : ");
            builder.append("exchange [").append(exchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        this.getRabbitTemplate().convertAndSend(exchange.getName(), 
                                                routingKey, 
                                                message, 
                                                messagePostProcessor);
    }
}

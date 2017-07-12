/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.config;

import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessorCallback;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationNotified;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationWithdrawn;
import com.dell.cpsd.common.rabbitmq.registration.notifier.service.AmqpRegistrationNotifierService;
import com.dell.cpsd.common.rabbitmq.registration.notifier.service.RegistrationNotifierService;
import com.dell.cpsd.common.rabbitmq.registration.notifier.spring.ApplicationContextRegistrationListener;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Configuration
public class RegistrationConfig
{
    public static final String EXCHANGE_COMMON_REGISTRATION_EVENT = "exchange.dell.cpsd.common.registration.event";

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";

    private static final int    MAX_ATTEMPTS     = 10;
    private static final int    INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER       = 2.0;
    private static final int    MAX_INTERVAL     = 50000;

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Bean
    ApplicationContextRegistrationListener registrationListener()
    {
        return new ApplicationContextRegistrationListener();
    }

    @Bean
    RegistrationNotifierService registrationNotifierService()
    {
        return new AmqpRegistrationNotifierService(registrationObjectMapper(), registrationRabbitTemplate(),
                EXCHANGE_COMMON_REGISTRATION_EVENT, null);
    }

    /**
     * This returns the <code>TopicExchange</code> for the registration events.
     *
     * @return The <code>TopicExchange</code> for the registration event messages.
     * @since SINCE-TBD
     */
    @Bean
    TopicExchange registrationEventExchange()
    {
        return new TopicExchange(EXCHANGE_COMMON_REGISTRATION_EVENT);
    }

    private RabbitTemplate registrationRabbitTemplate()
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(registrationMessageConverter());
        template.setRetryTemplate(registrationRetryTemplate());
        return template;
    }

    /**
     * This returns the <code>MessageConverter</code> for the
     * <code>RabbitTemplate</code>.
     *
     * @return The <code>MessageConverter</code> for the template.
     * @since SINCE-TBD
     */
    public MessageConverter registrationMessageConverter()
    {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(registrationClassMapper());
        messageConverter.setCreateMessageIds(true);
        messageConverter.setJsonObjectMapper(registrationObjectMapper());

        return messageConverter;
    }

    /**
     * This returns the <code>RetryTemplate</code> for the <code>RabbitTemplate
     * </code>.
     *
     * @return The <code>RetryTemplate</code>.
     * @since SINCE-TBD
     */
    private RetryTemplate registrationRetryTemplate()
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

    /**
     * Default class mapper. Intentionally not using using the Registration Aware class mapper here.
     *
     * @return a simple class mapper
     */
    private ClassMapper registrationClassMapper()
    {
        //stub
        DefaultClassMapper classMapper = new DefaultClassMapper();
        final Map<String, Class<?>> classMappings = new HashMap<>();

        List<Class<?>> messageClasses = asList(MessageRegistrationNotified.class, MessageRegistrationWithdrawn.class);

        MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();

        messageAnnotationProcessor.process(new MessageAnnotationProcessorCallback()
        {
            @Override
            public void found(String messageType, Class messageClass)
            {
                classMappings.put(messageType, messageClass);
            }

        }, messageClasses);

        classMapper.setIdClassMapping(classMappings);

        return classMapper;
    }

    /**
     * Pretty printing object mapper.
     *
     * @return
     */
    ObjectMapper registrationObjectMapper()
    {
        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        objectMapper.setDateFormat(new SimpleDateFormat(ISO8601_DATE_FORMAT));

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dell.cpsd.service.common.client.context.ConsumerContextConfig;

/**
 * This is the client context configuration for the SAMPLE PAQX
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.0
 */
@Configuration
public class ContextConfig extends ConsumerContextConfig
{

    /**
     * ContextConfig constructor.
     *
     * @since 1.0
     */
    @Autowired
    public ContextConfig(RabbitMQPropertiesConfig properties)
    {
        super(properties.applicationName(), false);
    }

}

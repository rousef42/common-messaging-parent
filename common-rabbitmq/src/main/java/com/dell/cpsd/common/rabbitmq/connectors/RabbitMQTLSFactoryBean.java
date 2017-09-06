/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.connectors;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQException;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.core.io.FileSystemResource;

/**
 * This class is a rabbit connection factory bean extending <code>RabbitConnectionFactoryBean
 * </code> to facilitate setting up of ssl properties on the connection factory of RabbitMQ
 * available in the environment.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @version 1.0
 * @since SINCE-TDB
 */
public class RabbitMQTLSFactoryBean extends RabbitConnectionFactoryBean {

    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(RabbitMQTLSFactoryBean.class);

    public RabbitMQTLSFactoryBean(IRabbitMqPropertiesConfig configuration) {
        super();
        try{
            String trustStorePath = System.getProperty("javax.net.ssl.trustStore");
            String trustStorePassphrase = System.getProperty("javax.net.ssl.trustStorePassword");

            if(trustStorePath == null || trustStorePath.trim().length() == 0)
            {
                throw new RabbitMQException(RabbitMQMessageCode.JAVA_TRUST_STORE_IS_NOT_SET_E.getMessageText());
            }

            if(trustStorePassphrase == null || trustStorePassphrase.trim().length() == 0)
            {
                throw new RabbitMQException(RabbitMQMessageCode.JAVA_TRUST_STORE_PASSWORD_IS_NOT_SET_E.getMessageText());
            }

            this.setUseSSL(configuration.isSslEnabled());
            this.setSslAlgorithm(configuration.tlsVersion());
            this.setKeyStoreType("JKS");
            this.setTrustStoreType("JKS");
            this.setTrustStoreResource(new FileSystemResource(trustStorePath));
            this.setTrustStorePassphrase(trustStorePassphrase);
            this.setKeyStoreResource(new FileSystemResource(trustStorePath));
            this.setKeyStorePassphrase(trustStorePassphrase);
            this.afterPropertiesSet();
        }
        catch (RabbitMQException rabbitMQException){
            LOGGER.error(rabbitMQException.getMessage(), rabbitMQException);
        }
        catch (Exception exception){
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(RabbitMQMessageCode.ERROR_RESPONSE_UNEXPECTED_ERROR_E.getMessageCode(), lparams, exception);
        }
    }
}

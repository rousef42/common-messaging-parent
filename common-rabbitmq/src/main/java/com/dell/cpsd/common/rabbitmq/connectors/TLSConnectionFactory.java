/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.connectors;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by varadr1 on 4/27/17.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @Deprecated
 */

@Deprecated
public class TLSConnectionFactory extends ConnectionFactory
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(TLSConnectionFactory.class);

    public TLSConnectionFactory(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig)
    {
        init(rabbitMqPropertiesConfig);
    }

    public void init(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig)
    {
        try

        {
            SSLContext c = getSSLContext(rabbitMqPropertiesConfig);

            this.setHost(rabbitMqPropertiesConfig.rabbitHostname());
            this.setPort(rabbitMqPropertiesConfig.rabbitPort());
            this.useSslProtocol(c);
        }
        catch (Exception exception)
        {
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(RabbitMQMessageCode.CONNECTION_FACTORY_INIT_E.getMessageCode(), lparams, exception);
        }
    }

    private SSLContext getSSLContext(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig) throws Exception
    {
        char[] keyStorePassphrase = rabbitMqPropertiesConfig.keyStorePassPhrase() == null ? null
                : rabbitMqPropertiesConfig.keyStorePassPhrase().toCharArray();
        char[] trustStorePassphrase = rabbitMqPropertiesConfig.trustStorePassphrase() == null ? null
                : rabbitMqPropertiesConfig.trustStorePassphrase().toCharArray();

        KeyManagerFactory kmf = getKeyManagerFactory(rabbitMqPropertiesConfig.keyStorePath(), keyStorePassphrase);
        TrustManagerFactory tmf = getTrustManagerFactory(rabbitMqPropertiesConfig.trustStorePath(), trustStorePassphrase);

        SSLContext c = SSLContext.getInstance(rabbitMqPropertiesConfig.tlsVersion());
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return c;
    }

    private KeyManagerFactory getKeyManagerFactory(String storePath, char[] storePassphrase) throws Exception
    {
        KeyStore ks = getKeyStore(storePath, storePassphrase, "PKCS12");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, storePassphrase);
        return kmf;
    }

    private TrustManagerFactory getTrustManagerFactory(String storePath, char[] storePassphrase) throws Exception
    {
        KeyStore ks = getKeyStore(storePath, storePassphrase, "JKS");

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        return tmf;
    }

    private KeyStore getKeyStore(String storePath, char[] storePassphrase, String storeType) throws Exception
    {
        KeyStore ks = KeyStore.getInstance(storeType);
        try (FileInputStream fis = getInputStream(storePath))
        {
            ks.load(fis, storePassphrase);
        }
        return ks;
    }


    FileInputStream getInputStream(String name)
    {
        FileInputStream file = null;
        try
        {
            file = new FileInputStream(name);
        }
        catch (Exception exception)
        {
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(RabbitMQMessageCode.CONNECTION_FACTORY_INIT_E.getMessageCode(), lparams, exception);
        }
        return file;
    }
}

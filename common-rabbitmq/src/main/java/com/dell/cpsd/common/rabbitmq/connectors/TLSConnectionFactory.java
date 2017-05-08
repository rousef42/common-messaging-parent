package com.dell.cpsd.common.rabbitmq.connectors;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.rabbitmq.client.ConnectionFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * Created by varadr1 on 4/27/17.
 */
public class TLSConnectionFactory extends ConnectionFactory
{

    public TLSConnectionFactory(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig)
    {
        init(rabbitMqPropertiesConfig);
    }

    public void init(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig)
    {
        try

        {
            System.out.println("Inside TLS connection factory");
            System.out.println("rabbitMqPropertiesConfig.trustStorePathTest()--------------->"+rabbitMqPropertiesConfig.trustStorePathTest());

            System.out.println("rabbitMqPropertiesConfig.keyStorePathTest()----------------->"+rabbitMqPropertiesConfig.keyStorePathTest());

        char[] keyStorePassphrase = rabbitMqPropertiesConfig.keyStorePassPhrase().toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(rabbitMqPropertiesConfig.keyStorePath()), keyStorePassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyStorePassphrase);

            System.out.println("keyStorePassphrase----------------->"+new String(keyStorePassphrase));

        char[] trustPassphrase = rabbitMqPropertiesConfig.trustStorePassphrase().toCharArray();
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream(rabbitMqPropertiesConfig.trustStorePath()), trustPassphrase);

            System.out.println("trustPassphrase----------------->"+new String(trustPassphrase));

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance(rabbitMqPropertiesConfig.tlsVersion());
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        this.setHost(rabbitMqPropertiesConfig.rabbitHostname());
        this.setPort(rabbitMqPropertiesConfig.rabbitPort());
        this.useSslProtocol(c);
    }
        catch (Exception ex)
        {
           System.out.println("exception in TLSconnectionFactory"+ex);
           ex.printStackTrace();
        }
    }
}

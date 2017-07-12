/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since Vision 1.3.0
 */
public class TLSConnectionFactoryTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testTLSConnectionFactory() throws Exception
    {
        IRabbitMqPropertiesConfig config = new RabbitMQPropertiesConfigStub();

        TLSConnectionFactory factory = new TLSConnectionFactoryStub(config);

        assertTrue(factory.isSSL());
        assertEquals(999, factory.getPort());
        assertEquals("host-name", factory.getHost());
    }

    private class TLSConnectionFactoryStub extends TLSConnectionFactory
    {

        public TLSConnectionFactoryStub(IRabbitMqPropertiesConfig rabbitMqPropertiesConfig)
        {
            super(rabbitMqPropertiesConfig);
        }

        @Override
        FileInputStream getInputStream(String name)
        {
            return null;
        }
    }

    private class RabbitMQPropertiesConfigStub implements IRabbitMqPropertiesConfig
    {

        @Override
        public String rabbitHostname()
        {
            return "host-name";
        }

        @Override
        public String secondaryHostnames()
        {
            return null;
        }

        @Override
        public Integer rabbitPort()
        {
            return 999;
        }

        @Override
        public String rabbitPassword()
        {
            return null;
        }

        @Override
        public String rabbitUsername()
        {
            return null;
        }

        @Override
        public String rabbitVirtualHost()
        {
            return null;
        }

        @Override
        public Integer rabbitRequestedHeartbeat()
        {
            return null;
        }

        @Override
        public String dataCenter()
        {
            return null;
        }

        @Override
        public String applicationName()
        {
            return null;
        }

        @Override
        public String trustStorePassphrase()
        {
            return "trust-pass-phrase";
        }

        @Override
        public String keyStorePassPhrase()
        {
            return "key-pass-phrase";
        }

        @Override
        public String keyStorePath()
        {
            return "key-path";
        }

        @Override
        public String trustStorePath()
        {
            return "trust-path";
        }

        @Override
        public String tlsVersion()
        {
            return "TLSv1.2";
        }

        @Override
        public Boolean isSslEnabled()
        {
            return true;
        }

    }
}

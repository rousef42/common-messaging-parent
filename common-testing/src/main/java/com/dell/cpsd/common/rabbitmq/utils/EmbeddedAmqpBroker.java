/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import org.apache.commons.io.FileUtils;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.apache.qpid.server.model.VirtualHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * Embedded AMQP message broker for integration tests.<br/>
 * Uses Apache Qpid.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @see <a href="https://dzone.com/articles/mocking-rabbitmq-for-integration-tests">Article the solution is based on</a>
 * @see <a href="https://qpid.apache.org/">Qpid</a>
 * @since 1.2
 */
public class EmbeddedAmqpBroker implements Closeable
{
    private static final Logger LOGGER           = LoggerFactory.getLogger(MessageLoader.class);
    public static final  String QPID             = "/qpid/";
    public static final  String FILE_PASS        = "passwd.properties";
    public static final  String FILE_QPID_CONFIG = "embedded-config.json";

    private static final int           PORT    = findAvailableTcpPort();
    private static final BrokerOptions OPTIONS = new BrokerOptions();
    private static final Broker        BROKER  = new Broker();

    private static EmbeddedAmqpBroker instance;

    private EmbeddedAmqpBroker() throws Exception
    {
        init();
        start();
    }

    private void init() throws IOException, URISyntaxException
    {
        LOGGER.info("Initializing embedded broker");
        OPTIONS.setConfigProperty("qpid.amqp.version", "0-91"); //Rabbit uses 0.91 AMQP protocol version
        OPTIONS.setConfigProperty("qpid.amqp_port", String.valueOf(PORT));
        final Path tmpPath = Files.createTempDirectory("embeddedBroker");
        OPTIONS.setConfigProperty("qpid.work_dir", tmpPath.toAbsolutePath().toString());
        OPTIONS.setConfigProperty("qpid.pass_file", copyAndReturnPassFile(tmpPath.toAbsolutePath().toString())); // This option doesn't like URL
        OPTIONS.setInitialConfigurationLocation(findQpidConfigUrl()); // URL is ok here
    }

    private void start() throws Exception
    {
        LOGGER.info("Starting embedded broker. Work-dir: " + OPTIONS.getConfigProperties().get("qpid.work_dir") + " / Port: " + PORT);
        BROKER.startup(OPTIONS);
    }

    private String findQpidConfigUrl() throws IOException
    {
        final URL url = EmbeddedAmqpBroker.class.getResource(QPID + FILE_QPID_CONFIG);
        final String path = url.toExternalForm();
        return path;
    }

    private String copyAndReturnPassFile(final String destinationDir) throws IOException, URISyntaxException
    {
        final URL url = EmbeddedAmqpBroker.class.getResource(QPID + FILE_PASS);
        final File dest = new File(destinationDir + File.separator + FILE_PASS);
        FileUtils.copyURLToFile(url, dest);
        return dest.getAbsolutePath();
    }

    public Broker getQpidBroker()
    {
        return BROKER;
    }

    public int getPort()
    {
        return PORT;
    }

    /**
     * Gets default virtual host, should be the only one.
     */
    public VirtualHost getVHost()
    {
        return BROKER.getBroker().findVirtualHostByName("default");
    }

    @Override
    public void close() throws IOException
    {
        LOGGER.info("Closing embedded broker");
        BROKER.shutdown();
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }

    /**
     * Should be changed to Spring bean.
     */
    public static EmbeddedAmqpBroker getInstance() throws Exception
    {
        if (instance == null)
        {
            synchronized (EmbeddedAmqpBroker.class)
            {
                if (instance == null)
                {
                    instance = new EmbeddedAmqpBroker();
                }
            }
        }
        return instance;

    }
}

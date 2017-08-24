/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/
package com.dell.cpsd.common.integration.docker.compose;

import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.SuccessOrFailure;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 * This is the abstract class that provides the information necessary for docker compose to know when a service is ready
 **/
abstract class ContainerServiceInfo implements ServiceInfo
{
    static final Logger LOGGER = LoggerFactory.getLogger(ContainerServiceInfo.class);

    private final String                 service;
    private final Duration               timeout;
    private final HealthCheck<Container> healthCheck;

    ContainerServiceInfo(String service, HealthCheck<Container> healthCheck, Duration timeout)
    {
        this.service = service;
        this.healthCheck = healthCheck;
        this.timeout = timeout;
    }

    public String getServiceName()
    {
        return service;
    }

    public HealthCheck<Container> getHealthCheck()
    {
        return healthCheck;
    }

    public Duration getTimeout()
    {
        return timeout;
    }

    /**
     * Sleep for an amount of seconds
     *
     * @param durationInSeconds
     */
    static void sleep(int durationInSeconds)
    {
        try
        {
            Thread.sleep(durationInSeconds * 1000);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("InterruptedException while sleeping", e);
        }

    }

    /**
     * Check is a log file contains a given string.
     *
     * @param logFileName
     * @param s
     * @return
     */
    static SuccessOrFailure logFileContains(String logFileName, String s)
    {
        StringBuffer fileContents = null;
        File logFile = new File("containersStartupLogs/" + logFileName);
        if (logFile.exists())
        {
            try (FileReader reader = new FileReader(logFile))
            {
                try (BufferedReader br = new BufferedReader(reader))
                {
                    fileContents = new StringBuffer();
                    String line = "";
                    while ((line = br.readLine()) != null)
                    {
                        fileContents.append(line).append('\n');
                    }
                    String contentsString = fileContents.toString();

                    return contentsString.contains(s) ?
                            SuccessOrFailure.success() :
                            SuccessOrFailure.failure("Log file " + logFileName + " doesn't contain " + s + " yet");
                }
                catch (IOException e)
                {
                    LOGGER.error("IOException for BufferedReader", e);
                }
            }
            catch (IOException e)
            {
                LOGGER.error("IOException for FileReader", e);
            }

        }
        return SuccessOrFailure.failure("log file doesn't exist yet");
    }

    /**
     * The time to sleep between the runs of health checks on each container
     * @return
     */
    static int getSleepTime()
    {
        return 3;
    }

    /**
     * The timeout for a container to be declared unhealthy
     * @return
     */
    static Duration getTimeoutValue()
    {
        return Duration.standardMinutes(10);
    }
}

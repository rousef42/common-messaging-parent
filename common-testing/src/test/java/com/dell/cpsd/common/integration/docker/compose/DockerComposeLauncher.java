/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 **/

package com.dell.cpsd.common.integration.docker.compose;

import com.dell.cpsd.common.integration.docker.compose.strategy.DeleteLogsShutdownStrategy;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.ImmutableDockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;
import com.palantir.docker.compose.connection.DockerMachine;
import com.palantir.docker.compose.execution.KillDownShutdownStrategy;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 * This is the shutdown strategy class.  We need to delete the logs also, as some of them cause jenkins to fail the NextB scan.
 **/
public class DockerComposeLauncher
{
    private static final Logger                     LOGGER = LoggerFactory.getLogger(DockerComposeLauncher.class);

    /**
     * The global DOCKER object that is exposed to unit tests
     * Handy things like the external port for a service can be gleamed from this object
     */
    public static ImmutableDockerComposeRule DOCKER    = null;

    /**
     * The entry point for launching docker compose
     *
     * @param services                           - The services list to be spun up
     * @param envFilePath                        - The env file to be sourced if necessary
     * @param variables                          - The variables to pass in to initialize the containers
     * @param timeoutValue                       - The overall time that will be waited for all containers to start up
     * @param dockerComposeRulesLoggingDirectory - The log directory to which the docker log output for each container is written to
     * @param dockerComposeFilePaths             - The relative path to the docker compose file
     * @param deleteLogs                         - Delete logs when done? defaults to yes if you want integration tests to pass
     * @param leaveContainersRunning             - Set to false by default but change to true if you want to debug integration tests locally
     */
    public static ImmutableDockerComposeRule launchDockerCompose(ServiceInfo[] services, String envFilePath, String[][] variables,
            Duration timeoutValue, String dockerComposeRulesLoggingDirectory, String[] dockerComposeFilePaths, boolean deleteLogs,
            boolean leaveContainersRunning)
    {
        Properties properties = null;

        if (envFilePath != null)
        {
            //Load .env properties
            properties = loadEnvProperties(envFilePath);
        }

        //Pass properties to docker
        DockerMachine dockerMachine = createDockerMachine(properties, variables);

        DockerComposeRule.Builder dockerStart = DockerComposeRule.builder().machine(dockerMachine)
                .nativeServiceHealthCheckTimeout(timeoutValue).pullOnStartup(true).saveLogsTo(dockerComposeRulesLoggingDirectory)
                .files(DockerComposeFiles.from(dockerComposeFilePaths)).removeConflictingContainersOnStartup(true)
                .shutdownStrategy(leaveContainersRunning ? (x, y) -> {
                } : deleteLogs ? new DeleteLogsShutdownStrategy(dockerComposeRulesLoggingDirectory) : new KillDownShutdownStrategy());

        //Spin up each container
        for (ServiceInfo service : services)
        {
            dockerStart = dockerStart.waitingForService(service.getServiceName(), service.getHealthCheck(), service.getTimeout());
        }
        DOCKER = dockerStart.build();
        return DOCKER;
    }

    /**
     * Loads properties from a properties file
     *
     * @param filePath
     * @return
     */
    private static Properties loadEnvProperties(String filePath)
    {
        Properties properties = new Properties();

        File envFile = new File(filePath);

        if (envFile.exists())
        {
            try (FileInputStream fis = new FileInputStream(new File(filePath));)
            {
                try (InputStreamReader isr = new InputStreamReader(fis);)
                {
                    properties.load(isr);
                }
                catch (IOException ex)
                {
                    LOGGER.error("IOException", ex);
                }
            }
            catch (IOException ex2)

            {
                LOGGER.error("IOException", ex2);
            }
        }
        return properties;
    }

    private static DockerMachine createDockerMachine(Properties properties, String[][] variables)
    {
        DockerMachine.LocalBuilder interimMachine = DockerMachine.localMachine().withEnvironment(propertiesToStringMap(properties));
        for (String[] keyValue : variables)
        {
            interimMachine = interimMachine.withAdditionalEnvironmentVariable(keyValue[0], keyValue[1]);
            LOGGER.info("Extra env variable: " + keyValue[0] + ", " + keyValue[1]);
        }
        return interimMachine.build();
    }

    /**
     * Converts a properties object to a map object.
     *
     * @param properties
     * @return
     */
    private static Map<String, String> propertiesToStringMap(final Properties properties)
    {
        //Create a String,String map of the env properties to pass to the docker machine
        Map<String, String> map = new HashMap<>();

        if (properties!=null)
        {
            final Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements())
            {
                String propertyName = propertyNames.nextElement().toString();
                map.put(propertyName, properties.get(propertyName).toString());
            }
            LOGGER.info("Map of properties is: " + map);
        }
        return map;
    }

    public static String getIPForContainer(String containerName)
    {
        String ip = null;
        try
        {
            String containerId=DOCKER.dockerCompose().id(DOCKER.containers().container(containerName)).orElse(" NO ID!");

            Process process = DOCKER.dockerExecutable()
                    .execute("inspect", "--format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'", containerId);
            ip = getOutputFromProcess(process);

            //Some docker versions give back the IP in single quotes
            ip=ip.replaceAll("'","");

            process.waitFor(20, TimeUnit.SECONDS);
            LOGGER.info("IP for " + containerName + "is: " + ip);

        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.error("Process error: " + e);
        }
        return ip;

    }

    private static String getOutputFromProcess(Process process)
    {
        StringBuffer builder = new StringBuffer();
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try
                {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        builder.append(line);
                    }
                    reader.close();
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
            executor.awaitTermination(20, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Process error: " + e);
        }
        return builder.toString();
    }
}
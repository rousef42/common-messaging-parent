/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.common.integration.docker.compose;

import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.SuccessOrFailure;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 * This is the interface that provides the information necessary for docker compose to know when a service is ready
 **/
public interface ServiceInfo
{
    String getServiceName();

    Duration getTimeout();

    HealthCheck<Container> getHealthCheck();

    class GoVCSimServiceInfo extends ContainerServiceInfo
    {
        public GoVCSimServiceInfo()
        {
            super("govcsim", (x) ->
            {
                sleep(getSleepTime());
                //TODO: Is there a better way to get this confirmation?
                return logFileContains("govcsim.log", "GOVC_URL");
            }, getTimeoutValue());
        }
    }

    class CredentialServiceInfo extends ContainerServiceInfo
    {
        public CredentialServiceInfo()
        {
            super("credential", (x) ->
            {
                sleep(getSleepTime());
                //TODO: Is there a better way to get this confirmation?
                return logFileContains("credential.log", "JVM running");
            }, getTimeoutValue());
        }
    }

    class VaultServiceInfo extends ContainerServiceInfo
    {
        public VaultServiceInfo()
        {
            super("vault", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("vault.log", "Vault server started");
            }, getTimeoutValue());
        }
    }

    class MongoServiceInfo extends ContainerServiceInfo
    {
        public MongoServiceInfo()
        {
            super("mongo", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("mongo.log", "waiting for connections");
            }, getTimeoutValue());
        }
    }

    class RackHDInfo extends ContainerServiceInfo
    {
        public RackHDInfo()
        {
            super("rackhd", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("rackhd.log", "[on-http] [Http.Server] [Server] Server Started.");
            }, getTimeoutValue());
        }
    }

    class TaskGraphServiceInfo extends ContainerServiceInfo
    {
        public TaskGraphServiceInfo()
        {
            super("taskgraph", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("taskgraph.log", "Task Graph Runner Started");
            }, getTimeoutValue());
        }
    }

    class AmqpServiceInfo extends ContainerServiceInfo
    {
        public AmqpServiceInfo()
        {
            super("amqp", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("amqp.log", "Server startup complete");
            }, getTimeoutValue());
        }

    }

    class ConsulServiceInfo extends ContainerServiceInfo
    {
        public ConsulServiceInfo()
        {
            super("consul", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("consul.log", "New leader elected");
            }, getTimeoutValue());
        }
    }

    class CapabilityServiceInfo extends ContainerServiceInfo
    {
        public CapabilityServiceInfo()
        {
            super("capability-registry", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("amqp.log", "Server startup complete");
            }, getTimeoutValue());
        }
    }

    class EndpointServiceInfo extends ContainerServiceInfo
    {
        public  EndpointServiceInfo()
        {
            super("endpoint-registry", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("endpoint-registry.log", "JVM running");
            }, getTimeoutValue());
        }
    }

    class VCenterServiceInfo extends ContainerServiceInfo
    {
        public VCenterServiceInfo()
        {
            super("vcenter-adapter", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("vcenter-adapter.log", "JVM running");
            }, getTimeoutValue());
        }
    }

    class CoprHdServiceInfo extends ContainerServiceInfo
    {
        public CoprHdServiceInfo()
        {
            super("coprhd-adapter", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("coprhd-adapter.log", "JVM running");
            }, getTimeoutValue());
        }
    }

    class RackHDServiceInfo extends ContainerServiceInfo
    {
        public RackHDServiceInfo()
        {
            super("rackhd-adapter", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("rackhd-adapter.log", "JVM running");
            }, getTimeoutValue());
        }
    }

    class SystemDefinitionServiceInfo extends ContainerServiceInfo
    {
        public SystemDefinitionServiceInfo()
        {
            super("system-definition-service", (x) ->
            {
                sleep(getSleepTime());
                return logFileContains("system-definition-service.log", "Consumers are already running");
            }, getTimeoutValue());
        }
    }
}

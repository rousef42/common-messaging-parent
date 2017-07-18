/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.common.integration.docker.suite;

import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import org.joda.time.Duration;

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
}
package com.dell.cpsd.common.rabbitmq.message;

/**
 * Marker interface for messages having 'correlationId' property.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface HasCorrelationId
{
    String getCorrelationId();

    void setCorrelationId(String correlationId);
}

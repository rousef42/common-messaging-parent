package com.dell.cpsd.common.rabbitmq.message;

/**
 * Marker interface for messages having 'messageProperties' property.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface HasMessageProperties<T extends MessagePropertiesContainer>
{
    T getMessageProperties();

    void setMessageProperties(T messageProperties);
}

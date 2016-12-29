package com.dell.cpsd.common.rabbitmq.message;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
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

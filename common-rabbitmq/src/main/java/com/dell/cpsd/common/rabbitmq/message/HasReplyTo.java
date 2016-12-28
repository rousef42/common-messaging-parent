package com.dell.cpsd.common.rabbitmq.message;

/**
 * Marker interface for messages having 'reply-to' property.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface HasReplyTo
{
    String getReplyTo();
}

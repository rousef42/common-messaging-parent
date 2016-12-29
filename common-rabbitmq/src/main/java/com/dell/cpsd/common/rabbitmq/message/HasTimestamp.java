package com.dell.cpsd.common.rabbitmq.message;

import java.util.Date;

/**
 * Marker interface for messages having 'timestamp' property.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface HasTimestamp
{
    Date getTimestamp();

    void setTimestamp(Date timestamp);
}

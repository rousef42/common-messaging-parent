/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

/**
 * Marker interface for messages having 'messageProperties' property.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public interface HasMessageProperties<T extends MessagePropertiesContainer>
{
    T getMessageProperties();

    void setMessageProperties(T messageProperties);
}

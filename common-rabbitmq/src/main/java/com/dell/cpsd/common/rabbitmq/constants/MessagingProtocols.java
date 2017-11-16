/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.constants;

/**
 * Enum of possible messaging protocols.
 * 
 * 
 *         <p>
 *         Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 *         </p>
 */
public enum MessagingProtocols
{
    AMQP("AMQP");

    private String value;

    private MessagingProtocols(final String value)
    {
        this.value = value;
    }

    /**
     * Provides the String value of the Enum type
     * 
     * @return the String value of the Enum type
     */
    public String value()
    {
        return value;
    }
}

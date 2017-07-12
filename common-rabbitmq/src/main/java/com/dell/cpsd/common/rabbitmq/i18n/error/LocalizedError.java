/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.i18n.error;

/**
 * Localized version of error.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class LocalizedError
{
    private String messageCode;
    private String message;

    @Override
    public String toString()
    {
        return message;
    }

    public String getMessageCode()
    {
        return messageCode;
    }

    public LocalizedError setMessageCode(String messageCode)
    {
        this.messageCode = messageCode;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public LocalizedError setMessage(String message)
    {
        this.message = message;
        return this;
    }
}

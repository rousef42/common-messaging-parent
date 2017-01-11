/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.ErrorContainer;

/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class Error implements ErrorContainer
{
    private String code;
    private String message;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    @Override
    public void setMessage(String message)
    {
        this.message = message;
    }
}

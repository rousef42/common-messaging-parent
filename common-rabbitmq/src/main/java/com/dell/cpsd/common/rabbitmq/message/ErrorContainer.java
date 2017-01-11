package com.dell.cpsd.common.rabbitmq.message;

/**
 * Basic interface for an error in error response.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface ErrorContainer
{
    String getCode();

    void setCode(String code);

    String getMessage();

    void setMessage(String detail);
}

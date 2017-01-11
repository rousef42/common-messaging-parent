package com.dell.cpsd.common.rabbitmq.message;

import java.util.List;

/**
 * Indicates that message has array of errors. Used mostly for error responses.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface HasErrors<T extends ErrorContainer>
{
    List<T> getErrors();

    void setErrors(List<T> errorMessages);
}

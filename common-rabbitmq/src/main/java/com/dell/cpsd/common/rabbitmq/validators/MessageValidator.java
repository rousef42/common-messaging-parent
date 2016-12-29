package com.dell.cpsd.common.rabbitmq.validators;

/**
 * Interface for message validator.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface MessageValidator<M>
{
    ValidationResult validate(M message);
}

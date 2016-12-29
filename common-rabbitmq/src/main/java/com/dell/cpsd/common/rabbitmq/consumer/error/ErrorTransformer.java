package com.dell.cpsd.common.rabbitmq.consumer.error;

/**
 * Transforms message processing exception into more specific exception. Ideally, ResponseMessageException.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface ErrorTransformer<M>
{
    Exception transform(Exception e, M requestMessage);
}

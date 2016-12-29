package com.dell.cpsd.common.rabbitmq.message;

/**
 * Interface for work with typical AMQP message fields.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface MessagePropertiesContainer extends HasCorrelationId, HasReplyTo, HasTimestamp
{

}

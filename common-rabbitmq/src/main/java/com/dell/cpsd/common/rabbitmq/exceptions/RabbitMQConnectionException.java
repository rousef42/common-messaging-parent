/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */


package com.dell.cpsd.common.rabbitmq.exceptions;

/**
 * This class represents an error creating the connection with RabbitMQ service.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @version 1.0
 * @since SINCE-TDB
 */
public final class RabbitMQConnectionException extends Exception
{
    /*
     * The default serial version identifier.
     */
    private static final long serialVersionUID = 1000001L;

    /**
     * RabbitMQConnectionException constructor.
     *
     * @param message The exception message.
     * @since SINCE-TDB
     */
    public RabbitMQConnectionException(String message)
    {
        super(message);
    }

    /**
     * RabbitMQConnectionException constructor.
     *
     * @param cause The cause of the exception.
     * @since SINCE-TDB
     */
    public RabbitMQConnectionException(Throwable cause)
    {
        super(cause);
    }

    /**
     * RabbitMQConnectionException constructor.
     *
     * @param message The exception message.
     * @param cause   The cause of the exception.
     * @since SINCE-TDB
     */
    public RabbitMQConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

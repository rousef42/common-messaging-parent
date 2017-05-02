/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.exceptions;

/**
 * This class define a generic Exception for the rabbitmq logic.
 * <p>
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 *
 * @version 1.0
 * @since SINCE-TDB
 */
public class RabbitMQException extends Exception
{
    /*
     * The default serial version identifier.
     */
    private static final long serialVersionUID = 1000001L;

    /**
     * RabbitMQException constructor.
     *
     * @param message The exception message.
     * @since SINCE-TDB
     */
    public RabbitMQException(String message)
    {
        super(message);
    }

    /**
     * RabbitMQException constructor.
     *
     * @param cause The cause of the exception.
     * @since SINCE-TDB
     */
    public RabbitMQException(Throwable cause)
    {
        super(cause);
    }

    /**
     * RabbitMQException constructor.
     *
     * @param message The exception message.
     * @param cause   The cause of the exception.
     * @since SINCE-TDB
     */
    public RabbitMQException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

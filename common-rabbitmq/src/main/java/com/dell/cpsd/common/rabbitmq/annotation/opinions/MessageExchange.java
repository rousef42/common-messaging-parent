/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.annotation.opinions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to indicate a POJO to be a message class.
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 *
 * @since   SINCE-TDB
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageExchange
{
    /**
     * This is the message example
     *
     * @return the message example
     */
    String exchange();

    /**
     * type
     *
     * @return
     */
    MessageExchangeType exchangeType() default MessageExchangeType.TOPIC;

    /**
     * routing key
     *
     * @return
     */
    String routingKey() default OpinionConstants.UNDEFINED;
}

/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.annotation;

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
public @interface Message
{
    /**
     * This is the message Id, for example the __TypeId__ used for SpringAMQP
     *
     * @return the messageId
     */
    String value();

    /**
     * This is the message version
     *
     * @return the message version
     */
    String version();

    /**
     * message correlationId property
     *
     * @return
     */
    String correlationIdProperty() default "correlationId";

    /**
     * message timestamp property
     *
     * @return
     */
    String timestampProperty() default "timestamp";

    /**
     * Content type
     *
     * @return
     */
    MessageContentType content() default MessageContentType.CLEAR;
}

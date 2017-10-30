/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.annotation.stereotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to indicate a POJO to be a message class.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 * 
 * @since SINCE-TDB
 */
/**
 * The class is deprecated as it will be moved to amqp-contract-extension repo, The implementation will be provided in the next release of
 * common-messaging-parent and amqp-contract-extension
 */
@Deprecated
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageEvent
{
    MessageStereotype stereotype() default MessageStereotype.EVENT;
}

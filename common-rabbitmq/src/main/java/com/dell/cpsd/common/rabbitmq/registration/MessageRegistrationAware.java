/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */


package com.dell.cpsd.common.rabbitmq.registration;

import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;

import java.util.Collection;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface MessageRegistrationAware
{
    Collection<MessageRegistrationDto> getRegistrations();

    boolean isAutoRegister();
}

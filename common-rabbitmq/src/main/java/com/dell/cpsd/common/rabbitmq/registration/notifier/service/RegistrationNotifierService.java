/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.service;

import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface RegistrationNotifierService
{
    void notify(MessageRegistrationDto entry);

    void withdraw(MessageRegistrationDto entry);
}

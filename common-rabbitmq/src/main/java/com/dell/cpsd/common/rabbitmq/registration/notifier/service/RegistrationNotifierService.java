/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.service;

import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface RegistrationNotifierService
{
    void notify(MessageRegistrationDto entry);

    void withdraw(MessageRegistrationDto entry);
}

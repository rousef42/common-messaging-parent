/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration;

import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;

import java.util.Collection;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface MessageRegistrationAware
{
    Collection<MessageRegistrationDto> getRegistrations();

    boolean isAutoRegister();
}

/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.model;

import java.util.List;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageQueueDto
{
    private String name;

    public MessageQueueDto(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}

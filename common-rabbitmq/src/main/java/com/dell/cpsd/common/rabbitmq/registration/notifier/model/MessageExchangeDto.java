/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.model;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageExchangeDto
{
    private String               name;
    private MessageDirectionType direction;
    private List<BindingDataDto> bindings;

    public MessageExchangeDto(String name, MessageDirectionType direction, BindingDataDto... bindings)
    {
        this.name = name;
        this.direction = direction;
        this.bindings = Arrays.asList(bindings);
    }

    public String getName()
    {
        return name;
    }

    public MessageDirectionType getDirection()
    {
        return direction;
    }

    public List<BindingDataDto> getBindings()
    {
        return bindings;
    }
}

/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageExchange
{
    @JsonProperty("name")
    private String name;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("bindings")
    private List<MessageBinding> bindings;

    public MessageExchange(String name, String direction, List<MessageBinding> bindings)
    {
        this.name = name;
        this.direction = direction;
        this.bindings = bindings;
    }

    public String getName()
    {
        return name;
    }

    public String getDirection()
    {
        return direction;
    }

    public List<MessageBinding> getBindings()
    {
        return bindings;
    }
}

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

import java.util.Date;

/**
 * <p>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.0
 */
public class TestMessageProperties implements MessagePropertiesContainer
{
    private Date   timestamp;
    private String correlationId;
    private String replyTo;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }

    public String getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo(String replyTo)
    {
        this.replyTo = replyTo;
    }
}

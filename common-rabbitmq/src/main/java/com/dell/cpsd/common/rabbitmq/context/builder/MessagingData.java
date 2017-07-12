/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import java.util.List;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @author Connor Goulding
 */
public class MessagingData
{
    private List<MessageMetaData> messages;

    public List<MessageMetaData> getMessages()
    {
        return messages;
    }

    public void setMessages(List<MessageMetaData> messages)
    {
        this.messages = messages;
    }
}

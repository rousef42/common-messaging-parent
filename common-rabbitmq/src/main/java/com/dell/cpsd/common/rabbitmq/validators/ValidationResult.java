/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Validation result holder.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since SINCE-TBD
 */
public class ValidationResult
{
    private List<String> messages = new ArrayList<>();
    private boolean      valid    = true;

    public List<String> getMessages()
    {
        return Collections.unmodifiableList(messages);
    }

    public void add(final List<String> newMessages)
    {
        this.messages.addAll(newMessages);
    }

    public void add(final String newMessage)
    {
        this.messages.add(newMessage);
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(final boolean valid)
    {
        this.valid = valid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ValidationResult{");
        sb.append("valid=").append(valid);
        sb.append(", messages:\n");
        for (final String message : messages)
        {
            sb.append(message);
            sb.append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}

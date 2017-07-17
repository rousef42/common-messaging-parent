/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.i18n;

import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Default message formatting functionality.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultMessageFormatter
{
    private final ResourceBundle bundle;

    public DefaultMessageFormatter(ResourceBundle bundle)
    {
        this.bundle = bundle;
    }

    public DefaultMessageFormatter(Class<? extends ResourceBundle> bundleClass)
    {
        this.bundle = ResourceBundle.getBundle(bundleClass.getName());
    }

    public LocalizedError getApplicationMessage(String messageCode, Object... params)
    {
        LocalizedError message = new LocalizedError();
        message.setMessage(getMessage(messageCode, params));
        message.setMessageCode(messageCode);
        return message;
    }

    public String getMessage(String messageCode, Object... params)
    {
        String localizedFormat;

        try
        {
            localizedFormat = bundle.getString(messageCode);
        }
        catch (MissingResourceException exception)
        {
            return messageCode;
        }

        if (isEmpty(params))
        {
            return localizedFormat;
        }

        return MessageFormat.format(localizedFormat, params);
    }

    protected ResourceBundle getBundle()
    {
        return bundle;
    }
}

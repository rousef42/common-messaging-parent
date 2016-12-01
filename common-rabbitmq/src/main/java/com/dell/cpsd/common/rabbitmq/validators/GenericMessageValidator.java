/*
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic message validator.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since SINCE-TBD
 */
public abstract class GenericMessageValidator<R extends Object>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericMessageValidator.class);

    /**
     * Implementation of message validation.
     *
     * @param requestMessage the request message
     * @throws Throwable the throwable
     */
    protected abstract ValidationResult validateMessage(final R requestMessage) throws Throwable;

    /**
     * Validate the message
     *
     * @param requestMessage The message to be consumed
     */
    public ValidationResult validate(final R requestMessage)
    {
        try
        {
            return validateMessage(requestMessage);
        }
        catch (Throwable e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}

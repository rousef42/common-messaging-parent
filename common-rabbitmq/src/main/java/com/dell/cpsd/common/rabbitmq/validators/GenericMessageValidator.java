/*
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.VALIDATION_INTERNAL_ERROR_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.VALIDATION_MESSAGE_IS_NULL_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.VALIDATION_PROPERTY_IS_NULL_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.VALIDATION_STRING_IS_EMPTY_E;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Generic message validator.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 */
public abstract class GenericMessageValidator<M> implements MessageValidator<M>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericMessageValidator.class);

    /**
     * Implementation of message validation.
     *
     * @param message the request message
     * @param validationResult validation details to populate
     * @throws Exception the throwable
     */
    protected abstract void validateMessage(M message, ValidationResult validationResult) throws Exception;

    /**
     * Validate the message.
     * In case of internal exception, returns ValidationResult with error.
     *
     * @param message The message to be consumed
     */
    @Override
    public ValidationResult validate(M message)
    {
        if (message == null)
        {
            return failedValidationResult(VALIDATION_MESSAGE_IS_NULL_E.getMessageText());
        }

        try
        {
            ValidationResult result = new ValidationResult();
            validateMessage(message, result);
            return result;
        }
        catch (Exception e)
        {
            String msg = VALIDATION_INTERNAL_ERROR_E.getMessageText(e.getMessage());
            LOGGER.error(msg, e);
            return failedValidationResult(msg);
        }
    }

    protected void validateNotNull(Object value, String property, ValidationResult validationResult)
    {
        if (value == null)
        {
            validationResult.addError(VALIDATION_PROPERTY_IS_NULL_E.getMessageText(property));
        }
    }

    protected void validateNotEmpty(String value, String property, ValidationResult validationResult)
    {
        if (isBlank(value))
        {
            validationResult.addError(VALIDATION_STRING_IS_EMPTY_E.getMessageText(property));
        }
    }

    protected ValidationResult failedValidationResult(String error)
    {
        ValidationResult result = new ValidationResult();
        result.addError(error);
        return result;
    }
}

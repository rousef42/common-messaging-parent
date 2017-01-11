/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validation result holder.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 */
public class ValidationResult
{
    private List<LocalizedError> errors = new ArrayList<>();

    public List<LocalizedError> getLocalizedErrors()
    {
        return Collections.unmodifiableList(errors);
    }

    public ValidationResult addError(LocalizedErrorCode localizedErrorCode, Object... params)
    {
        this.errors.add(localizedErrorCode.getLocalizedError(params));
        return this;
    }

    public ValidationResult addError(LocalizedError error)
    {
        this.errors.add(error);
        return this;
    }

    /**
     * @return list of error messages
     * @deprecated Use getLocalizedErrors() instead.
     */
    @Deprecated
    public List<String> getErrors()
    {
        return errors
                .stream()
                .map(LocalizedError::getMessage)
                .collect(Collectors.toList());
    }

    /**
     * @param newErrors error messages to add
     * @return itself
     * @deprecated Use addError(LocalizedError) instead.
     */
    @Deprecated
    public ValidationResult addErrors(List<String> newErrors)
    {
        for (String error : newErrors)
        {
            addError(error);
        }
        return this;
    }

    /**
     * @param error error message to add
     * @return itself
     * @deprecated Use addError(LocalizedError) instead.
     */
    @Deprecated
    public ValidationResult addError(String error)
    {
        LocalizedError message = new LocalizedError();
        message.setMessage(error);
        this.errors.add(message);
        return this;
    }

    public boolean isValid()
    {
        return errors.isEmpty();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ValidationResult{");
        sb.append("valid=").append(isValid());
        sb.append(", messages:\n");
        for (LocalizedError message : errors)
        {
            sb.append(message);
            sb.append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}

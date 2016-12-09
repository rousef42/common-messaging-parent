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
 */
public class ValidationResult
{
    private List<String> errors = new ArrayList<>();

    public List<String> getErrors()
    {
        return Collections.unmodifiableList(errors);
    }

    public void addErrors(List<String> newErrors)
    {
        this.errors.addAll(newErrors);
    }

    public void addError(String error)
    {
        this.errors.add(error);
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
        for (final String message : errors)
        {
            sb.append(message);
            sb.append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}

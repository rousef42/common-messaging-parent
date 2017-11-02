/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

import java.util.List;

/**
 * Indicates that message has array of errors. Used mostly for error responses.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @deprecated use {@link com.dell.cpsd.contract.extension.amqp.message.HasErrors} instead
 */
@Deprecated
public interface HasErrors<T extends ErrorContainer>
{
    List<T> getErrors();

    void setErrors(List<T> errorMessages);
}

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

/**
 * Basic interface for an error in error response.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @deprecated use {@link com.dell.cpsd.contract.extension.amqp.message.ErrorContainer} instead
 */
@Deprecated
public interface ErrorContainer
{
    String getCode();

    void setCode(String code);

    String getMessage();

    void setMessage(String detail);
}

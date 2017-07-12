/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.i18n.error;

import java.util.List;

/**
 * Provides related localized errors. Intended for exceptions.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public interface LocalizedErrorsProvider
{

    List<LocalizedError> getLocalizedErrors();
}

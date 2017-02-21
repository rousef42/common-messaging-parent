/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.springframework.integration.aggregator.CorrelationStrategy;

/**
 * Correlation strategies
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since TBD
 */
public class CorrelationStrategies
{
    public static CorrelationStrategy subGroupCorrelationStrategy()
    {
        return (item) ->
        {
            String correlationId = (String) item.getHeaders().get("correlation-id");
            String correlatedBy = correlationId.contains("$") ? correlationId.substring(0, correlationId.indexOf('$')) : correlationId;
            return correlatedBy;
        };
    }
}

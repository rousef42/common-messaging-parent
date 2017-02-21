/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.aggregate;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.aggregator.TimeoutCountSequenceSizeReleaseStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregation strategies for Spring aggregator, Release strategies and correlation strategies
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since TBD
 */
public class AggregationStrategies
{
    public enum CORRELATION_STRATEGIES
    {
        GROUP_SUBMESSAGES;
    }

    public enum RELEASE_STRATEGIES
    {
        MESSAGE_COUNT, TIMEOUT_COUNT;
    }

    private static final Map<CORRELATION_STRATEGIES, CorrelationStrategy> correlationStrategies = new HashMap<>();

    static
    {
        correlationStrategies.put(CORRELATION_STRATEGIES.GROUP_SUBMESSAGES, (item) ->
        {
            String correlationId = (String) item.getHeaders().get("correlation-id");
            String correlatedBy = correlationId.contains("$") ? correlationId.substring(0, correlationId.indexOf('$')) : correlationId;
            return correlatedBy;
        });
    }

    public static CorrelationStrategy getCorrelationStrategy(CORRELATION_STRATEGIES correlationStrategy)
    {
        return correlationStrategies.get(correlationStrategy);
    }

    public static ReleaseStrategy getReleaseStrategy(RELEASE_STRATEGIES releaseStrategy, Object... parameters)
    {
        switch (releaseStrategy)
        {
            case MESSAGE_COUNT:
                return new MessageCountReleaseStrategy((Integer)parameters[0]);
            case TIMEOUT_COUNT:
                return new TimeoutCountSequenceSizeReleaseStrategy((Integer)parameters[0], (Long)parameters[1]);
            default:
                return null;
        }
    }
}

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import java.util.HashMap;
import java.util.Map;

/**
 * Test data type for MessageAggregatorTest.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public class TestAggregatorWrapper implements AggregatorWrapper
{
    private Map<String, TestMessagesAggregate> aggregates = new HashMap<>();

    @Override
    public void forceCompletion(final String initialRequestCorrelationId)
    {
        aggregates.get(initialRequestCorrelationId).forceCompletion();
    }

    @Override
    public boolean checkIfCorrelationIdPresent(final String initialRequestCorrelationId) throws Exception
    {
        return aggregates.containsKey(initialRequestCorrelationId);
    }

    protected Map<String, TestMessagesAggregate> getAggregates()
    {
        return aggregates;
    }

}

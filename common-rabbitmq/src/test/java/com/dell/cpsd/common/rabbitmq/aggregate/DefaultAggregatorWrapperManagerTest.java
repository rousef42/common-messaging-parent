/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * This is the default aggregator wrapper manager test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public class DefaultAggregatorWrapperManagerTest
{
    private AggregatorWrapperManager<TestAggregatorWrapper> mgr;

    @Test
    public void createWithNull() throws Exception
    {
        try
        {
            mgr = new DefaultAggregatorWrapperManager<>(null);
            fail("Should throw an exception");
        }
        catch (IllegalArgumentException e)
        {
            //fine
        }
    }

    @Test
    public void lookupAggregatorWrapperEmptyList() throws Exception
    {
        mgr = new DefaultAggregatorWrapperManager<>(Collections.emptyList());
        assertNull(mgr.lookupAggregatorWrapper(null));
        assertNull(mgr.lookupAggregatorWrapper(""));
        assertNull(mgr.lookupAggregatorWrapper("a"));
    }

    @Test
    public void lookupAggregatorWrapperNotEmptyList() throws Exception
    {
        List<TestAggregatorWrapper> list = new ArrayList<>();
        TestAggregatorWrapper wrap1 = new TestAggregatorWrapper();
        TestAggregatorWrapper wrap2 = new TestAggregatorWrapper();
        list.add(wrap1);
        list.add(wrap2);
        mgr = new DefaultAggregatorWrapperManager<>(list);
        assertNull(mgr.lookupAggregatorWrapper(null));
        assertNull(mgr.lookupAggregatorWrapper(""));
        assertNull(mgr.lookupAggregatorWrapper("a"));

        TestMessagesAggregate agg11 = new TestMessagesAggregate();
        agg11.updateCorrelationStatus("correlation1-1-1", CorrelationStatus.REQUESTED);
        wrap1.getAggregates().put("b", agg11);

        TestMessagesAggregate agg12 = new TestMessagesAggregate();
        agg12.updateCorrelationStatus("correlation1-2-1", CorrelationStatus.REQUESTED);
        wrap1.getAggregates().put("correlation1-2-1", agg12);

        TestMessagesAggregate agg21 = new TestMessagesAggregate();
        agg21.updateCorrelationStatus("correlation2-1-1", CorrelationStatus.REQUESTED);
        wrap2.getAggregates().put("correlation2-1-1", agg21);

        assertNull(mgr.lookupAggregatorWrapper(null));
        assertNull(mgr.lookupAggregatorWrapper(""));
        assertNull(mgr.lookupAggregatorWrapper("a"));
        assertEquals(wrap1, mgr.lookupAggregatorWrapper("b"));
        assertEquals(wrap1, mgr.lookupAggregatorWrapper("correlation1-2-1"));
        assertEquals(wrap2, mgr.lookupAggregatorWrapper("correlation2-1-1"));
    }

}

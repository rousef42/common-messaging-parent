/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Abstract aggregate test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public class AbstractAggregateTest
{
    private AbstractAggregate agg = new TestAbstractAggregate();

    @Test
    public void isComplete() throws Exception
    {
        assertTrue(agg.isComplete());
    }

    @Test
    public void forceCompletion() throws Exception
    {
        assertFalse(agg.isForcedToComplete());
        agg.forceCompletion();
        assertTrue(agg.isForcedToComplete());
        agg.forceCompletion();
        assertTrue(agg.isForcedToComplete());
        assertTrue(agg.isComplete());
    }

    @Test
    public void isForcedToComplete() throws Exception
    {
        assertFalse(agg.isForcedToComplete());
        agg.forceCompletion();
        assertTrue(agg.isForcedToComplete());
    }

    @Test
    public void updateCorrelationStatusEmptyCorrelation() throws Exception
    {
        //Nothing yet
        assertFalse(agg.containsStatus(CorrelationStatus.FAILED));

        agg.updateCorrelationStatus(null, CorrelationStatus.FAILED);
        assertFalse(agg.containsStatus(CorrelationStatus.FAILED));
        agg.updateCorrelationStatus("", CorrelationStatus.FAILED);
        assertTrue(agg.containsStatus(CorrelationStatus.FAILED));
    }

    @Test
    public void updateCorrelationStatusNull() throws Exception
    {
        //Nothing yet
        assertFalse(agg.containsStatus(null));

        //Only nulls
        agg.updateCorrelationStatus("1", null);
        assertTrue(agg.containsStatus(null));
        agg.updateCorrelationStatus("2", null);
        assertTrue(agg.containsStatus(null));

        //Not only nulls
        agg.updateCorrelationStatus("3", CorrelationStatus.FAILED);
        assertTrue(agg.containsStatus(null));

        //Let's overwrite nulls
        agg.updateCorrelationStatus("1", CorrelationStatus.REQUESTED);
        assertTrue(agg.containsStatus(null));
        agg.updateCorrelationStatus("2", CorrelationStatus.RESPONDED);
        assertFalse(agg.containsStatus(null));
    }

    @Test
    public void updateCorrelationStatusNotNull() throws Exception
    {
        //Nothing yet
        assertFalse(agg.containsStatus(null));
        assertFalse(agg.containsStatus(CorrelationStatus.FAILED));
        assertFalse(agg.containsStatus(CorrelationStatus.REQUESTED));
        assertFalse(agg.containsStatus(CorrelationStatus.RESPONDED));

        agg.updateCorrelationStatus("1", null);
        agg.updateCorrelationStatus("2", CorrelationStatus.FAILED);
        agg.updateCorrelationStatus("3", CorrelationStatus.RESPONDED);
        agg.updateCorrelationStatus("4", CorrelationStatus.REQUESTED);
        agg.updateCorrelationStatus("5", CorrelationStatus.RESPONDED);
        assertTrue(agg.containsStatus(CorrelationStatus.RESPONDED));
        assertTrue(agg.containsStatus(CorrelationStatus.REQUESTED));
        assertTrue(agg.containsStatus(null));
        assertTrue(agg.containsStatus(CorrelationStatus.FAILED));

        agg.updateCorrelationStatus("4", CorrelationStatus.RESPONDED);
        assertTrue(agg.containsStatus(CorrelationStatus.RESPONDED));
        assertFalse(agg.containsStatus(CorrelationStatus.REQUESTED));
        assertTrue(agg.containsStatus(null));
        assertTrue(agg.containsStatus(CorrelationStatus.FAILED));
    }

    @Test
    public void statusTransitionCorrect() throws Exception
    {
        assertTrue(agg.statusTransitionCorrect(null));
        assertTrue(agg.statusTransitionCorrect(CorrelationStatus.REQUESTED));
        assertFalse(agg.statusTransitionCorrect(CorrelationStatus.RESPONDED));
        assertFalse(agg.statusTransitionCorrect(CorrelationStatus.FAILED));
    }

    @Test
    public void containsRequested() throws Exception
    {
        //Nothing yet
        assertFalse(agg.containsRequested());

        agg.updateCorrelationStatus("1", null);
        agg.updateCorrelationStatus("2", CorrelationStatus.FAILED);
        agg.updateCorrelationStatus("3", CorrelationStatus.RESPONDED);
        assertFalse(agg.containsRequested());
        agg.updateCorrelationStatus("4", CorrelationStatus.REQUESTED);
        agg.updateCorrelationStatus("5", CorrelationStatus.RESPONDED);
        assertTrue(agg.containsRequested());

        //Overwrite
        agg.updateCorrelationStatus("4", CorrelationStatus.RESPONDED);
        assertFalse(agg.containsRequested());
    }

    @Test
    public void containsStatus() throws Exception
    {
        //Nothing yet
        assertFalse(agg.containsStatus(null));
        assertFalse(agg.containsStatus(CorrelationStatus.FAILED));
        assertFalse(agg.containsStatus(CorrelationStatus.REQUESTED));
        assertFalse(agg.containsStatus(CorrelationStatus.RESPONDED));

        agg.updateCorrelationStatus("1", null);
        agg.updateCorrelationStatus("2", CorrelationStatus.FAILED);
        agg.updateCorrelationStatus("3", CorrelationStatus.RESPONDED);
        agg.updateCorrelationStatus("4", CorrelationStatus.REQUESTED);
        agg.updateCorrelationStatus("5", CorrelationStatus.RESPONDED);
        assertTrue(agg.containsStatus(CorrelationStatus.RESPONDED));
        assertTrue(agg.containsStatus(CorrelationStatus.REQUESTED));
        assertTrue(agg.containsStatus(null));
        assertTrue(agg.containsStatus(CorrelationStatus.FAILED));
    }

    @Test
    public void testToString() throws Exception
    {
        String stringNothing = agg.toString();
        assertNotNull(stringNothing);

        agg.updateCorrelationStatus("1", CorrelationStatus.FAILED);
        String stringUpdated = agg.toString();
        assertNotNull(stringUpdated);
        assertNotSame(stringNothing, stringUpdated);

        agg.forceCompletion();
        String stringForced = agg.toString();
        assertNotNull(stringForced);
        assertNotSame(stringForced, stringNothing);
        assertNotSame(stringForced, stringUpdated);
    }

    private class TestAbstractAggregate
            extends AbstractAggregate
    {

    }

}
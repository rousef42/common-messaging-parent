/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import java.util.function.Consumer;

/**
 * Aggregator interface for aggregating multiple related messages.
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version TBD
 * @see <a href="http://www.enterpriseintegrationpatterns.com/patterns/messaging/Aggregator.html">Aggregator pattern</a>
 * @see <a href="http://docs.spring.io/spring-integration/docs/2.0.0.RC1/reference/html/aggregator.html">Spring integrations aggregator</a>
 * @since TBD
 */
public interface MessageAggregator<M extends Aggregate>
{
    /**
     * Trigger processing of an aggregate for given ID.
     */
    void process(final String correlationId, final Consumer<M> updateAction);
}

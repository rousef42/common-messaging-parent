/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.aggregator.TimeoutCountSequenceSizeReleaseStrategy;

/**
 * Release strategies
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since TBD
 */
public class ReleaseStrategies
{
    public static ReleaseStrategy messageCountReleaseStrategy(int messageThreshold)
    {
        return new MessageCountReleaseStrategy(messageThreshold);
    }

    public static ReleaseStrategy timeoutOrThresholdReleaseStrategy(int messageCountThreshold, long timeoutInMillis)
    {
        return new TimeoutCountSequenceSizeReleaseStrategy(messageCountThreshold, timeoutInMillis);
    }
}

/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.annotation;

/**
 * Determines whether a given message should be federated across RabbitMQ 
 * clusters, or kept only within the cluster.
 * <p/>
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 *
 * @since   SINCE-TDB
 */
public enum MessageVisibility
{
    CLUSTER,
    FEDERATED
}

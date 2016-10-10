/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.i18n;

import java.util.ListResourceBundle;

/**
 * This is the resource bundle for AMQP library.
 * 
 * <p>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * 
 * @version 1.0
 * 
 * @since    SINCE-TDB
 */
public class RabbitMQMessageBundle extends ListResourceBundle
{
    /*
     * The messages for this bundle.
     */
    private static final Object[][] CONTENTS = 
    {
        {"VAMQP1001E", "VAMQP1001E Failed to initialize the AMQP connection factory. Reason [{0}]"},
        {"VAMQP1002I", "VAMQP1002I Setting up AMQP connection factory values [{0}:{1}]"},
        {"VAMQP1003E", "VAMQP1003E AMQP failed to connect to [{0}]. Reason [{1}] "},
        {"VAMQP1004E", "VAMQP1004E There are no AMQP broker connections available."},
        {"VAMQP1005E", "VAMQP1005E "},
        {"VAMQP1006E", "VAMQP1006E [{0}] Unhandled message received: [{1}]"}
    };
    
    
    /**
     * RabbitMQMessageBundle constructor.
     * 
     * @since   SINCE-TDB
     */
    public RabbitMQMessageBundle()
    {
        super();
    }

    
    /**
     * This returns the messages for this bundle.
     * 
     * @return  The messages for this bundle.
     * 
     * @since   SINCE-TDB
     */
    @Override
    protected Object[][] getContents()
    {
        return CONTENTS;
    }
}

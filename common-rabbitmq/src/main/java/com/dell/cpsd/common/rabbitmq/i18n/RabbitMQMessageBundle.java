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
        {"VAMQP1006E", "VAMQP1006E [{0}] Unhandled message received: [{1}]"},
        {"VAMQP1007E", "VAMQP1007E [{0}] AMQP acknowledgment error: [{1}]"},
        {"VAMQP1008E", "VAMQP1008E AMQP error (attempt {0}): [{1}]"},
        {"VAMQP1009E", "VAMQP1009E [{0}] AMQP error: [{1}]"},
        {"VAMQP1010E", "VAMQP1010E Failed to create error message for exception [{0}]. Reason:"},
        {"VAMQP1011E", "VAMQP1011E Can not create response error message: property [{0}] not specified"},
        {"VAMQP1012E", "VAMQP1012E Request failed with unexpected error: {0}"},

        {"VAMQP2001E", "VAMQP2001E Unexpected error during validation: [{0}]"},
        {"VAMQP2002E", "VAMQP2002E Provided message is null."},
        {"VAMQP2003E", "VAMQP2003E Message property [{0}] is null."},
        {"VAMQP2004E", "VAMQP2004E Message property [{0}] is empty."},
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

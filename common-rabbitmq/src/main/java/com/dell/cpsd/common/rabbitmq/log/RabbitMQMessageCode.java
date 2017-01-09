/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.log;

import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.dell.cpsd.common.rabbitmq.i18n.RabbitMQMessageBundle;

/**
 * This is the message code enum for the package subscription client.
 * 
 * <p>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * 
 * @version  1.0
 * 
 * @since    SINCE-TDB
 */
public enum RabbitMQMessageCode
{
    CONNECTION_FACTORY_INIT_E(1001,     "VAMQP1001E"),
    CONNECTION_FACTORY_INIT_I(1002,     "VAMQP1002I"),
    AMQP_CONNECTION_FAILURE_E(1003,     "VAMQP1003E"),
    NO_AMQP_CONNECTIONS_E(1004,         "VAMQP1004E"),
    REUSE_1005_E(1005,                  "VAMQP1005E"),
    MESSAGE_CONSUMER_E(1006,            "VAMQP1006E"),
    MESSAGE_IMMEDIATE_ACK_E(1007,       "VAMQP1007E"),
    AMQP_ERROR_RETRY_E(1008,            "VAMQP1008E"),
    AMQP_ERROR_E(1009,                  "VAMQP1009E"),
    ERROR_RESPONSE_FAILED_E(1010,       "VAMQP1010E"),
    ERROR_RESPONSE_NO_PROPERTY_E(1011,  "VAMQP1011E"),

    VALIDATION_INTERNAL_ERROR_E(2001,   "VAMQP2001E"),
    VALIDATION_MESSAGE_IS_NULL_E(2002,  "VAMQP2002E"),
    VALIDATION_PROPERTY_IS_NULL_E(2003, "VAMQP2003E"),
    VALIDATION_STRING_IS_EMPTY_E(2004,  "VAMQP2004E"),
    ;
    

    /*
     * The path to the resource bundle
     */
    private static final ResourceBundle BUNDLE = 
                ResourceBundle.getBundle(RabbitMQMessageBundle.class.getName());
    
    /*
     * The error code.
     */
    private final int errorCode;
    
    /*
     * The message code.
     */
    private final String messageCode;
    
    
    /**
     * RabbitMQMessageCode constructor
     * 
     * @param   errorCode    The error code.
     * @param   messageCode  The message code.
     * 
     * @since   SINCE-TDB
     */
    private RabbitMQMessageCode(int errorCode, String messageCode)
    {
       this.errorCode = errorCode;
       this.messageCode = messageCode;
    }
    
    
    /**
     * This returns the message code.
     * 
     * @return  The message code.
     * 
     * @since   SINCE-TDB
     */
    public String getMessageCode()
    {
        return this.messageCode;
    }

    
    /**
     * This returns the error code.
     * 
     * @return  The error code.
     * 
     * @since   SINCE-TDB
     */
    public int getErrorCode()
    {
        return this.errorCode;
    }

    
    /**
     * This returns the error text.
     * 
     * @return  The error text.
     * 
     * @since   SINCE-TDB
     */
    public String getErrorText()
    {
        try
        {
            return BUNDLE.getString(this.messageCode);
            
        } catch (MissingResourceException exception)
        {
            return this.messageCode;
        }
    }
    

    /**
     * This formats the  message using the array of parameters.
     * 
     * @param   params   The message parameters.
     * 
     * @return  The localized message populated with the parameters.
     * 
     * @since   SINCE-TDB
     */
    public String getMessageText(Object... params)
    {
        String message = null;
        
        try
        {
            message = BUNDLE.getString(this.messageCode);
            
        } catch (MissingResourceException exception)
        {
            return this.messageCode;
        }
        
        if ((params == null) || (params.length == 0)) 
        {
            return message;
        }
       
        return MessageFormat.format(message, params);
    }
    
}

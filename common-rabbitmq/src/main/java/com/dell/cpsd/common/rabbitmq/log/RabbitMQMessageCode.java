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
    MESSAGE_CONSUMER_E(1006,            "VAMQP1006E");
    

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
    public String getMessageText(Object[] params)
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

/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.logging;

import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;

/**
 * This is an <code>ILogger</code> delegate for a <code>Log</code>.
 * 
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @version 1.0
 * 
 * @since   SINCE-TDB
 */
public class DelegateLogger implements ILogger
{
    
    // TODO : add variable arguments once initial cut of code is in place
    
    static final long      serialVersionUID = -1L;

    /*
     * The reference to the <code>Logger</code>.
     */
    private Logger          logger           = null;

    /*
     * The <code>ResourceBundle</code> for this logger.
     */
    private ResourceBundle resourceBundle   = null;
    

    /**
     * DelegateLogger constructor.
     * 
     * @param   logger  The reference to the <code>Logger</code>.
     * 
     * @since   SINCE-TDB
     */
    public DelegateLogger(Logger logger)
    {
        super();
       
        if (logger == null)
        {
            throw new IllegalArgumentException("The reference to the Log is null");
        }
       
        this.logger = logger;
    }
    
    
    /**
     * This returns true if the debug logging level is enabled.
     * 
     * @return  True if the debug logging level is enabled.
     * 
     * @since   SINCE-TDB
     */
    public boolean isDebugEnabled()
    {
        return this.logger.isDebugEnabled();
    }
    
    
    /**
     * This logs the message at the debug level.
     *   
     * @param   message  The message to log.
     * 
     * @since   SINCE-TDB
     */
    public void debug(String message)
    {
        this.logger.debug(message);
    }
    
    
    /**
     * This returns true if the trace logging level is enabled.
     * 
     * @return  True if the trace logging level is enabled.
     * 
     * @since   SINCE-TDB
     */
    public boolean isTraceEnabled()
    {
        return this.logger.isTraceEnabled();
    }
    
    
    /**
     * This logs the message at the trace level.
     *   
     * @param   message  The message to log.
     * 
     * @since   SINCE-TDB
     */
    public void trace(String message)
    {
        this.logger.trace(message);
    }
    
    
    /**
     * This returns true if the information logging level is enabled.
     * 
     * @return  True if the information logging level is enabled.
     * 
     * @since   SINCE-TDB
     */
    public boolean isInfoEnabled()
    {
        return this.logger.isInfoEnabled();
    }
    
    
    /**
     * This logs the message at the information level.
     *   
     * @param   message  The message to log.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String info(String message)
    {
        String msg = this.formatMessage(message);
       
        this.logger.info(msg);

        return msg;
    }
    
    
    /**
     * This logs the message at the information level.
     *   
     * @param   message  The message to log.
     * @param   params   The message parameters.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String info(String message, Object[] params)
    {
        String msg = this.formatMessage(message, params);
       
        this.logger.info(msg);
       
        return msg;
    }
    
   
    /**
     * This logs the message at the warning level.
     *   
     * @param   message  The message to log.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String warn(String message)
    {
        String msg = this.formatMessage(message);
       
        this.logger.warn(msg);
       
        return msg;
    }
    
  
    /**
     * This logs the message at the warning level.
     *   
     * @param   message  The message to log.
     * @param   params   The message parameters.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String warn(String message, Object[] params)
    {
        String msg = this.formatMessage(message, params);
       
        this.logger.warn(msg);
       
        return msg;
    }
    
    
    /**
     * This logs the message at the error level.
     *   
     * @param   message  The message to log.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String error(String message)
    {
        String msg = this.formatMessage(message);
       
        this.logger.error(msg);
       
        return msg;
    }
    
    
    /**
     * This logs the message at the error level.
     *   
     * @param   message  The message to log.
     * @param   params   The message parameters.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String error(String message, Object[] params)
    {
        String msg = this.formatMessage(message, params);
       
        this.logger.error(msg);

        return msg;
    }
    
    
    /**
     * This logs the message at the error level.
     *   
     * @param   message  The message to log.
     * @param   thrown   The exception that was thrown.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String error(String message, Throwable thrown)
    {
        String msg = this.formatMessage(message);
       
        this.logger.error(message, thrown);
       
        return msg;
    }
    
   
    /**
     * This logs the message at the error level.
     *   
     * @param   message  The message to log.
     * @param   params   The message parameters.
     * @param   thrown   The exception that was thrown.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    public String error(String message, Object[] params, Throwable thrown)
    {
        String msg = this.formatMessage(message, params);
       
        this.logger.error(message, thrown);
       
        return msg;
    }
    
    
    /**
     * This sets the resource bundle for this logger.
     * 
     * @param   resourceBundle  The <code>ResourceBundle</code> for this logger.
     * 
     * @since   SINCE-TDB
     */
    public void setResourceBundle(ResourceBundle resourceBundle)
    {
        this.resourceBundle = resourceBundle;
    }
    
    
    /*
     * This formats the specified message.
     * 
     * @param   message  The message to format.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    private String formatMessage(String message)
    {
        if (message == null)
        {
            return "";
        }
       
        if (this.resourceBundle == null)
        {
            return message;
        }

        String msg = null;

        try
        {
            msg = this.resourceBundle.getString(message);

        }
        catch (MissingResourceException exception)
        {
            return message;
        }

        if ((msg == null) || (msg.trim().length() == 0))
        {
            return message;
        }

        return msg;
    }
    
    
    /*
     * This formats the  message using the array of parameters.
     * 
     * @param   message  The message to log.
     * @param   params   The message parameters.
     * 
     * @return  The localized message.
     * 
     * @since   SINCE-TDB
     */
    private String formatMessage(String message, Object[] params)
    {
        if (message == null)
        {
            return "";
        }

        if ((params == null) || (params.length == 0))
        {
            return message;
        }

        if (this.resourceBundle == null)
        {
            return MessageFormat.format(message, params);
        }

        String msg = null;

        try
        {
            msg = this.resourceBundle.getString(message);

        }
        catch (MissingResourceException exception)
        {
            return message;
        }

        if ((msg == null) || (msg.trim().length() == 0))
        {
            return message;
        }

        return MessageFormat.format(msg, params);
    }
}

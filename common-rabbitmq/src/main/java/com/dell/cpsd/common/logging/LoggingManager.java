/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * This class is a factory for <code>ILogger</code> instances.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since SINCE-TDB
 */
public class LoggingManager
{

    /**
     * LoggingManager constructor.
     *
     * @since SINCE-TDB
     */
    public LoggingManager()
    {
        super();
    }

    /**
     * This returns an <code>ILogger</code> using the name of the specified
     * class as the logger name.
     *
     * @param clazz The <code>Class</code> used for the logger.
     * @return The <code>ILogger</code> with the name of the class.
     * @throws IllegalArgumentException Thrown if the class is null.
     * @since SINCE-TDB
     */
    public ILogger getLogger(Class clazz)
    {
        return this.getLogger(clazz, null);
    }

    /**
     * This returns an <code>ILogger</code> using the name of the specified
     * class as the logger name.
     *
     * @param clazz          The <code>Class</code> used for the logger.
     * @param resourceBundle The message resource bundle to use.
     * @return The <code>ILogger</code> with the name of the class.
     * @throws IllegalArgumentException Thrown if the class is null.
     * @since SINCE-TDB
     */
    public ILogger getLogger(Class clazz, ResourceBundle resourceBundle)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("The class reference is null.");
        }

        Logger logger = LoggerFactory.getLogger(clazz.getName());

        DelegateLogger dLogger = new DelegateLogger(logger);

        dLogger.setResourceBundle(resourceBundle);

        return dLogger;
    }

    /**
     * This returns an <code>ILogger</code> using the specified name as the
     * logger name.
     *
     * @param name The name of the logger.
     * @return The <code>ILogger</code> with the specified name.
     * @throws IllegalArgumentException Thrown if the logger name is null.
     * @since SINCE-TDB
     */
    public ILogger getLogger(String name)
    {
        return this.getLogger(name, null);
    }

    /**
     * This returns an <code>ILogger</code> using the specified name as the
     * logger name.
     *
     * @param name           The name of the logger.
     * @param resourceBundle The message resource bundle to use.
     * @return The <code>ILogger</code> with the specified name.
     * @throws IllegalArgumentException Thrown if the logger name is null.
     * @since SINCE-TDB
     */
    public ILogger getLogger(String name, ResourceBundle resourceBundle)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("The logger name is null.");
        }

        Logger logger = LoggerFactory.getLogger(name);

        DelegateLogger dLogger = new DelegateLogger(logger);

        dLogger.setResourceBundle(resourceBundle);

        return dLogger;
    }
}

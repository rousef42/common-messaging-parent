/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.logging;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Unit tests for DelegateLogger.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since Vision 1.3.0
 */
public class DelegateLoggerTest
{
    private DelegateLogger delegateLogger;
    private Logger         logger;
    private ResourceBundle resources;

    @Before
    public void setUp() throws Exception
    {
        logger = Mockito.mock(Logger.class);
        delegateLogger = new DelegateLogger(logger);
        resources = new MyResources();
    }

    @Test
    public void testDebug()
    {
        String message = "test message";
        Mockito.doNothing().when(logger).debug(message);

        delegateLogger.debug(message);

        Mockito.verify(logger).debug(message);
    }

    @Test
    public void testTrace()
    {
        String message = "test message";
        Mockito.doNothing().when(logger).trace(message);

        delegateLogger.trace(message);

        Mockito.verify(logger).trace(message);
    }

    @Test
    public void testInfoString()
    {
        String message = "test message";
        Mockito.doNothing().when(logger).info(message);

        String actualMsg = delegateLogger.info(message);

        assertEquals(message, actualMsg);
        Mockito.verify(logger).info(message);
    }

    @Test
    public void testInfoStringResource()
    {
        String message = "msg1";
        String expectedMsg = resources.getString(message);
        delegateLogger.setResourceBundle(resources);
        Mockito.doNothing().when(logger).info(message);

        String actualMsg = delegateLogger.info(message);

        assertEquals(expectedMsg, actualMsg);
        Mockito.verify(logger).info(expectedMsg);
    }

    @Test
    public void testInfoStringParams()
    {
        String message = "msg3";
        String param = "param";
        String expectedMsg = MessageFormat.format(resources.getString(message), param);
        delegateLogger.setResourceBundle(resources);
        Mockito.doNothing().when(logger).info(message);

        String actualMsg = delegateLogger.info(message, new String[] {param});

        assertEquals(expectedMsg, actualMsg);
        Mockito.verify(logger).info(expectedMsg);
    }

    @Test
    public void testWarnString()
    {
        String message = "test message";
        Mockito.doNothing().when(logger).warn(message);

        String actualMsg = delegateLogger.warn(message);

        assertEquals(message, actualMsg);
        Mockito.verify(logger).warn(message);
    }

    @Test
    public void testWarnStringResource()
    {
        String message = "msg1";
        String expectedMsg = resources.getString(message);
        delegateLogger.setResourceBundle(resources);
        Mockito.doNothing().when(logger).warn(message);

        String actualMsg = delegateLogger.warn(message);

        assertEquals(expectedMsg, actualMsg);
        Mockito.verify(logger).warn(expectedMsg);
    }

    @Test
    public void testErrorString()
    {
        String message = "test message";
        Mockito.doNothing().when(logger).error(message);

        String actualMsg = delegateLogger.error(message);

        assertEquals(message, actualMsg);
        Mockito.verify(logger).error(message);
    }

    @Test
    public void testErrorStringResource()
    {
        String message = "msg1";
        String expectedMsg = resources.getString(message);
        delegateLogger.setResourceBundle(resources);
        Mockito.doNothing().when(logger).error(message);

        String actualMsg = delegateLogger.error(message);

        assertEquals(expectedMsg, actualMsg);
        Mockito.verify(logger).error(expectedMsg);
    }

    @Test
    public void testErrorStringThrowable()
    {
        String message = "msg3";
        String param = "param";
        String expectedMsg = MessageFormat.format(resources.getString(message), param);
        Exception ex = new Exception("Expected");

        delegateLogger.setResourceBundle(resources);
        Mockito.doNothing().when(logger).error(expectedMsg, ex);

        String actualMsg = delegateLogger.error(message, new String[] {param}, ex);

        assertEquals(expectedMsg, actualMsg);
        Mockito.verify(logger).error(expectedMsg, ex);
    }

    class MyResources extends ResourceBundle
    {
        private Map<String, String> resources = Collections.unmodifiableMap(Stream
                .of(new SimpleEntry<>("msg1", "message 1"), new SimpleEntry<>("msg2", "message 2"),
                        new SimpleEntry<>("msg3", "message {0}"))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

        @Override
        public Object handleGetObject(String key)
        {
            String value = null;
            if (resources.containsKey(key))
            {
                value = resources.get(key);
            }
            return value;
        }

        @Override
        public Enumeration<String> getKeys()
        {
            return Collections.enumeration(resources.keySet());
        }

        @Override
        protected Set<String> handleKeySet()
        {
            return resources.keySet();
        }
    }

}

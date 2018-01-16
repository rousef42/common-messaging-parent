/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.json.utils;

import java.io.UncheckedIOException;

import org.junit.Test;

/**
 * ResourcesSchemaClient test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.1
 */
public class ResourcesSchemaClientTest
{
    private static final String SCHEMA_DIR   = "/messages/";
    private static final String INCLUDES_DIR = SCHEMA_DIR;
    
    private ResourcesSchemaClient cut = new ResourcesSchemaClient(INCLUDES_DIR, SCHEMA_DIR);
    
    @Test(expected = UncheckedIOException.class)
    public void testGet_UncheckedIOException() {
        cut.get("test.jsd");
    }
}

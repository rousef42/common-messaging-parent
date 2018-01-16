/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.json.utils;

import org.junit.Test;

import static com.dell.cpsd.common.json.utils.JsonSchemaValidation.validateSchema;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * JSON schema validator test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.1
 */
public class JsonSchemaValidationTest
{
    //Here everything is in the same folder
    public static final String SCHEMA_DIR   = "/messages/";
    public static final String INCLUDES_DIR = SCHEMA_DIR;
    public static final String EXAMPLE_DIR  = SCHEMA_DIR;

    @Test
    public void ValidateSchemaPositive() throws Exception
    {
        String messageName = "ping";
        String errors = validateSchema(SCHEMA_DIR + messageName + ".jsd", EXAMPLE_DIR + messageName + ".json", INCLUDES_DIR);
        assertNull(errors, errors);
    }

    @Test
    public void ValidateSchemaPositiveUsage() throws Exception
    {
        String messageName = "ping";
        String errors = validateSchema(SCHEMA_DIR + messageName + ".jsd", EXAMPLE_DIR + messageName + ".usage.json", INCLUDES_DIR);
        assertNull(errors, errors);
    }

    @Test
    public void ValidateSchemaNegative() throws Exception
    {
        String errors = validateSchema(SCHEMA_DIR + "ping.jsd", EXAMPLE_DIR + "pong.json", INCLUDES_DIR);
        assertNotNull(errors);
        assertTrue(errors.contains("message"));
    }
    
    @Test
    public void ValidateSchemaPositive_schemaDir() throws Exception
    {
        String messageName = "ping";
        String errors = validateSchema(SCHEMA_DIR + messageName + ".jsd", EXAMPLE_DIR + messageName + ".json", INCLUDES_DIR, SCHEMA_DIR);
        assertNull(errors, errors);
    }

    @Test
    public void ValidateSchemaPositiveUsage_schemaDir() throws Exception
    {
        String messageName = "ping";
        String errors = validateSchema(SCHEMA_DIR + messageName + ".jsd", EXAMPLE_DIR + messageName + ".usage.json", INCLUDES_DIR, SCHEMA_DIR);
        assertNull(errors, errors);
    }

    @Test
    public void ValidateSchemaNegative_schemaDir() throws Exception
    {
        String errors = validateSchema(SCHEMA_DIR + "ping.jsd", EXAMPLE_DIR + "pong.json", INCLUDES_DIR, SCHEMA_DIR);
        assertNotNull(errors);
        assertTrue(errors.contains("message"));
    }
}

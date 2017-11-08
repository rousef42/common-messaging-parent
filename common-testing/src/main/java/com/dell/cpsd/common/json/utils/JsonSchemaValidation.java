/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.json.utils;

import com.dell.cpsd.common.rabbitmq.utils.MessageLoader;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * JSON schema validation utils.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.1
 * @since 1.0
 */
public final class JsonSchemaValidation
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLoader.class);

    private JsonSchemaValidation()
    {
        // Utility class
    }

    /**
     * Validate JSON message against schema definition.
     *
     * @param schemaResourcePath
     *            absolute path to schema resource
     * @param jsonResourcePath
     *            absolute path to message example resource
     * @param includesDir
     *            additional path to look for includes
     * @return null if correct, string with description when error.
     * @since 1.0
     */
    public static String validateSchema(final String schemaResourcePath, final String jsonResourcePath, final String includesDir)
    {
        try (final InputStream streamSchema = JsonSchemaValidation.class.getResourceAsStream(schemaResourcePath);
                final InputStream streamExample = JsonSchemaValidation.class.getResourceAsStream(jsonResourcePath))
        {
            final String originalContent = IOUtils.toString(streamExample);
            final String body = MessageLoader.removeAmqpToolWrapper(originalContent);
            final ByteArrayInputStream bodyIS = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            return validateSchema(streamSchema, bodyIS, includesDir);
        }
        catch (final IOException e)
        {
            final String message = e.getMessage() + "\n" + schemaResourcePath + "\n" + jsonResourcePath;
            LOGGER.error(message, e);
            return message;
        }
    }

    /**
     * 
     * Validate JSON message against schema definition.This method will search the referenced JSD in includesDir.If it is not found, then it
     * will search in the externalDir which has to be in classpath.
     * 
     * @param schemaResourcePath
     *            absolute path to schema resource
     * @param jsonResourcePath
     *            absolute path to message example resource
     * @param includesDir
     *            additional path to look for includes
     * @param externalDir
     *            external directory in classpath to look for referenced file
     * @return null if correct, string with description when error.
     * 
     * @since 2.3.0
     */
    public static String validateSchema(final String schemaResourcePath, final String jsonResourcePath, final String includesDir,
            final String externalDir)
    {
        try (final InputStream streamSchema = JsonSchemaValidation.class.getResourceAsStream(schemaResourcePath);
                final InputStream streamExample = JsonSchemaValidation.class.getResourceAsStream(jsonResourcePath))
        {
            final String originalContent = IOUtils.toString(streamExample);
            final String body = MessageLoader.removeAmqpToolWrapper(originalContent);
            final ByteArrayInputStream bodyIS = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            return validateSchema(streamSchema, bodyIS, includesDir, externalDir);
        }
        catch (final IOException e)
        {
            final String message = e.getMessage() + "\n" + schemaResourcePath + "\n" + jsonResourcePath;
            LOGGER.error(message, e);
            return message;
        }
    }

    /**
     * Validate JSON message against schema definition.
     *
     * @param streamSchema
     *            schema InputStream
     * @param streamExample
     *            message example InputStream
     * @param includesDir
     *            additional path to look for includes
     * @return null if correct, string with description when error.
     * @since 1.0
     */
    public static String validateSchema(final InputStream streamSchema, final InputStream streamExample, final String includesDir)
    {
        final JSONTokener schemaTokener = new JSONTokener(streamSchema);
        final JSONObject rawSchema = new JSONObject(schemaTokener);
        final Schema schema;
        try
        {
            final SchemaClient client = new ResourcesSchemaClient(includesDir);
            schema = SchemaLoader.load(rawSchema, client);
        }
        catch (final SchemaException e)
        {
            LOGGER.error("Schema loading error", e);
            return e.getLocalizedMessage();
        }
        try
        {
            final JSONTokener messageTokener = new JSONTokener(streamExample);
            final JSONObject messageObject = new JSONObject(messageTokener);
            schema.validate(messageObject);
        }
        catch (final ValidationException e)
        {
            final String message = "Schema: " + schema.getTitle() + " / " + schema.getDescription();
            LOGGER.error(message);
            return message + "\n" + readValidationException(e);
        }
        catch (final JSONException e)
        {
            final String message = "Schema: " + schema.getTitle() + " / " + schema.getDescription() + " JSON parsing failed.";
            LOGGER.error(message, e);
            return message + "\n" + e.getLocalizedMessage();
        }
        // Everything fine
        return null;
    }

    /**
     * Validate JSON message against schema definition.This method will search the referenced JSD in includesDir.If it is not found, then it
     * will search in the externalDir which has to be in classpath.
     *
     * @param streamSchema
     *            schema InputStream
     * @param streamExample
     *            message example InputStream
     * @param includesDir
     *            additional path to look for includes
     * @param externalDir
     *            external directory in classpath to look for referenced file
     * @return null if correct, string with description when error.
     * @since 2.3.0
     */
    public static String validateSchema(final InputStream streamSchema, final InputStream streamExample, final String includesDir,
            final String externalDir)
    {
        final JSONTokener schemaTokener = new JSONTokener(streamSchema);
        final JSONObject rawSchema = new JSONObject(schemaTokener);
        final Schema schema;
        try
        {
            final SchemaClient client = new ResourcesSchemaClient(includesDir, externalDir);
            schema = SchemaLoader.load(rawSchema, client);
        }
        catch (final SchemaException e)
        {
            LOGGER.error("Schema loading error", e);
            return e.getLocalizedMessage();
        }
        try
        {
            final JSONTokener messageTokener = new JSONTokener(streamExample);
            final JSONObject messageObject = new JSONObject(messageTokener);
            schema.validate(messageObject);
        }
        catch (final ValidationException e)
        {
            final String message = "Schema: " + schema.getTitle() + " / " + schema.getDescription();
            LOGGER.error(message);
            return message + "\n" + readValidationException(e);
        }
        catch (final JSONException e)
        {
            final String message = "Schema: " + schema.getTitle() + " / " + schema.getDescription() + " JSON parsing failed.";
            LOGGER.error(message, e);
            return message + "\n" + e.getLocalizedMessage();
        }
        // Everything fine
        return null;
    }

    public static String readValidationException(final ValidationException e)
    {
        final StringBuilder bld = new StringBuilder(e.getMessage());
        e.getCausingExceptions().stream().map(ValidationException::getMessage).forEach(err -> {
            LOGGER.error(err);
            bld.append(err);
            bld.append("\n");
        });
        return bld.toString();
    }

}

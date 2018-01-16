/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.json.utils;

import org.everit.json.schema.loader.internal.DefaultSchemaClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;

/**
 * Original class in the jar is not able to load files from /includes/ folder.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
class ResourcesSchemaClient extends DefaultSchemaClient
{
    private String includesDir = null;
    private String schemaDir = null;

    public ResourcesSchemaClient(final String includesDir, final String schemaDir)
    {
        this.includesDir = includesDir;
        this.schemaDir = schemaDir;
    }
    
    @Override
    public InputStream get(final String url)
    {
        try
        {
            return super.get(url);
        }
        catch (final UncheckedIOException eSuper)
        {
            try
            {
                final URL aUrl = new URL(url);
                return (InputStream) aUrl.getContent();
            }
            catch (final IOException eRes)
            {
                final File file = new File(url);
                final String path = includesDir + "/" + file.getName();
                InputStream stream = JsonSchemaValidation.class.getResourceAsStream(path);
                if (stream == null)
                {
                    stream = JsonSchemaValidation.class.getResourceAsStream(schemaDir+url);

                    if (stream == null)
                    {
                        throw new UncheckedIOException("Could not open " + url + " as URL or Resources", eRes);
                    }
                }
                return stream;
            }
        }
    }
}

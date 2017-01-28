/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
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
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
class ResourcesSchemaClient extends DefaultSchemaClient
{
    private String includesDir = null;

    public ResourcesSchemaClient(final String includesDir)
    {
        this.includesDir = includesDir;
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
                final InputStream stream = JsonSchemaValidation.class.getResourceAsStream(path);
                if (stream == null)
                {
                    throw new UncheckedIOException("Could not open " + url + " as URL or Resources", eRes);
                }
                return stream;
            }
        }
    }
}

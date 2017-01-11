/*
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Correlation ID builder.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class CorrelationIdBuilder
{
    /**
     * Sections separator character.
     */
    public static final String SEPARATOR_REGEX = "\\$";
    public static final String SEPARATOR = "$";

    private List<String> sections = new ArrayList<>();

    /**
     * Default constructor creates an initial random UUID as the first section.
     */
    public CorrelationIdBuilder()
    {
        sections.add(UUID.randomUUID().toString());
    }

    /**
     * Constructor with initial correlationId value.
     *
     * @param correlationId previous chain of IDs
     */
    public CorrelationIdBuilder(final String correlationId)
    {
        this.sections = new ArrayList<>(splitString(correlationId));
    }

    /**
     * Add another random UUID section to the existing CorrelationID.
     *
     * @return Returns this
     */
    public CorrelationIdBuilder addSection()
    {
        sections.add(UUID.randomUUID().toString());
        return this;
    }

    /**
     * Add custom value section to the existing CorrelationID.
     *
     * @return Returns this
     */
    public CorrelationIdBuilder addSection(final String sectionValue)
    {
        sections.add(sectionValue);
        return this;
    }

    /**
     * Add another random UUID section to the existing CorrelationID.
     *
     * @return Returns this
     */
    public String readLast()
    {
        if (sections != null && !sections.isEmpty())
        {
            return sections.get(sections.size() - 1);
        }
        else
        {
            return "";
        }
    }

    /**
     * Remove the last section.
     *
     * @return Returns this
     */
    public CorrelationIdBuilder removeLast()
    {
        if (sections != null && !sections.isEmpty())
        {
            sections.remove(sections.size() - 1);
        }
        return this;
    }

    public String build()
    {
        if (sections != null && !sections.isEmpty())
        {
            StringBuilder bld = new StringBuilder(sections.get(0));
            for (int i = 1; i < sections.size(); i++)
            {
                final String section = sections.get(i);
                bld.append(SEPARATOR);
                bld.append(section);
            }
            return bld.toString();
        }
        else
        {
            return "";
        }
    }

    @Override
    public String toString()
    {
        return build();
    }

    private List<String> splitString(final String correlationString)
    {
        if (StringUtils.isNotEmpty(correlationString))
        {
            final String[] array = correlationString.split(SEPARATOR_REGEX);
            return Arrays.asList(array);
        }
        else
        {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final CorrelationIdBuilder that = (CorrelationIdBuilder) o;
        return Objects.equals(this.build(), that.build());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.build());
    }
}

/*
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Correlation ID builder test.
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since 1.1
 */
public class CorrelationIdBuilderTest
{
    @Test
    public void addSection() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder();
        bld.addSection();
        bld.addSection();
        bld.addSection();
        assertEquals(36 * 4 + 3, bld.build().length());
    }

    @Test
    public void addSectionCustom() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder("a1");
        bld.addSection("b1");
        bld.addSection("c1");
        bld.addSection("d1");
        assertEquals("a1$b1$c1$d1", bld.build());
    }

    @Test
    public void readLast() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder("a1");
        bld.addSection("b1").addSection("c1").addSection("d1");
        assertEquals("d1", bld.readLast());
        assertEquals("c1", bld.removeLast().readLast());
        assertEquals("a1$b1$c1", bld.build());
    }

    @Test
    public void removeLast() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder("a1");
        bld.addSection("b1").addSection("c1").addSection("d1");
        assertEquals("a1", bld.removeLast().removeLast().removeLast().readLast());
        assertEquals("a1", bld.build());
    }

    @Test
    public void splitAndRemoveLast() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder("a1$b212$1667667");
        assertEquals("a1", bld.removeLast().removeLast().readLast());
        assertEquals("a1", bld.build());
    }
    @Test
    public void buildEmpty() throws Exception
    {
        CorrelationIdBuilder bld = new CorrelationIdBuilder("");
        assertEquals("", bld.build());

        bld = new CorrelationIdBuilder(null);
        assertEquals("", bld.build());
    }

    @Test
    public void equals() throws Exception
    {
        {
            CorrelationIdBuilder bld1 = new CorrelationIdBuilder("");
            CorrelationIdBuilder bld2 = new CorrelationIdBuilder("");
            assertTrue(bld1.equals(bld2));
            assertTrue(bld2.equals(bld1));
        }

        {
            CorrelationIdBuilder bld1 = new CorrelationIdBuilder("a1");
            CorrelationIdBuilder bld2 = new CorrelationIdBuilder("a1");
            assertTrue(bld1.equals(bld2));
            assertTrue(bld2.equals(bld1));
        }

        {
            CorrelationIdBuilder bld1 = new CorrelationIdBuilder("a1$b2$c3");
            CorrelationIdBuilder bld2 = new CorrelationIdBuilder("a1$b2$c3");
            assertTrue(bld1.equals(bld2));
            assertTrue(bld2.equals(bld1));
        }

        {
            CorrelationIdBuilder bld1 = new CorrelationIdBuilder("a1$b2$c3");
            CorrelationIdBuilder bld2 = new CorrelationIdBuilder("a1$b2");
            assertFalse(bld1.equals(bld2));
            assertFalse(bld2.equals(bld1));
        }
    }

}

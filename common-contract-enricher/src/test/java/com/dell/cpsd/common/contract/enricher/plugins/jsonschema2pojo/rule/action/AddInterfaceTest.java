package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class AddInterfaceTest
{
    AddInterface action;
    JDefinedClass jClass;

    @Before
    public void setUp() throws Exception
    {
        JCodeModel codeModel = new JCodeModel();
        jClass = codeModel._class("com.dell.cpsd.test.TestClass");

        action = new AddInterface(asList("field1", "field2"), "com.dell.cpsd.test.TestInterface");
    }

    @Test
    public void supports() throws Exception
    {
        assertFalse(action.supports(jClass));

        jClass.field(JMod.PRIVATE, String.class, "field1");
        assertFalse(action.supports(jClass));

        jClass.field(JMod.PRIVATE, String.class, "field2");
        assertTrue(action.supports(jClass));
    }

    @Test
    public void apply() throws Exception
    {
        action.apply(jClass);

        Iterator<JClass> interfaces = jClass._implements();
        assertTrue(interfaces.hasNext());
        assertEquals("com.dell.cpsd.test.TestInterface", interfaces.next().fullName());
    }
}
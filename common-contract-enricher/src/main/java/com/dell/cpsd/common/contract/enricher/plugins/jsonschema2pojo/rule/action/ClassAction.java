/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action;

import com.sun.codemodel.JDefinedClass;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Transforms provided class if it fits specific condition.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface ClassAction extends Function<JDefinedClass, JDefinedClass>
{
    boolean supports(JDefinedClass jClass);

}

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;

import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.DefaultPropertiesRule;
import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.DefaultPropertyRule;
import com.sun.codemodel.JDefinedClass;

/**
 * Provides factory to create default rules.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultRuleFactory extends RuleFactory
{
    @Override
    public Rule<JDefinedClass, JDefinedClass> getPropertiesRule()
    {
        return new DefaultPropertiesRule(this);
    }
    
    @Override
    public Rule<JDefinedClass, JDefinedClass> getPropertyRule()
    {
        return new DefaultPropertyRule(this);
    }
    
    /*@Override
    public Rule<JClassContainer, JType> getSchemaRule()
    {
        return new DefaultSchemaRule(this);
    }
    
    @Override
    public Rule<JClassContainer, JType> getTypeRule()
    {
        return new DefaultTypeRule(this);
    }*/
}

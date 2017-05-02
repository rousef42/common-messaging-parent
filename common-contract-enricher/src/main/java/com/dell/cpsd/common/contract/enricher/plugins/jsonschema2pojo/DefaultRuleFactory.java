/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.DefaultPropertiesRule;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;

/**
 * Provides factory to create default rules.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultRuleFactory extends RuleFactory
{
    @Override
    public Rule<JDefinedClass, JDefinedClass> getPropertiesRule()
    {
        return new DefaultPropertiesRule(this);
    }
}

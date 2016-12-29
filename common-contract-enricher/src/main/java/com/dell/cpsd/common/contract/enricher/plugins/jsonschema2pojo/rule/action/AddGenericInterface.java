/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

/**
 * Adds generic interface if class has a required property. Generic type is taken from field declaration.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class AddGenericInterface implements ClassAction
{
    private String property;
    private String interfaceName;

    public AddGenericInterface(String property, String interfaceName)
    {
        this.property = property;
        this.interfaceName = interfaceName;
    }

    @Override
    public boolean supports(JDefinedClass jClass)
    {
        return jClass.fields().containsKey(property);
    }

    @Override
    public JDefinedClass apply(JDefinedClass jClass)
    {
        JCodeModel codeModel = jClass.owner();
        JClass jInterface = codeModel.directClass(interfaceName);

        JFieldVar field = jClass.fields().get(property);
        JClass jGenericInterface = jInterface.narrow(field.type());

        return jClass._implements(jGenericInterface);
    }
}

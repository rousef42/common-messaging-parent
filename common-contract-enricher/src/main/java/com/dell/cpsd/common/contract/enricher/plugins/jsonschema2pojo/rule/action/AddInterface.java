/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JType;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Adds interface to a class if it contains all required fields.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class AddInterface implements ClassAction
{
    protected List<String> requiredProperties;
    protected String       interfaceName;

    public AddInterface(String requiredProperty, String interfaceName)
    {
        this(asList(requiredProperty), interfaceName);
    }

    public AddInterface(List<String> requiredProperties, String interfaceName)
    {
        this.requiredProperties = requiredProperties;
        this.interfaceName = interfaceName;
    }

    @Override
    public boolean supports(JDefinedClass jClass)
    {
        return jClass.fields().keySet().containsAll(requiredProperties);
    }

    @Override
    public JDefinedClass apply(JDefinedClass jClass)
    {
        //jClass.owner().
        JCodeModel codeModel = jClass.owner();
        codeModel._ref(com.dell.cpsd.contract.extension.amqp.RequestMessageProperties.class); 
        JClass jInterface = codeModel.directClass(interfaceName);
        JDefinedClass _implements = jClass._implements(jInterface);
        
        
        _implements.direct("private String TEST;");
        
        _implements.generify("com.dell.cpsd.contract.extension.amqp.ResponseMessageProperties");
        _implements.erasure();
        JFieldVar field = _implements.fields().get(requiredProperties.get(0));
        JType fieldType = field.type();
       // fieldType = unwrap(fieldType);
        
        
        fieldType.owner()._package("com.dell.cpsd.contract.extension.amqp");
        
         return _implements;
    }
}

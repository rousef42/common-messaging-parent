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
import java.util.Objects;

/**
 * Adds generic interface if class has a required property. Generic type is taken from field declaration.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p/>
 */
public class AddGenericInterface implements ClassAction
{
    private String property;
    private String interfaceName;
    private String wrapperClassName;

    public AddGenericInterface(String property, String interfaceName)
    {
        this.property = property;
        this.interfaceName = interfaceName;
    }

    /**
     * If field type is parametrized class like List<T>, takes generic parameter from that class.
     *
     * @param wrapperClass field wrapper class
     * @return itself for fluent statements building
     */
    public AddGenericInterface unwrapFieldType(Class wrapperClass)
    {
        this.wrapperClassName = wrapperClass.getName();
        return this;
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
        JType fieldType = field.type();
        fieldType = unwrap(fieldType);
        JClass jGenericInterface = jInterface.narrow(fieldType);

        return jClass._implements(jGenericInterface);
    }

    protected JType unwrap(JType type)
    {
        if (!Objects.equals(type.erasure().fullName(), wrapperClassName))
        {
            return type;
        }

        if (!(type instanceof JClass))
        {
            return type;
        }
        JClass classType = (JClass) type;

        List<JClass> parameters = classType.getTypeParameters();
        if (parameters == null || parameters.isEmpty())
        {
            return type;
        }

        return parameters.get(0);
    }
}

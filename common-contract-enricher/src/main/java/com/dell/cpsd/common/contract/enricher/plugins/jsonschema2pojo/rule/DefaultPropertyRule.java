/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule;

import static org.apache.commons.lang3.StringUtils.capitalize;

import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertyRule;
import org.jsonschema2pojo.rules.RuleFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocCommentable;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * Applies the schema rules that represent a property definition. Conditionally avoiding generation of setter methods for field
 * 'messageProperties'.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultPropertyRule extends PropertyRule
{
    private final RuleFactory   ruleFactory;

    private static final String MESSAGE_PROPERTIES = "messageProperties";

    public DefaultPropertyRule(RuleFactory ruleFactory)
    {
        super(ruleFactory);
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JDefinedClass apply(String nodeName, JsonNode node, JDefinedClass jclass, Schema schema)
    {
        String propertyName = ruleFactory.getNameHelper().getPropertyName(nodeName, node);

        JType propertyType = ruleFactory.getSchemaRule().apply(nodeName, node, jclass, schema);

        node = resolveRefs(node, schema);

        int accessModifier = ruleFactory.getGenerationConfig().isIncludeAccessors() ? JMod.PRIVATE : JMod.PUBLIC;
        JFieldVar field = jclass.field(accessModifier, propertyType, propertyName);

        propertyAnnotations(nodeName, node, schema, field);

        formatAnnotation(field, node);

        ruleFactory.getAnnotator().propertyField(field, jclass, nodeName, node);

        if (ruleFactory.getGenerationConfig().isIncludeAccessors())
        {
            JMethod getter = addGetter(jclass, field, nodeName, node);
            ruleFactory.getAnnotator().propertyGetter(getter, nodeName);
            propertyAnnotations(nodeName, node, schema, getter);

            if (!MESSAGE_PROPERTIES.equals(propertyName.trim()))
            {
                JMethod setter = addSetter(jclass, field, nodeName, node);
                ruleFactory.getAnnotator().propertySetter(setter, nodeName);
                propertyAnnotations(nodeName, node, schema, setter);
            }
        }

        if (ruleFactory.getGenerationConfig().isGenerateBuilders())
        {
            addBuilder(jclass, field);
        }

        if (node.has("pattern"))
        {
            ruleFactory.getPatternRule().apply(nodeName, node.get("pattern"), field, schema);
        }

        ruleFactory.getDefaultRule().apply(nodeName, node.get("default"), field, schema);

        ruleFactory.getMinimumMaximumRule().apply(nodeName, node, field, schema);

        ruleFactory.getMinItemsMaxItemsRule().apply(nodeName, node, field, schema);

        ruleFactory.getMinLengthMaxLengthRule().apply(nodeName, node, field, schema);

        if (isObject(node) || isArray(node))
        {
            ruleFactory.getValidRule().apply(nodeName, node, field, schema);
        }

        return jclass;
    }

    private JsonNode resolveRefs(JsonNode node, Schema parent)
    {
        if (node.has("$ref"))
        {
            Schema refSchema = ruleFactory.getSchemaStore().create(parent, node.get("$ref").asText(),
                    ruleFactory.getGenerationConfig().getRefFragmentPathDelimiters());
            JsonNode refNode = refSchema.getContent();
            return resolveRefs(refNode, parent);
        }
        else
        {
            return node;
        }
    }

    private void propertyAnnotations(String nodeName, JsonNode node, Schema schema, JDocCommentable generatedJavaConstruct)
    {
        if (node.has("title"))
        {
            ruleFactory.getTitleRule().apply(nodeName, node.get("title"), generatedJavaConstruct, schema);
        }

        if (node.has("javaName"))
        {
            ruleFactory.getJavaNameRule().apply(nodeName, node.get("javaName"), generatedJavaConstruct, schema);
        }

        if (node.has("description"))
        {
            ruleFactory.getDescriptionRule().apply(nodeName, node.get("description"), generatedJavaConstruct, schema);
        }

        if (node.has("required"))
        {
            ruleFactory.getRequiredRule().apply(nodeName, node.get("required"), generatedJavaConstruct, schema);
        }
        else
        {
            ruleFactory.getNotRequiredRule().apply(nodeName, node.get("required"), generatedJavaConstruct, schema);
        }
    }

    private void formatAnnotation(JFieldVar field, JsonNode node)
    {
        String format = node.path("format").asText();
        if ("date-time".equalsIgnoreCase(format))
        {
            ruleFactory.getAnnotator().dateTimeField(field, node);
        }
        else if ("date".equalsIgnoreCase(format))
        {
            ruleFactory.getAnnotator().dateField(field, node);
        }
    }

    private JMethod addGetter(JDefinedClass c, JFieldVar field, String jsonPropertyName, JsonNode node)
    {
        JMethod getter = c.method(JMod.PUBLIC, field.type(), getGetterName(jsonPropertyName, field.type(), node));
        JBlock body = getter.body();
        body._return(field);
        return getter;
    }

    private String getGetterName(String propertyName, JType type, JsonNode node)
    {
        return ruleFactory.getNameHelper().getGetterName(propertyName, type,
                (null != propertyName && MESSAGE_PROPERTIES.equals(propertyName.trim()) ? null : node));
    }

    private JMethod addSetter(JDefinedClass c, JFieldVar field, String jsonPropertyName, JsonNode node)
    {
        JMethod setter = c.method(JMod.PUBLIC, void.class, getSetterName(jsonPropertyName, node));
        JVar param = setter.param(field.type(), field.name());
        JBlock body = setter.body();
        body.assign(JExpr._this().ref(field), param);
        return setter;
    }

    private String getSetterName(String propertyName, JsonNode node)
    {
        return ruleFactory.getNameHelper().getSetterName(propertyName, node);
    }

    private JMethod addBuilder(JDefinedClass c, JFieldVar field)
    {
        JMethod builder = c.method(JMod.PUBLIC, c, getBuilderName(field.name()));
        JVar param = builder.param(field.type(), field.name());
        JBlock body = builder.body();
        body.assign(JExpr._this().ref(field), param);
        body._return(JExpr._this());
        return builder;
    }

    private boolean isObject(JsonNode node)
    {
        return node.path("type").asText().equals("object");
    }

    private boolean isArray(JsonNode node)
    {
        return node.path("type").asText().equals("array");
    }

    private String getBuilderName(String propertyName)
    {
        propertyName = ruleFactory.getNameHelper().replaceIllegalCharacters(propertyName);
        return "with" + capitalize(ruleFactory.getNameHelper().capitalizeTrailingWords(propertyName));
    }
}

/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration;

import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageExchangeDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.springframework.amqp.support.converter.DefaultClassMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class RegistrationAwareClassMapper extends DefaultClassMapper implements MessageRegistrationAware
{
    private String serviceName;
    private boolean autoRegister;

    private Map<String, MessageRegistrationDto> classRegistrations = new HashMap<>();

    public RegistrationAwareClassMapper(String serviceName, boolean autoRegister)
    {
        this.serviceName = serviceName;
        this.autoRegister = autoRegister;
    }

    public void addClassMapping(Class<?> expectedClass, MessageExchangeDto... expectedExchanges)
    {
        final MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();

        messageAnnotationProcessor.process((messageType, messageClass) ->
        {
            JsonSchema schema = createSchema(messageClass);
            classRegistrations
                    .put(messageType, new MessageRegistrationDto(serviceName, messageClass, messageType, "1.0", schema,
                            Arrays.asList(expectedExchanges)));
        }, expectedClass);
    }

    public void initialiseMapper()
    {
        final Map<String, Class<?>> classMappings = new HashMap<>();

        classRegistrations.forEach((k, v) ->
        {
            classMappings.put(k, v.getMessageClass());
        });

        super.setIdClassMapping(classMappings);
    }

    @Override
    public Collection<MessageRegistrationDto> getRegistrations()
    {
        return classRegistrations.values();
    }

    @Override
    public boolean isAutoRegister()
    {
        return autoRegister;
    }

    private JsonSchema createSchema(Class<?> clazz)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
            return mapper.generateJsonSchema(clazz);
        }
        catch (JsonMappingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}

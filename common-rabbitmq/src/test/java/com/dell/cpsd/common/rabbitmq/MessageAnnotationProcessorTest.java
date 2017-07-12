/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.dell.cpsd.common.rabbitmq.annotation.Message;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since Vision x.y.z
 */
public class MessageAnnotationProcessorTest
{
    MessageAnnotationProcessorCallback callback;
    MessageAnnotationProcessor         processor;

    @Before
    public void setUp() throws Exception
    {
        callback = Mockito.mock(MessageAnnotationProcessorCallback.class);
        processor = new MessageAnnotationProcessor();
    }

    @Test
    public void testProcessMessageAnnotationProcessorCallbackListOfClassOfQ()
    {
        Class<TestReplyMessage> clazz1 = TestReplyMessage.class;
        Annotation annotation1 = clazz1.getAnnotation(Message.class);
        String value1 = ((Message) annotation1).value();

        Class<TestRequestMessage> clazz2 = TestRequestMessage.class;
        Annotation annotation2 = clazz2.getAnnotation(Message.class);
        String value2 = ((Message) annotation2).value();

        Mockito.doNothing().when(callback).found(value1, clazz1);
        Mockito.doNothing().when(callback).found(value2, clazz2);

        processor.process(callback, Arrays.asList(clazz1, clazz2));

        Mockito.verify(callback).found(value1, clazz1);
        Mockito.verify(callback).found(value2, clazz2);
    }

    @Test
    public void testProcessMessageAnnotationProcessorCallbackClassOfQ()
    {
        Class<TestReplyMessage> clazz = TestReplyMessage.class;
        Annotation annotation = clazz.getAnnotation(Message.class);
        String value = ((Message) annotation).value();
        Mockito.doNothing().when(callback).found(value, clazz);

        processor.process(callback, TestReplyMessage.class);

        Mockito.verify(callback).found(value, clazz);
    }

    @Test
    public void testProcessMessageNoAnnotation()
    {
        Class<Object> clazz = Object.class;

        processor.process(callback, clazz);

        Mockito.verify(callback, Mockito.never()).found(Mockito.anyString(), Mockito.any());
    }

}

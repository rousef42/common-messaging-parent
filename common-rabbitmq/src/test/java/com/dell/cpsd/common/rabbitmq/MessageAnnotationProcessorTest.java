/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.dell.cpsd.annotation.message.test.data.TestMessageForAutoAnnotationScan;
import com.dell.cpsd.contract.extension.amqp.annotation.Message;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
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
    public void testScanAndProcessClassMappings()
    {

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

    @Test
    public void testScanAndProcessAnnotation()
    {
        processor.scanAndProcessAnnotation(new MessageAnnotationProcessorCallback()
        {

            @Override
            public void found(String messageType, Class messageClass)
            {
                assertEquals("test.message.request", messageType);
                assertEquals(TestMessageForAutoAnnotationScan.class, messageClass);
            }
        }, "com.dell.cpsd.annotation.message.test.data");
    }

    @Test
    public void testScanAndProcessAnnotationNoMatches()
    {
        processor.scanAndProcessAnnotation(callback, "com.dell.cpsd.annotation.message.test.data.no.package");
        Mockito.verify(callback, Mockito.never()).found(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testScanAndProcessAnnotationDefaultPackage()
    {
        processor.scanAndProcessAnnotation(callback);
        Mockito.verify(callback, Mockito.atLeastOnce()).found(Mockito.anyString(), Mockito.any());
    }

}

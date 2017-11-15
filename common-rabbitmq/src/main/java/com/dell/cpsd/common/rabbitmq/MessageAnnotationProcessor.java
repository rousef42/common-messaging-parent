/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.dell.cpsd.contract.extension.amqp.annotation.Message;

/**
 * An annotation process for evaluating @Message annotations.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 * 
 * @version 1.0
 * @since SINCE-TDB
 */
public class MessageAnnotationProcessor
{
    private static final String DEFAULT_PACKAGE_TO_SCAN = "com.dell.cpsd";
    private static final Logger LOG                     = LoggerFactory.getLogger(MessageAnnotationProcessor.class);

    /**
     * Called to process the list of classes.
     *
     */
    public void process(final MessageAnnotationProcessorCallback callback, final List<Class<?>> classes)
    {
        for (final Class aClass : classes)
        {
            process(callback, aClass);
        }
    }

    /**
     * This operation will scan the classpath for all classes containing {@link Message} annotation. Only packages under 'com.dell.cpsd'
     * will be scanned for annotated classes
     * 
     * @param callback
     *            - {@link MessageAnnotationProcessorCallback} - callback function to be executed. Non null.
     */
    public void scanAndProcessAnnotation(final MessageAnnotationProcessorCallback callback)
    {
        scanAndProcessAnnotation(callback, DEFAULT_PACKAGE_TO_SCAN);
    }

    /**
     * This operation will scan the classpath for all classes containing {@link Message} annotation. The package to scanned can be provided.
     * 
     * @param callback
     *            - - {@link MessageAnnotationProcessorCallback} - callback function to be executed. Non null.
     * @param packageToBeScanned
     *            - String. All classes under this package will be scanned for the annotation
     */
    public void scanAndProcessAnnotation(final MessageAnnotationProcessorCallback callback, String packageToBeScanned)
    {
        // Scan classpath for classes with @Message Annotation
        ClassPathScanningCandidateComponentProvider classPathScanner = new ClassPathScanningCandidateComponentProvider(false);
        classPathScanner.addIncludeFilter(new AnnotationTypeFilter(Message.class));

        Set<BeanDefinition> beanDefinitions = classPathScanner.findCandidateComponents(packageToBeScanned);

        beanDefinitions.forEach(beanDefinition -> {
            String beanClassName = beanDefinition.getBeanClassName();
            try
            {
                process(callback, Class.forName(beanClassName));
            }
            catch (ClassNotFoundException classNotFoundException)
            {
                // This exception will never be thrown. We are initially scanning the classloaders and getting the names of classes with the
                // annotation and using the same class we found in Class.forName() method. Hence, we will never get class not found
                // exception. Hence, cannot be tested.
                LOG.error("Class of name" + beanClassName + " with @Message annotation could not be loaded", classNotFoundException);
            }
        });
    }

    /**
     * Calls the callback if the class contains the Message annotation.
     *
     */
    public void process(final MessageAnnotationProcessorCallback callback, final Class<?> aClass)
    {
        Annotation annotation = aClass.getAnnotation(Message.class);

        if (annotation != null)
        {
            Message messageAnnotation = (Message) annotation;
            callback.found(messageAnnotation.value(), aClass); // @TODO, update callback to include version ...
                                                               // messageAnnotation.version());
        }
    }
}

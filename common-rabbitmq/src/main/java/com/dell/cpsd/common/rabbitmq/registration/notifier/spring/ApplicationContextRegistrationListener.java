/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.spring;

import com.dell.cpsd.common.rabbitmq.registration.MessageRegistrationAware;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.BindingDataDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageExchangeDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.service.RegistrationNotifierService;
import org.springframework.amqp.core.Binding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Component
public class ApplicationContextRegistrationListener implements ApplicationListener<ApplicationContextEvent>
{
    private List<MessageRegistrationDto> registrations = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationContextEvent event)
    {
        if (event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent || event instanceof ContextRefreshedEvent)
        {
            ApplicationContext context = event.getApplicationContext();

            RegistrationNotifierService registrationNotifierService = context.getBean(RegistrationNotifierService.class);
            if (registrationNotifierService == null)
            {
                return;
            }

            if (event instanceof ContextRefreshedEvent)
            {
                Map<String, MessageRegistrationAware> registrations = context.getBeansOfType(MessageRegistrationAware.class);
                Map<String, Binding> bindings = context.getBeansOfType(Binding.class);

                for (Map.Entry<String, MessageRegistrationAware> registration : registrations.entrySet())
                {
                    MessageRegistrationAware messageRegistration = registration.getValue();
                    if (messageRegistration.isAutoRegister())
                    {
                        notify(registrationNotifierService, registration.getValue(), bindings.values());
                    }
                }
            }
            else
            {
                withdraw(registrationNotifierService);
            }
        }
    }

    private void notify(RegistrationNotifierService registrationNotifierService, MessageRegistrationAware messageRegistrationAware,
            Collection<Binding> bindings)
    {

        for (MessageRegistrationDto entry : messageRegistrationAware.getRegistrations())
        {
            List<MessageExchangeDto> exchanges =
                    entry.getMessageExchanges().stream()
                            .map(new MergeBindings(bindings)).collect(Collectors.toList());

            MessageRegistrationDto enrichedEntry = new MessageRegistrationDto(entry.getServiceName(), entry.getMessageClass(),
                    entry.getMessageType(), entry.getMessageVersion(), entry.getMessageSchema(), exchanges);

            registrations.add(enrichedEntry);
            registrationNotifierService.notify(enrichedEntry);
        }
    }

    private class MergeBindings implements Function<MessageExchangeDto, MessageExchangeDto>
    {
        private Collection<Binding> bindings;

        public MergeBindings(Collection<Binding> bindings)
        {
            this.bindings = bindings;
        }

        @Override
        public MessageExchangeDto apply(MessageExchangeDto exchange)
        {
            List<BindingDataDto> bindingDataDtos = new ArrayList<>();

            if (exchange.getBindings() != null)
            {
                bindingDataDtos.addAll(exchange.getBindings());
            }

            if (bindings != null)
            {
                bindingDataDtos.addAll(bindings.stream()
                        .filter(binding -> binding.getExchange().equals(exchange.getName()))
                        .map(this::transformBinding).collect(Collectors.toList()));
            }

            return new MessageExchangeDto(exchange.getName(), exchange.getDirection(), bindingDataDtos.toArray(new BindingDataDto[]{}));
        };

        private BindingDataDto transformBinding(Binding binding)
        {
            return new BindingDataDto(binding.getDestination(), binding.getRoutingKey());
        }
    }

    private void withdraw(RegistrationNotifierService registrationNotifierService)
    {
        for (MessageRegistrationDto messageRegistrationDto : registrations)
        {
            registrationNotifierService.withdraw(messageRegistrationDto);
        }
    }
}
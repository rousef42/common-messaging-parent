/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.spring;

import com.dell.cpsd.common.rabbitmq.registration.MessageRegistrationAware;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.BindingDataDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageDirectionType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
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
                Map<String, MessageRegistrationAware> msgRegistrations = context.getBeansOfType(MessageRegistrationAware.class);
                Map<String, Binding> bindings = context.getBeansOfType(Binding.class);

                for (Map.Entry<String, MessageRegistrationAware> registration : msgRegistrations.entrySet())
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
            Map<String, MessageExchangeDto> exchangeMap = new HashMap<>();
            Map<String, List<BindingDataDto>> exchangeToBindingMap = new HashMap<>();

            if (entry.getMessageExchanges() != null)
            {
                entry.getMessageExchanges().forEach(m ->
                {
                    exchangeMap.put(m.getName(), m);
                    m.getBindings().forEach(b ->
                    {
                        List<BindingDataDto> exchangeBinding = exchangeToBindingMap.get(m.getName());
                        if (exchangeBinding == null)
                        {
                            exchangeBinding = new ArrayList<>();
                            exchangeToBindingMap.put(m.getName(), exchangeBinding);
                        }
                        exchangeBinding.add(b);
                    });
                });
            }

            if (entry.getMessageQueues() != null)
            {
                entry.getMessageQueues().forEach(q ->
                {
                    bindings.stream().filter(b -> q.getName().equals(b.getDestination())).forEach(b ->
                    {
                        MessageExchangeDto exchange = exchangeMap.get(b.getExchange());
                        if (exchange == null)
                        {
                            exchange = new MessageExchangeDto(b.getExchange(), MessageDirectionType.CONSUME);
                            exchangeMap.put(b.getExchange(), exchange);
                        }
                        List<BindingDataDto> exchangeBinding = exchangeToBindingMap.get(b.getExchange());
                        if (exchangeBinding == null)
                        {
                            exchangeBinding = new ArrayList<>();
                            exchangeToBindingMap.put(b.getExchange(), exchangeBinding);
                        }
                        exchangeBinding.add(new BindingDataDto(b.getDestination(), b.getRoutingKey()));
                    });
                });
            }

            List<MessageExchangeDto> messageExchanges = new ArrayList<>();
            for (Map.Entry<String, MessageExchangeDto> exchangeEntry : exchangeMap.entrySet())
            {
                MessageExchangeDto exchangeDto = exchangeEntry.getValue();
                List<BindingDataDto> bindingDataDtos = exchangeToBindingMap.get(exchangeEntry.getKey());
                messageExchanges.add(new MessageExchangeDto(exchangeDto.getName(), exchangeDto.getDirection(), bindingDataDtos));
            }

            MessageRegistrationDto enrichedEntry = new MessageRegistrationDto(entry.getServiceName(), entry.getMessageClass(),
                    entry.getMessageType(), entry.getMessageVersion(), entry.getMessageSchema(), messageExchanges,
                    entry.getMessageQueues());

            registrations.add(enrichedEntry);
            registrationNotifierService.notify(enrichedEntry);
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
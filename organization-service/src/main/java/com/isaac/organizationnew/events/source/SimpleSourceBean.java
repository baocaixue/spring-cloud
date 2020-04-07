package com.isaac.organizationnew.events.source;

import com.isaac.organizationnew.events.models.OrganizationChangeModel;
import com.isaac.organizationnew.utils.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SimpleSourceBean {
    private Source source;

    public void publishOrgChange(String action, String orgId) {
        log.debug("Sending RabbitMq message {} for Organization Id: {}", action, orgId);
        var change = new OrganizationChangeModel()
                .setType(OrganizationChangeModel.class.getTypeName())
                .setAction(action)
                .setOrganizationId(orgId)
                .setCorrelationId(UserContextHolder.getContext().getCorrelationId());
        source.output().send(MessageBuilder.withPayload(change).build());
    }
}

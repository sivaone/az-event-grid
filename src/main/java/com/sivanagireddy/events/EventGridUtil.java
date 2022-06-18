package com.sivanagireddy.events;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;

import java.util.Objects;

public class EventGridUtil {

    private static final String AZ_EG_ENDPOINT = "";
    private static final String AZ_EG_KEY = "";
    private EventGridPublisherClient<EventGridEvent> eventGridClient;

    public void initClient() {
        eventGridClient = new EventGridPublisherClientBuilder()
                .endpoint(AZ_EG_ENDPOINT)
                .credential(new AzureKeyCredential(AZ_EG_KEY))
                .buildEventGridEventPublisherClient();
    }

    public EventGridPublisherClient<EventGridEvent> getEventGridClient(){
        if (Objects.isNull(eventGridClient)) {
            initClient();
        }
        return eventGridClient;
    }
}

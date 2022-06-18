package com.sivanagireddy.events;

import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EventSender {

    private static final EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .seed(12345L)
            .objectPoolSize(100)
            .randomizationDepth(1)
            .charset(StandardCharsets.UTF_8)
            .stringLengthRange(5, 10)
            .build();

    public void sendEvents() {
        // Make sure that the event grid topic or domain you're sending to is able to accept the EventGridEvent schema.
        List<EventGridEvent> events = new ArrayList<>();
        random.objects(User.class, 50).forEach(user ->
                events.add(
                        new EventGridEvent(
                                "users",
                                "com.sivanagireddy.events.User",
                                BinaryData.fromObject(user),
                                "0.1")));
        EventGridUtil eventGridUtil = new EventGridUtil();
        System.out.println("sending events ...");
        eventGridUtil.getEventGridClient().sendEvents(events);
        System.out.println("sending events completed ....");
    }
}


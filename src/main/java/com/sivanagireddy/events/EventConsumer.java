package com.sivanagireddy.events;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.messaging.eventhubs.models.PartitionContext;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.IOException;
import java.util.function.Consumer;

public class EventConsumer {

    private static final String EH_CONNECTION_STRING = "";
    private static final String BLOB_CONN_STRING = "";
    private static final String CONTAINER_NAME = "";

    public static final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
        PartitionContext partitionContext = eventContext.getPartitionContext();
        EventData eventData = eventContext.getEventData();

        System.out.printf("Processing event from partition %s with sequence number %d with body: %s%n",
                partitionContext.getPartitionId(), eventData.getSequenceNumber(), eventData.getBodyAsString());

        // Every 10 events received, it will update the checkpoint stored in Azure Blob Storage.
        if (eventData.getSequenceNumber() % 10 == 0) {
            System.out.println("Updating checkpoint...");
            eventContext.updateCheckpoint();
        }
    };

    public static final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
        System.out.printf("Error occurred in partition processor for partition %s, %s.%n",
                errorContext.getPartitionContext().getPartitionId(),
                errorContext.getThrowable());
    };


    public void consumeEvents() {

        System.out.println("Creating blob container client");
        BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
                .connectionString(BLOB_CONN_STRING)
                .containerName(CONTAINER_NAME)
                .buildAsyncClient();

        System.out.println("Creating event processor client");
        EventProcessorClientBuilder eventProcessorClientBuilder =  new EventProcessorClientBuilder()
                .connectionString(EH_CONNECTION_STRING)
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .processEvent(PARTITION_PROCESSOR)
                .processError(ERROR_HANDLER)
                .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient));

        EventProcessorClient eventProcessorClient = eventProcessorClientBuilder.buildEventProcessorClient();
        System.out.println("event processor id: " + eventProcessorClient.getIdentifier());
        System.out.println("Starting event processor");
        eventProcessorClient.start();

        System.out.println("Is running : " + eventProcessorClient.isRunning());
        System.out.println("Press enter to stop.");
        try {
            System.in.read();
        } catch (IOException e) {
            // NOOP
        }

        System.out.println("Stopping event processor");
        eventProcessorClient.stop();
        System.out.println("Event processor stopped.");

        System.out.println("Exiting process");
    }
}

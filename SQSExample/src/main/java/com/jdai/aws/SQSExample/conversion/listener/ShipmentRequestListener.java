package com.jdai.aws.SQSExample.conversion.listener;


import com.jdai.aws.SQSExample.conversion.model.event.DomesticShipmentRequestedEvent;
import com.jdai.aws.SQSExample.conversion.model.event.InternationalShipmentRequestedEvent;
import com.jdai.aws.SQSExample.conversion.model.event.ShipmentRequestedEvent;
import com.jdai.aws.SQSExample.conversion.service.ShipmentService;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class ShipmentRequestListener {

    private final ShipmentService shippingService;

    public ShipmentRequestListener(ShipmentService shippingService) {
        this.shippingService = shippingService;
    }

    @SqsListener("${events.queues.shipping.simple-pojo-conversion-queue}")
    public void receiveShipmentRequest(ShipmentRequestedEvent shipmentRequestedEvent) {
        shippingService.processShippingRequest(shipmentRequestedEvent.toDomain());
    }

    @SqsListener("${events.queues.shipping.custom-object-mapper-queue}")
    public void receiveShipmentRequestWithCustomObjectMapper(ShipmentRequestedEvent shipmentRequestedEvent) {
        shippingService.processShippingRequest(shipmentRequestedEvent.toDomain());
    }

    @SqsListener(queueNames = "${events.queues.shipping.subclass-deserialization-queue}")
    public void receiveShippingRequestWithType(ShipmentRequestedEvent shipmentRequestedEvent) {
        if (shipmentRequestedEvent instanceof InternationalShipmentRequestedEvent event) {
            shippingService.processInternationalShipping(event.toDomain());
        } else if (shipmentRequestedEvent instanceof DomesticShipmentRequestedEvent event) {
            shippingService.processDomesticShipping(event.toDomain());
        } else {
            throw new RuntimeException("Event type not supported " + shipmentRequestedEvent.getClass()
                    .getSimpleName());
        }
    }

}

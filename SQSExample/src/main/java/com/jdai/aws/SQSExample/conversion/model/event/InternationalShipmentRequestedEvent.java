package com.jdai.aws.SQSExample.conversion.model.event;

import com.jdai.aws.SQSExample.conversion.model.entity.InternationalShipment;
import com.jdai.aws.SQSExample.conversion.model.entity.ShipmentStatus;

import java.time.LocalDate;
import java.util.UUID;


public class InternationalShipmentRequestedEvent extends ShipmentRequestedEvent {

    private String destinationCountry;
    private String customsInfo;

    public InternationalShipmentRequestedEvent(){}

    public InternationalShipmentRequestedEvent(UUID orderId, String customerAddress, LocalDate shipBy, String destinationCountry,
                                               String customsInfo) {
        super(orderId, customerAddress, shipBy);
        this.destinationCountry = destinationCountry;
        this.customsInfo = customsInfo;
    }

    public InternationalShipment toDomain() {
        return new InternationalShipment(getOrderId(), getCustomerAddress(), getShipBy(), ShipmentStatus.REQUESTED, destinationCountry,
                customsInfo);
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getCustomsInfo() {
        return customsInfo;
    }

    public void setCustomsInfo(String customsInfo) {
        this.customsInfo = customsInfo;
    }

}
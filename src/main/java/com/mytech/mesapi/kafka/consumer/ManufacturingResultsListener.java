package com.mytech.mesapi.kafka.consumer;

import com.mytech.mesapi.kafka.dto.ManufacturingResultEvent;
import com.mytech.mesapi.service.ManufacturingResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManufacturingResultsListener {

    private final ManufacturingResultService manufacturingResultService;

    @KafkaListener(
            topics = "${app.kafka.topics.manufacturing-results}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void onManufacturingResult(ManufacturingResultEvent event) {
        manufacturingResultService.handle(event);
    }
}

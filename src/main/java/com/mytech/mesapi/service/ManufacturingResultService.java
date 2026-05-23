package com.mytech.mesapi.service;

import com.mytech.mesapi.kafka.dto.ManufacturingResultEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ManufacturingResultService {

    public void handle(ManufacturingResultEvent event) {
        if (event == null) {
            log.warn("Received null manufacturing result event, skipping");
            return;
        }

        log.info(
                "Processing manufacturing result: serialNumber={}, moId={}, stationId={}, overallResult={}",
                event.getSerialNumber(),
                event.getMoId(),
                event.getStationId(),
                event.getOverallResult());

        // TODO: persist to manufacturing_result when JPA layer is enabled
    }
}

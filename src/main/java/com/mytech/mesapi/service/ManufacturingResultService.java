package com.mytech.mesapi.service;

import com.mytech.mesapi.entity.ManufacturingResult;
import com.mytech.mesapi.kafka.dto.ManufacturingResultEvent;
import com.mytech.mesapi.repository.ManufacturingResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManufacturingResultService {

    private final ManufacturingResultRepository manufacturingResultRepository;

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

        ManufacturingResult saved = manufacturingResultRepository.save(ManufacturingResult.fromEvent(event));
        log.info("Saved manufacturing result id={}", saved.getId());
    }
}

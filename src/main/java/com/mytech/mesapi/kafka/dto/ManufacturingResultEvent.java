package com.mytech.mesapi.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Manufacturing result payload aligned with {@code manufacturing_result} table columns.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ManufacturingResultEvent {

    private Long id;

    private String serialNumber;
    private String productType;
    private Long moId;

    private Integer stationId;
    private Integer controllerId;
    private Integer fixtureId;
    private Integer nestNumber;

    private OverallResult overallResult;
    private BigDecimal cycleTimeSeconds;

    private String swVersion;
    private String hwRevision;

    private Map<String, Object> testDataJson;
    private String errorCode;

    private String operatorId;
    private String shiftCode;

    private Instant createdAt;
}

package com.mytech.mesapi.entity;

import com.mytech.mesapi.kafka.dto.ManufacturingResultEvent;
import com.mytech.mesapi.kafka.dto.OverallResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "manufacturing_result")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @Column(name = "product_type", length = 50)
    private String productType;

    @Column(name = "mo_id", nullable = false)
    private Long moId;

    @Column(name = "station_id")
    private Integer stationId;

    @Column(name = "controller_id", nullable = false)
    private Integer controllerId;

    @Column(name = "fixture_id", nullable = false)
    private Integer fixtureId;

    @Column(name = "nest_number")
    private Integer nestNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_result", nullable = false)
    private OverallResult overallResult;

    @Column(name = "cycle_time_seconds", precision = 10, scale = 2)
    private BigDecimal cycleTimeSeconds;

    @Column(name = "sw_version", length = 50)
    private String swVersion;

    @Column(name = "hw_revision", length = 50)
    private String hwRevision;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "test_data_json", columnDefinition = "json")
    private Map<String, Object> testDataJson;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "operator_id", length = 50)
    private String operatorId;

    @Column(name = "shift_code", length = 20)
    private String shiftCode;

    @Column(name = "created_at")
    private Instant createdAt;

    public static ManufacturingResult fromEvent(ManufacturingResultEvent event) {
        return ManufacturingResult.builder()
                .serialNumber(event.getSerialNumber())
                .productType(event.getProductType())
                .moId(event.getMoId())
                .stationId(event.getStationId())
                .controllerId(event.getControllerId())
                .fixtureId(event.getFixtureId())
                .nestNumber(event.getNestNumber())
                .overallResult(event.getOverallResult())
                .cycleTimeSeconds(event.getCycleTimeSeconds())
                .swVersion(event.getSwVersion())
                .hwRevision(event.getHwRevision())
                .testDataJson(event.getTestDataJson())
                .errorCode(event.getErrorCode())
                .operatorId(event.getOperatorId())
                .shiftCode(event.getShiftCode())
                .createdAt(event.getCreatedAt())
                .build();
    }
}

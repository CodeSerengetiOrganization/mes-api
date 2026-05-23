# Manufacturing result Kafka message example

Example messages for topic `manufacturing-results-topic`, aligned with the `manufacturing_result` table (`mes-db/baseline/01_schema.sql`) and `ManufacturingResultEvent` in mes-api.

**Partitioning:** use `serial_number` as the **message key** so all events for the same unit go to the same partition and are consumed in order by one consumer in the group. The manufacturing result JSON is the **payload** (value).

---

## Message layout

| Part | Content |
|------|---------|
| **Key** | `serial_number` (plain string, e.g. `SN-TEST-20260519-0001`) |
| **Value** | JSON payload with snake_case fields matching the table / DTO |

---

## Example 1 — PASS (EOL, with `test_data_json`)

**Key:**

```
SN-TEST-20260519-0001
```

**Value:**

```json
{
  "serial_number": "SN-TEST-20260519-0001",
  "product_type": "SCU",
  "mo_id": 1,
  "station_id": null,
  "controller_id": 10,
  "fixture_id": 11,
  "nest_number": 1,
  "overall_result": "PASS",
  "cycle_time_seconds": 42.50,
  "sw_version": "1.2.3",
  "hw_revision": "Rev-A",
  "test_data_json": {
    "voltage_v": 12.0,
    "current_ma": 150
  },
  "error_code": null,
  "operator_id": "OP-TEST",
  "shift_code": "A",
  "created_at": "2026-05-19T14:30:00Z"
}
```

Replace `mo_id`, `controller_id`, and `fixture_id` with real IDs from your database (see `mes-db/baseline/03_test_data.sql`).

---

## Example 2 — FAIL (with `error_code`)

**Key:**

```
SN-TEST-20260519-0002
```

**Value:**

```json
{
  "serial_number": "SN-TEST-20260519-0002",
  "product_type": "SCU",
  "mo_id": 1,
  "station_id": 201,
  "controller_id": 10,
  "fixture_id": 11,
  "nest_number": 2,
  "overall_result": "FAIL",
  "cycle_time_seconds": 38.20,
  "sw_version": null,
  "hw_revision": null,
  "test_data_json": {
    "Power_Voltage": 11.1,
    "Static_Current": 0.005
  },
  "error_code": "FUNCTION_FAIL",
  "operator_id": "OP02",
  "shift_code": "B",
  "created_at": "2026-05-19T14:35:00Z"
}
```

---

## Send with `kafka-console-producer` (key + value)

Use `parse.key=true` and `key<TAB>value` (tab between key and JSON):

```bash
kubectl exec -i -n machine-monitoring kafka-kafka-broker-0 -c kafka -- \
  bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic manufacturing-results-topic \
  --property parse.key=true <<'EOF'
SN-TEST-20260519-0001	{"serial_number":"SN-TEST-20260519-0001","product_type":"SCU","mo_id":1,"station_id":null,"controller_id":10,"fixture_id":11,"nest_number":1,"overall_result":"PASS","cycle_time_seconds":42.50,"sw_version":"1.2.3","hw_revision":"Rev-A","test_data_json":{"voltage_v":12.0,"current_ma":150},"error_code":null,"operator_id":"OP-TEST","shift_code":"A","created_at":"2026-05-19T14:30:00Z"}
EOF
```

**Verify** (print key and value):

```bash
kubectl exec -it -n machine-monitoring kafka-kafka-broker-0 -c kafka -- \
  bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic manufacturing-results-topic \
  --from-beginning \
  --property print.key=true \
  --property key.separator=' | '
```

---

## Partition / consumer behavior

- Kafka hashes the **key** (`serial_number`) to choose a **partition**.
- All messages with the same key go to the **same partition** → one consumer in the group processes them **in order** for that serial.
- Different serials can land on different partitions and be processed in parallel.

mes-api uses consumer group `mes-api` (see `application.properties`). Default partition assignment is sufficient as long as producers set `serial_number` as the key.

---

## Field notes vs schema

| Field | In Kafka payload? | Notes |
|-------|-------------------|--------|
| `id` | Omit | `AUTO_INCREMENT` on insert |
| `serial_number` | Yes (value + **key**) | Key and value should match |
| `mo_id`, `controller_id`, `fixture_id` | Yes | `NOT NULL` in DB |
| `station_id` | Yes, nullable | Nullable for EOL controller+fixture stacks |
| `overall_result` | Yes | `PASS`, `FAIL`, or `ABORTED` |
| `created_at` | Optional | DB defaults to `CURRENT_TIMESTAMP` if omitted |

---

## Spring consumer note

`ManufacturingResultsListener` currently deserializes only the **value** into `ManufacturingResultEvent`. To use the key explicitly:

```java
@KafkaListener(...)
public void onManufacturingResult(
        @Header(KafkaHeaders.RECEIVED_KEY) String serialNumberKey,
        ManufacturingResultEvent event) {
    // optional: assert serialNumberKey.equals(event.getSerialNumber())
    manufacturingResultService.handle(event);
}
```

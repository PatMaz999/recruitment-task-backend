# Recruitment task

 Backend of a web application which presents clean energy percentage in UK energy mix and calculates optimal
 electric vehicle charging hours based on peak renewable energy availability.

 

---

## Tech stack

- Language: Java 25
- Framework: Java Spring Boot 4.0
- Build: Gradle
- Architecture: Hexagonal

---

## Architecture: Hexagonal

The project follows the Hexagonal Architecture which make it highly scalable. 

### Domain Layer
*Contains pure business logic. No dependencies on external APIs.*
* **Model:** `EnergyMixRange`, `EnergyMixTimestamp`, `EnergySource` (record)
* **Service:** `EnergyMixService`

### Application Ports
*Interfaces defining contracts for communication between the Domain and Infrastructure.*
* **Port:** `CarbonPort` (Interface)

### Infrastructure Layer
*Technical implementations of ports and system entry points.*
* **Adapter:** `CarbonAdapter` (Implementation of `CarbonPort`)
* **API Gateway:** `CarbonIntensityApiGateway` (External API communication)
* **Controller:** `CarbonController` (REST Endpoints)
* **Mapper:** `CarbonMapper` (DTO ↔ Domain conversion)
* **Data Transfer:** `CarbonDto`, `EnergyTimestampDto`, `OptimalChargingDto`
* **Configuration:** `CorsConfig`, `RestClientConfiguration`, `DateTimeConfig`

---

### Key Features

* **Infrastructure Independence:** The Domain layer is completely independent of external services. This allows the data provider to be easily replaced without modifying the core business logic.
* **Scalability & Extensibility:** The implementation of `merge` and `split` methods within the Domain layer allows for fast and easy development of new features, keeping the business logic centralized and reusable
* **Time Zone Integrity:** The application utilizes the UTC standard, ensuring full data consistency with the external provider and allowing for easy global time zone reconfiguration in the future

---

## API Endpoints

The API base path is `/energy-mix`. All percentages are rounded to **2 decimal places**.

### Daily Average Generation Mix
* **URL:** `GET /energy-mix/current-three-days`
* **Description:** Retrieves aggregated energy mix data for today and the next two days.
* **Response:** `List<EnergyTimestampDto>`

### Optimal Charging Window
* **URL:** `GET /energy-mix/optimal-charging`
* **Request Parameter:** `hours` (Integer, range: 1–6)
* **Description:** Finds the continuous period with the highest total green energy percentage using a **Sliding Window Algorithm**.
* **Response:** `OptimalChargingDto`

---

## Error Handling

| Scenario | Status Code | Exception |
| :--- | :--- | :--- |
| **Invalid Hour Count** | `400 Bad Request` | `InvalidHourCountException` |
| **Illegal Arguments** | `400 Bad Request` | `IllegalArgumentException` |
| **Data Not Found** | `404 Not Found` | `RestClientException` |

---

## Related Projects
- [Frontend](https://github.com/PatMaz999/recruitment-task-frontend)

---
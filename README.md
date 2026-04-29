# QSolutions GPS Fleet Tracking Service

> Java-based SOAP client for automated GPS pulse dispatch to the QSolutions/DigiHaul logistics platform.

![Java](https://img.shields.io/badge/Java-JDK%2025-orange?style=flat-square)
![SOAP](https://img.shields.io/badge/Protocol-SOAP%2FXML-blue?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)
![Status](https://img.shields.io/badge/Status-Production-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square)

---

## The problem

A logistics company needed their fleet of 5 transport units to report GPS position to the
QSolutions/DigiHaul platform every 15 minutes during active routes. No existing tooling
handled the SOAP/XML integration their platform required.

This service solves that — a lightweight Java application that manages per-unit scheduling,
operator-driven configuration, and automated pulse dispatch over SOAP.

---

## How it works

At session start, the operator configures each unit individually:

- Activate or skip the unit for today's route
- Set that unit's operational window (e.g. `07:00–15:00`)
- Enter the initial GPS coordinates sourced from SinoTrack

The scheduler then fires every 15 minutes. Each cycle iterates over active units,
skips any outside their operational window, and dispatches a GPS pulse to the
QSolutions SOAP endpoint with real-time timestamps.

---

## Architecture

```
src/
└── com/qsolutions/gpsclient/
    ├── MainClient.java              ← entry point, starts the scheduler
    ├── config/
    │   └── FleetConfig.java         ← central registry of fleet units
    ├── model/
    │   └── Unidad.java              ← vehicle model with per-unit schedule
    ├── scheduler/
    │   └── FleetScheduler.java      ← 15-min pulse cycle + operator config
    ├── service/
    │   └── GpsSoapService.java      ← SOAP payload builder and dispatcher
    └── util/
        └── DateUtils.java           ← real-time XMLGregorianCalendar builder
```

---

## Setup

### Option A — Docker (recommended)

No Java installation required. Just Docker.

```bash
# 1. Clone the repository
git clone https://github.com/YaeltSnake/qsolutions-gps-soap-client.git
cd qsolutions-gps-soap-client

# 2. Configure credentials
cp config.properties.example config.properties
# Edit config.properties with your QSolutions credentials

# 3. Run
docker-compose run fleet-tracker
```

> `config.properties` is mounted as a volume — credentials never go inside the image.

### Option B — Local (requires Java 25 + NetBeans)

```bash
# 1. Clone the repository
git clone https://github.com/YaeltSnake/qsolutions-gps-soap-client.git
cd qsolutions-gps-soap-client

# 2. Configure credentials
cp config.properties.example src/config.properties
# Edit src/config.properties with your QSolutions credentials

# 3. Build and run
# Open in NetBeans → Clean and Build → Run MainClient.java
# Or via terminal:
java -jar dist/GPSWebServicesClient.jar
```

> `config.properties` is gitignored — credentials never leave your machine.

---

## How to add or rename a fleet unit

Edit `FleetConfig.java`:

```java
public static List<Unidad> getUnidades() {
    return Arrays.asList(
        new Unidad("Peugeot"),
        new Unidad("NuevoNombre")  // ← add or rename here
    );
}
```

> The `NumUnidad` value must match the Vehicle Registration Number
> configured in the DigiHaul Driver App.

---

## How to change the pulse interval

Edit the constant in `FleetScheduler.java`:

```java
private static final int INTERVALO_MINUTOS = 15; // ← change this value
```

---

## Tech stack

| Layer | Technology | Why |
|---|---|---|
| Language | Java JDK 25 | Enterprise-grade, strongly typed |
| Protocol | SOAP / JAX-WS 2.3.5 | Required by QSolutions endpoint |
| Serialization | XML + XSD | QSolutions contract definition |
| Scheduling | ScheduledExecutorService | Precise intervals, no framework overhead |
| Build | Apache Ant (NetBeans) | Lightweight, no Maven/Gradle dependency |
| Container | Docker + docker-compose | Zero-install deployment for operators |

---

## Roadmap

- [x] SOAP client with XML serialization
- [x] Per-unit scheduling and individual activation
- [x] Real-time timestamp generation on every pulse
- [x] Externalized credentials via config.properties
- [x] Docker containerization — zero-install deployment
- [ ] Structured logging with SLF4J/Logback
- [ ] JavaFX desktop UI for fleet management
- [ ] Web dashboard with embedded Jetty
- [ ] Flespi/SinoTrack API integration for automatic coordinate sourcing
- [ ] JUnit 5 test suite + GitHub Actions CI/CD
- [ ] Spring Boot microservices migration

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit using conventional commits: `feat:`, `fix:`, `refactor:`, `docs:`
4. Open a pull request

---

## License

MIT — feel free to use, modify and distribute.
# Building, Running & Deploying

## Prerequisites

- Java 25 (e.g. [Adoptium Temurin](https://adoptium.net)) — for JVM builds and running tests
- [GraalVM Community Edition for JDK 25](https://github.com/graalvm/graalvm-ce-builds/releases) — only required for building a native image locally
- Maven is not required — the project includes the Maven Wrapper (`./mvnw`)

## Build (JVM)

```bash
./mvnw package
```

Compiles the project, runs all tests, and produces a self-contained JAR at
`target/coraquest-progress-to-bgg-0.0.1.jar`.

To skip tests:

```bash
./mvnw package -DskipTests
```

## Build native image (Linux)

Requires GraalVM with `native-image` on `PATH`. The `native` Maven profile
activates Spring Boot's AOT processing and then compiles a self-contained native
executable:

```bash
./mvnw -Pnative native:compile -DskipTests
```

The resulting binary is placed at `target/coraquest-progress-to-bgg`. It has no
external dependencies and starts in milliseconds.

> Native compilation is CPU-intensive and typically takes several minutes.
> The CI workflow (see [GitHub setup](github-setup.md)) produces a ready-to-use
> Linux x64 binary as a downloadable artifact on every commit to `main`.

## Run locally (in-memory database)

Use the `local` Spring profile to get an in-memory H2 database and the H2 web
console at `http://localhost:8080/h2-console`:

```bash
# JVM
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# JAR
java -jar target/coraquest-progress-to-bgg-0.0.1.jar --spring.profiles.active=local

# Native binary
./target/coraquest-progress-to-bgg --spring.profiles.active=local
```

Data is lost when the process stops.

## Run on a server (file-based database)

Without the `local` profile the application uses a file-based H2 database. The
database file is created at `./data/coraquestdb.mv.db` relative to the working
directory and survives restarts.

```bash
# JAR
java -jar target/coraquest-progress-to-bgg-0.0.1.jar

# Native binary
./coraquest-progress-to-bgg
```

The application listens on port `8080` by default. To change the port:

```bash
java -jar target/coraquest-progress-to-bgg-0.0.1.jar --server.port=9090
# or
./coraquest-progress-to-bgg --server.port=9090
```

### Running as a systemd service

Create `/etc/systemd/system/coraquest.service`:

```ini
[Unit]
Description=CoraQuest Progress to BGG
After=network.target

[Service]
User=coraquest
WorkingDirectory=/opt/coraquest
ExecStart=/opt/coraquest/coraquest-progress-to-bgg
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

Then enable and start it:

```bash
sudo systemctl daemon-reload
sudo systemctl enable coraquest
sudo systemctl start coraquest
```

## Deploy

Download the `coraquest-linux-amd64` artifact from the latest successful CI run
on GitHub (see [GitHub setup](github-setup.md)), copy it to the server, and
restart the service:

```bash
scp coraquest-progress-to-bgg user@server:/opt/coraquest/
ssh user@server "sudo systemctl restart coraquest"
```

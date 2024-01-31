## Start infrastructure dependencies
```shell
docker compose -p dio up 
```
## Local Infrastrucure Services

- Prometheus: http://localhost:9090
- Loki, Grafana, Tempo: http://localhost:3000

## Generate fake client files
```shell
time make create-abc-client-files
```
```shell
time make create-def-client-files
```
```shell
time make create-ghi-client-files
```
## Start the demos
```shell
./mvnw spring-boot:run -pl scheduler
```
```shell
./mvnw spring-boot:run -pl collector
```
## Stop infrastructure dependencies and purge data
```shell
docker compose -p dio down --volumes 
```

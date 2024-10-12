## Run Grafana and Tempo Locally
See <https://github.com/grafana/tempo/tree/main/example/docker-compose/local>
All data is stored locally in the `tempo-data` folder.
Create the storage directory with the correct permissions and start up the local stack.

```console
mkdir tempo-data/
docker compose up -d
```

# Backup

```shell
sudo docker exec -it keycloak.cert sh -c "./opt/keycloak/bin/kc.sh export --file /tmp/realms.json" && sudo docker cp keycloak.cert:/tmp/realms.json .
```

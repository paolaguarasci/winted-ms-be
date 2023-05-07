# Backup

```shell
sudo docker exec -it keycloak sh -c "./opt/keycloak/bin/kc.sh export --file /tmp/realms.json" && sudo docker cp keycloak:/tmp/realms.json .
```

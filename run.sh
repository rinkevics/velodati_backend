#!/bin/bash

read -s mySecret
export mySecret

(printenv mySecret | nohup java -jar -Xms128m -Xmx1024m "target/velodati-1.0-SNAPSHOT.jar" secrets-file="/home/velo/velodati_backend/secrets.properties" server-url="https://veloslazdi.datuskola.lv/") & disown

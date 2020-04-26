#!/bin/bash

read -s mySecret
export mySecret

#nohup java -jar -Xms64m -Xmx128m "target\velodati-1.0-SNAPSHOT.jar" secrets-file="C:\work\secrets.properties" password="$a" > out.txt &

printenv mySecret | nohup java -jar -Xms64m -Xmx128m "target\velodati-1.0-SNAPSHOT.jar" secrets-file="C:\work\secrets.properties"


#!/usr/bin/env bash
# start cassandra
CASSANDRA="apache-cassandra-3.1"
cd /home/vagrant/$CASSANDRA
if ps aux | grep "cassandra" | grep -v grep > /dev/null
then
    echo "Cassandra has already been run"
else
    echo "Start Cassandra"
    bin/cassandra
fi
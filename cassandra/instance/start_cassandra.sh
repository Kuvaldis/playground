#!/usr/bin/env bash
# start cassandra
CASSANDRA="apache-cassandra-3.0.0"
cd /home/vagrant/$CASSANDRA
if ps aux | grep "cassandra" | grep -v grep > /dev/null
then
    echo "Cassandra has already been run"
else
    echo "Start Cassandra"
    bin/cassandra
fi
bin/cqlsh -f /home/vagrant/data/load_users.cql

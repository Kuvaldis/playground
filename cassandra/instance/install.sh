#!/bin/bash
apt-get install python-software-properties -y
add-apt-repository ppa:openjdk-r/ppa
apt-get update
apt-get install openjdk-8-jdk -y
update-alternatives --config java
update-alternatives --config javac

# download cassandra
CASSANDRA="apache-cassandra-3.0.0"
if [ ! -d  "$CASSANDRA" ]; then
    wget http://apache-mirror.rbc.ru/pub/apache/cassandra/3.0.0/$CASSANDRA-bin.tar.gz
    tar xvzf $CASSANDRA-bin.tar.gz
fi
cd $CASSANDRA

# data and log directories
if [ ! -d "/var/lib/cassandra" ]; then
    mkdir /var/lib/cassandra
    chown -R $USER:$GROUP /var/lib/cassandra
fi
if [ ! -d "/var/log/cassandra" ]; then
    mkdir /var/log/cassandra
    chown -R $USER:$GROUP /var/log/cassandra
fi

# start cassandra
cd ~/$CASSANDRA
bin/cassandra
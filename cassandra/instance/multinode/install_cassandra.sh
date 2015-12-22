#!/bin/bash
cat > /etc/hosts <<EOF
127.0.0.1       localhost
10.211.55.100   cassandra-node0
10.211.55.101   cassandra-node1
10.211.55.102   cassandra-node2
10.211.55.103   cassandra-node3
EOF

apt-get install python-software-properties -y
add-apt-repository ppa:openjdk-r/ppa
apt-get update
apt-get install openjdk-8-jdk -y
update-alternatives --config java
update-alternatives --config javac

# download cassandra
CASSANDRA="apache-cassandra-3.1"
if [ ! -d  "$CASSANDRA" ]; then
    wget http://apache-mirror.rbc.ru/pub/apache/cassandra/3.1/$CASSANDRA-bin.tar.gz
    tar xvzf $CASSANDRA-bin.tar.gz
fi
cd $CASSANDRA
chown -R vagrant:vagrant /home/vagrant/$CASSANDRA

# data and log directories
if [ ! -d "/var/lib/cassandra" ]; then
    mkdir /var/lib/cassandra
    chown -R vagrant:vagrant /var/lib/cassandra
fi
if [ ! -d "/var/log/cassandra" ]; then
    mkdir /var/log/cassandra
    chown -R vagrant:vagrant /var/log/cassandra
fi
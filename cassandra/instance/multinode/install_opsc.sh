#!/usr/bin/env bash
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

echo "deb http://debian.datastax.com/community stable main" | tee /etc/apt/sources.list.d/cassandra.sources.list
wget -q -O - http://debian.datastax.com/debian/repo_key | apt-key add -
apt-get update
apt-get install vim curl zip unzip git python-pip opscenter -y
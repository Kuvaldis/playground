#!/usr/bin/env bash

add-apt-repository ppa:openjdk-r/ppa -y
apt-get update

apt-get -y install vim curl nmap tree zip htop upstart
apt-get -y install git


apt-get -y install openjdk-8-jre-headless
update-alternatives --config java

mkdir /opt/gerrit
curl https://gerrit-releases.storage.googleapis.com/gerrit-2.14.war -o gerrit.war
java -jar gerrit.war init -d /opt/gerrit --batch --dev
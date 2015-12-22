#!/usr/bin/env bash
apt-get update
apt-get install python-software-properties -y
add-apt-repository ppa:openjdk-r/ppa
apt-get update
apt-get install openjdk-8-jdk -y
update-alternatives --config java
update-alternatives --config javac

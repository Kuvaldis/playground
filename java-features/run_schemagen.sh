#!/usr/bin/env bash
# will generate schema1.xsd with schema for Employee and Address
javac -d bin src/main/java/kuvaldis/play/java/xml/*.java
schemagen -cp bin src/main/java/kuvaldis/play/java/xml/Employee.java
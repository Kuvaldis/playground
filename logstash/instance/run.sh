#!/usr/bin/env bash
docker run \
  --rm \
  -it \
  -v ./pipeline/:/usr/share/logstash/pipeline/ \
  docker.elastic.co/logstash/logstash-oss:6.1.4
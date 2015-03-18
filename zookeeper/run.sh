docker run --name zk-kuvaldis-v1 -d \
-v /opt/zookeeper/conf:/home/kuvaldis/Work/Mine/Study/playground/zookeeper/conf \
-v /var/lib/zookeeper:/home/kuvaldis/Work/Mine/Study/tmp/zk \
-p 2181:2181
zookeeper-kuvaldis



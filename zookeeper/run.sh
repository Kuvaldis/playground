docker run --name zk-kuvaldis-v1 -d \
-v /home/kuvaldis/Work/Mine/Study/playground/zookeeper/conf:/opt/zookeeper/conf \
-v /home/kuvaldis/Work/Mine/Study/tmp/zk1:/var/lib/zookeeper \
-p 2181:2181 -p 2888:2888 -p 3888:3888 zookeeper-kuvaldis
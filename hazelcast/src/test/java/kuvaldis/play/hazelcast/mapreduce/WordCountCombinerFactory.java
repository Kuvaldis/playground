package kuvaldis.play.hazelcast.mapreduce;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class WordCountCombinerFactory implements CombinerFactory<String, Long, Long> {

    // running for each key in a node. will be reused for each partition on the node, so reset method is needed
    @Override
    public Combiner<Long, Long> newCombiner(String key) {
        return new WordCountCombiner();
    }

    // there can be several values for the same key on a particular node.
    // so it's like pre reduce operations and are run inside the node.
    // single chunk is run on the same thread
    private class WordCountCombiner extends Combiner<Long, Long> {
        private long sum = 0;

        @Override
        public void combine(Long value) {
            sum++;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0;
        }
    }
}

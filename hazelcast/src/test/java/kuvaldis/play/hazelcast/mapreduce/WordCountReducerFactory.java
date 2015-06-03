package kuvaldis.play.hazelcast.mapreduce;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class WordCountReducerFactory implements ReducerFactory<String, Long, Long> {

    // final step, run on a single node per key, so there is no need reducer reusing
    @Override
    public Reducer<Long, Long> newReducer(String key) {
        return new WordCountReducer();
    }

    private static class WordCountReducer extends Reducer<Long, Long> {

        // no need to make volatile in current version. there was necessity as there could be thread switching
        private long sum = 0;

        @Override
        public void reduce(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}

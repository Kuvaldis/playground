package kuvaldis.play.hazelcast.mapreduce;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.StringTokenizer;

public class TokenizerMapper implements Mapper<String, String, String, Long> {

    // makes a single token as a key and maybe multiple values for the same key. they are still in different nodes
    @Override
    public void map(String key, String value, Context<String, Long> context) {
        final StringTokenizer tokenizer = new StringTokenizer(value);
        while (tokenizer.hasMoreTokens()) {
            context.emit(tokenizer.nextToken(), 1l);
        }
    }
}

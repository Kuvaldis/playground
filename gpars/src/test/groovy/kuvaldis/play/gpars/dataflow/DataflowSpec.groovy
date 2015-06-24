package kuvaldis.play.gpars.dataflow

import groovyx.gpars.dataflow.Dataflow
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.stream.DataflowStream
import groovyx.gpars.dataflow.stream.DataflowStreamReadAdapter
import groovyx.gpars.dataflow.stream.DataflowStreamWriteAdapter
import spock.lang.Specification

import static groovyx.gpars.dataflow.Dataflow.task


class DataflowSpec extends Specification {

    def "data flow hello world"() {
        given:
        final x = new DataflowVariable<Integer>()
        final y = new DataflowVariable<Integer>()
        final z = new DataflowVariable<Integer>()
        when:
        task {
            z << x.val + y.val
        }
        and:
        task {
            x << 10
        }
        and:
        task {
            y << 5
        }
        then:
        15 == z.val
    }

    def "data flow channels test"() {
        given:
        final broadcast = new DataflowBroadcast<Integer>()
        final channel1 = broadcast.createReadChannel()
        final channel2 = broadcast.createReadChannel()
        when:
        broadcast << 1
        broadcast << 2
        broadcast << 3
        then:
        1 == channel1.val
        1 == channel2.val
        2 == channel1.val
        2 == channel2.val
        3 == channel1.val
        3 == channel2.val
    }

    /**
     * Flow explaied:
     * 1. writer1 gets value 1. Means stream1 contains value 1
     * 2. stream1's reader1 is bind to writer2 with operator, so writer2 gets 1 with bindOutput method. Now stream2 contains value 1
     * 3. stream2's reader2 is bind to result queue with selector, so result queue gets value 1.
     */
    def "data flow adapters test"() {
        given:
        final stream1 = new DataflowStream<Integer>()
        final stream2 = new DataflowStream<Integer>()
        and:
        final writer1 = new DataflowStreamWriteAdapter<Integer>(stream1)
        final writer2 = new DataflowStreamWriteAdapter<Integer>(stream2)
        and:
        final reader1 = new DataflowStreamReadAdapter<Integer>(stream1)
        final reader2 = new DataflowStreamReadAdapter<Integer>(stream2)
        and:
        final result = new DataflowQueue<Integer>()
        and:
        // moves from reader to writer with calculation. to bind value to the only output (writer) use bindOutput method
        Dataflow.operator(reader1, writer2) {
            bindOutput it
        }
        and:
        Dataflow.selector([reader2], [result]) {
            bindAllOutputs it
        }
        when:
        writer1 << 1
        and:
        writer1 << 2
        and:
        writer1 << 3
        then:
        [1, 2, 3] == [result.val, result.val, result.val]
    }
}
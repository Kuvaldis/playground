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

    def "bind data flow variable methods test"() {
        given:
        final isBound = new DataflowVariable<Boolean>();
        and:
        final a = new DataflowVariable<Integer>()
        a >> {
            isBound << true
        }
        when:
        a << 1
        then:
        isBound.val
    }

    def "chain variables test"() {
        given:
        final result = new DataflowVariable<Integer>()
        and:
        final var = new DataflowVariable<Integer>()
        var >> { it * 2 } >> { it + 1 } >> { result << it }
        when:
        var << 4
        then:
        9 == result.val
    }

    def "variable exception handling test"() {
        given:
        final var = new DataflowVariable<Integer>()
        and:
        final chain = var >> { it * 2 } >> { 1 / it }
        and:
        final result = chain.then({ "Passed" }, { "Failed" })
        when:
        var << 0
        then:
        "Failed" == result.val
    }

    def "fork and join test"() {
        given:
        final values = new DataflowVariable<List<Integer>>()
        when:
        task {
            2
        }.thenForkAndJoin({ it**2 }, { it**3 }, { it**4 }, { it**5 }).then({ values << it }).join()
        then:
        [4, 8, 16, 32] == values.val
    }

    def "lazy task test"() {
        given:
        def attempts = 0
        final lazyTask = Dataflow.lazyTask { attempts++ }
        when:
        lazyTask.get()
        and:
        lazyTask.get()
        then:
        attempts == 1
    }

    def "data flow variable calculation"() {
        given:
        final initialDistance = new DataflowVariable<Integer>()
        final acceleration = new DataflowVariable<Integer>()
        final time = new DataflowVariable<Integer>()
        when:
        task {
            initialDistance << 100
            acceleration << 2
            time << 10
        }
        and:
        final result = initialDistance + acceleration * 0.5 * time**2
        then:
        200 == result.val
    }
}
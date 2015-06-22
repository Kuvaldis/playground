package kuvaldis.play.gpars

import groovyx.gpars.dataflow.SyncDataflowQueue
import groovyx.gpars.group.DefaultPGroup
import groovyx.gpars.scheduler.ResizeablePool
import spock.lang.Specification


class CSPSpec extends Specification {

    def "task return result"() {
        given:
        final group = new DefaultPGroup(new ResizeablePool(true))
        when:
        final task = group.task { "Hello CSP!" }
        then:
        "Hello CSP!" == task.get()
    }

    def "channels test"() {
        given:
        final inputChannel = new SyncDataflowQueue<String>()
        final outputChannel = new SyncDataflowQueue<String>()
        final group = new DefaultPGroup(new ResizeablePool(true))
        when:
        final task = group.task {
            final term1 = inputChannel.val
            final term2 = inputChannel.val
            outputChannel << term1
            outputChannel << term2
            "Done"
        }
        and:
        inputChannel << "Me"
        inputChannel << "Willy"
        and:
        final term1 = outputChannel.val
        final term2 = outputChannel.val
        then:
        "Done" == task.get()
        "Me" == term1
        "Willy" == term2
    }
}
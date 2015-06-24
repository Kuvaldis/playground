package kuvaldis.play.gpars

import groovyx.gpars.stm.GParsStm
import org.multiverse.api.PropagationLevel
import org.multiverse.api.StmUtils
import spock.lang.Specification

// work only with objects through special methods. don't try to modify several objects in the same atomic block
class STMSpec extends Specification {
    def "test transaction rollback"() {
        given:
        final counter = StmUtils.newTxnInteger(0)
        and:
        final block = GParsStm.createTxnExecutor(maxRetries: 3000, familyName: 'Custom',
                PropagationLevel: PropagationLevel.Requires, interruptible: false)
        when:
        try {
            GParsStm.atomic(block) {
                counter.increment(1)
                throw new Exception()
            }
        } catch (Exception ignore) { }
        and:
        GParsStm.atomic(block) {
            counter.increment(3)
        }
        then:
        3 == counter.atomicGet()
    }
}


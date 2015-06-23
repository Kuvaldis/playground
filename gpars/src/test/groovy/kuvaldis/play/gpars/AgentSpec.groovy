package kuvaldis.play.gpars

import groovyx.gpars.agent.Agent
import spock.lang.Specification

class AgentSpec extends Specification {

    def "agent test"() {
        given:
        final agent = Agent.agent([])
        when:
        agent << {
            it << 'Me'
        }
        and:
        agent << {
            it << 'Willy'
        }
        and:
        agent << {
            updateValue(['Donkey!'])
        }
        then:
        agent.await()
        ['Donkey!'] == agent.val
    }

    def "agent validator test"() {
        given:
        final agent = Agent.agent 0
        agent.addValidator {oldValue, newValue ->
            if (oldValue > newValue) throw new IllegalArgumentException('New value should be greater then old')}
        when:
        agent << 10
        and:
        agent << 5
        then:
        agent.await()
        agent.hasErrors()
    }
}

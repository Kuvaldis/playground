package kuvaldis.play.gpars.actor

import spock.lang.Specification

import static groovyx.gpars.actor.Actors.actor

class ActorSpec extends Specification {

    def "hello world"() {
        given:
        def decryptedMessage;
        final decryptor = actor {
            loop {
                react { message ->
                    if (message instanceof String) reply message.reverse()
                    else stop()
                }
            }
        }
        final console = actor {
            decryptor.send '!dlroW ,olleH'
            react {
                decryptedMessage = it;
                decryptor.send false
            }
        }
        when:
        [decryptor, console]*.join()
        then:
        'Hello, World!' == decryptedMessage
    }

    def "yet another actors example from manual"() {
        given:
        final answerHolder = [value: null]
        final gameMaster = new GameMaster(secretNumber: 5)
        final largePlayer = new Player(gameMaster: gameMaster, guess: 7, answer: answerHolder)
        final smallPlayer = new Player(gameMaster: gameMaster, guess: 3, answer: answerHolder)
        final winner = new Player(gameMaster: gameMaster, guess: 5, answer: answerHolder)
        when:
        gameMaster.start()
        and:
        largePlayer.start()
        largePlayer.join()
        then:
        'too large' == answerHolder.value
        when:
        smallPlayer.start()
        smallPlayer.join()
        then:
        'too small' == answerHolder.value
        when:
        winner.start()
        winner.join()
        then:
        'you win' == answerHolder.value
    }

    def "sender test"() {
        given:
        final result = [value: '']
        final receiverActor = actor {
            react {
                sender << 'Go away'
            }
        }
        final senderActor = actor {
            receiverActor << 'Hi!'
            react {
                switch (it) {
                    case 'Go away': result.value = 'Fuck you!'; break
                    default: result.value = 'Great!'
                }
            }
        }
        when:
        [senderActor, receiverActor]*.join()
        then:
        'Fuck you!' == result.value
    }

    def "loop times test"() {
        given:
        def max;
        final actor = actor {
            final candidates = []
            loop(3) { max = candidates.max() } {
                react {
                    candidates << it
                }
            }
        }
        when:
        actor << 10
        actor << 20
        actor << 30
        and:
        actor.join()
        then:
        30 == max
    }

    def "loop until test"() {
        given:
        def max;
        final actor = actor {
            final candidates = []
            loop { candidates.max() < 30 } { max = candidates.max() } {
                react { candidates << it }
            }
        }
        when:
        actor << 10
        actor << 20
        actor << 30
        actor << 40
        actor << 50
        and:
        actor.join()
        then:
        30 == max
    }

    def "dynamic dispatch actor test"() {
        given:
        final dda = new CustomDDA().become {
            when { Long aLong -> values << 'Long' }
            when { BigDecimal bigDecimal -> values << 'BigDecimal' }
        }
        when:
        dda.start()
        and:
        actor {
            dda << 'Hello'
            dda << 10
            dda << 10l
            dda << (10 as BigDecimal)
            dda << new CustomDDA.Kill()
            dda.join()
        }.join()
        then:
        ['String', 'Integer', 'Kill', 'Long', 'BigDecimal'] as Set == dda.values

    }
}
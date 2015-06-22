package kuvaldis.play.gpars

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom

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

    def "sender test" () {
        given:
        final result = [value: '']
        final receiverActor = actor {
            react {
                sender << 'Go away'
                terminate()
            }
        }
        final senderActor = actor {
            receiverActor << 'Hi!'
            react {
                switch (it) {
                    case 'Go away': result.value = 'Fuck you!'; break
                    default: result.value = 'Great!'
                }
                terminate()
            }
        }
        when:
        [senderActor, receiverActor]*.join()
        then:
        'Fuck you!' == result.value
    }
}
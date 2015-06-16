package kuvaldis.play.gpars

import org.codehaus.groovy.ast.ClassNode
import org.spockframework.compiler.model.Spec
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

import static groovyx.gpars.actor.Actors.actor

class ActorsSpec extends Specification {

    def "hello world" () {
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
}
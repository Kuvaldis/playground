package kuvaldis.play.gpars

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

class Player extends DefaultActor {

    Actor gameMaster
    int guess;
    def answer;

    void act() {
        loop {
            gameMaster << guess
            react {
                switch (it) {
                    case 'too large': answer.value = 'too large'; break
                    case 'too small': answer.value = 'too small'; break
                    case 'you win': answer.value = 'you win'; break
                }
                terminate()
            }
        }
    }
}

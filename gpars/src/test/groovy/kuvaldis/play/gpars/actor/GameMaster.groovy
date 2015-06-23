package kuvaldis.play.gpars.actor

import groovyx.gpars.actor.DefaultActor

class GameMaster extends DefaultActor {

    int secretNumber

    void afterStart() {
        println 'Will be run after start'
    }

    @Override
    protected void act() {
        loop {
            react { int number ->
                if (number > secretNumber) {
                    reply 'too large'
                } else if (number < secretNumber) {
                    reply 'too small'
                } else {
                    reply 'you win'
                    terminate()
                }
            }
        }
    }
}

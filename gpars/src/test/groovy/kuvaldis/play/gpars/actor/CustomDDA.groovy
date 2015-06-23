package kuvaldis.play.gpars.actor

import groovyx.gpars.actor.DynamicDispatchActor

class CustomDDA extends DynamicDispatchActor {

    static class Kill {}

    def values = [] as Set

    void onMessage(final String message) {
        values << 'String'
    }

    void onMessage(final Integer integer) {
        values << 'Integer'
    }

    void onMessage(final Kill kill) {
        values << 'Kill'
        stop()
    }
}

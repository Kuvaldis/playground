package kuvaldis.play.gpars.actor

import groovyx.gpars.activeobject.ActiveMethod
import groovyx.gpars.activeobject.ActiveObject

@ActiveObject("group")
class DecryptorActiveObject {

    @ActiveMethod
    def decrypt(final String encryptedText) {
        encryptedText.reverse()
    }
}

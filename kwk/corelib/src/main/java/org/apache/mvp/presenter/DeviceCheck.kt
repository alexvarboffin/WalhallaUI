package org.apache.mvp.presenter

class DeviceCheck private constructor(builder: Builder) {

    val isCheckEmulator: Boolean = builder.checkEmulator
    val isCheckRoot: Boolean = builder.rooted
    val isPackageSignature: Boolean = builder.signature


    class Builder {
        internal var checkEmulator: Boolean = false
        var rooted: Boolean = false
        var signature: Boolean = false

        fun setCheckEmulator(checkEmulator: Boolean): Builder {
            this.checkEmulator = checkEmulator
            return this
        }

        fun setRooted(rooted: Boolean): Builder {
            this.rooted = rooted
            return this
        }

        fun setSignature(signature: Boolean): Builder {
            this.signature = signature
            return this
        }

        fun build(): DeviceCheck {
            return DeviceCheck(this)
        }
    } // Другие методы класса
}

package org.llschall.control.board

import org.springframework.stereotype.Component

@Component
class CounterModel {
    @JvmField
    @Volatile
    var value: Int = 0

    @JvmField
    @Volatile
    var switchOn: Boolean = false

    fun increment() {
        if (switchOn) {
            this.value++
        }
    }
}
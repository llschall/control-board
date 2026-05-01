package org.llschall.control.board

import org.springframework.stereotype.Component

@Component
class CounterModel {
    @JvmField
    var value: Int = 0

    fun increment() {
        this.value++
    }
}
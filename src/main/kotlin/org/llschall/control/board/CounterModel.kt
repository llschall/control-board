package org.llschall.control.board

import org.springframework.stereotype.Component

@Component
class CounterModel {
    @JvmField
    var value: Int = 0

    @JvmField
    var switchOn: Boolean = false

    fun increment() {
        this.value++
    }
}
package org.llschall.boot.controller

import org.springframework.stereotype.Component

@Component
class CounterModel {
    @kotlin.jvm.JvmField
    var value: Int = 0

    fun increment() {
        this.value++
    }
}

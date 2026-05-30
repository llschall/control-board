package org.llschall.control.board

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CounterModel(private val measurementRepository: MeasurementRepository) {
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
        save()
    }

    fun save() {
        measurementRepository.save(Measurement(value, LocalDateTime.now()))
    }
}
package pl.edu.pollub.parser.domain

import java.util.*

class Computer(
        var manufacturer: String = DEFAULT_VALUE,
        var matrixSize: String = DEFAULT_VALUE,
        var resolution: String = DEFAULT_VALUE,
        var matrixType: String = DEFAULT_VALUE,
        var touchscreen: String = DEFAULT_VALUE,
        var processor: String = DEFAULT_VALUE,
        var coresCount: String = DEFAULT_VALUE,
        var timing: String = DEFAULT_VALUE,
        var ram: String = DEFAULT_VALUE,
        var discCapacity: String = DEFAULT_VALUE,
        var discType: String = DEFAULT_VALUE,
        var graphicCard: String = DEFAULT_VALUE,
        var graphicCardMemory: String = DEFAULT_VALUE,
        var operationSystem: String = DEFAULT_VALUE,
        var opticalDrive: String = DEFAULT_VALUE
) {
    val id: ComputerId = ComputerId(UUID.randomUUID().toString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Computer

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

data class ComputerId(val value: String)

const val DEFAULT_VALUE = ""
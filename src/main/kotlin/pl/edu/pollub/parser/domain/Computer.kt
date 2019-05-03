package pl.edu.pollub.parser.domain

import java.util.*

class Computer(
        var manufacturer: Manufacturer = Manufacturer(),
        var screen: Screen = Screen(),
        var processor: Processor = Processor(),
        var ram: Ram = Ram(),
        var disc: Disc = Disc(),
        var graphicCard: GraphicCard = GraphicCard(),
        var operationSystem: OperationSystem = OperationSystem(),
        var discReader: DiscReader = DiscReader()
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

data class ComputerId(val value: String = DEFAULT_VALUE)

data class Manufacturer(val value: String = DEFAULT_VALUE)

data class Screen(val size: String = DEFAULT_VALUE,
                  val resolution: String = DEFAULT_VALUE,
                  val type: String = DEFAULT_VALUE,
                  val touchscreen: String = DEFAULT_VALUE)

data class Processor(val name: String = DEFAULT_VALUE,
                     val physicalCores: String = DEFAULT_VALUE,
                     val clockSpeed: String = DEFAULT_VALUE)

data class Ram(val value: String = DEFAULT_VALUE)

data class Disc(val storage: String = DEFAULT_VALUE, val type: String = DEFAULT_VALUE)

data class GraphicCard(val name: String = DEFAULT_VALUE, val memory: String = DEFAULT_VALUE)

data class OperationSystem(val value: String = DEFAULT_VALUE)

data class DiscReader(val value: String = DEFAULT_VALUE)

const val DEFAULT_VALUE = ""
package pl.edu.pollub.parser.domain

data class Computer(
        val manufacturer: String = DEFAULT_VALUE,
        val matrixSize: String = DEFAULT_VALUE,
        val resolution: String = DEFAULT_VALUE,
        val matrixType: String = DEFAULT_VALUE,
        val touchscreen: String = DEFAULT_VALUE,
        val processor: String = DEFAULT_VALUE,
        val coresCount: String = DEFAULT_VALUE,
        val timing: String = DEFAULT_VALUE,
        val ram: String = DEFAULT_VALUE,
        val discCapacity: String = DEFAULT_VALUE,
        val discType: String = DEFAULT_VALUE,
        val graphicCard: String = DEFAULT_VALUE,
        val graphicCardMemory: String = DEFAULT_VALUE,
        val operationSystem: String = DEFAULT_VALUE,
        val opticalDrive: String = DEFAULT_VALUE
)

const val DEFAULT_VALUE = "brak"
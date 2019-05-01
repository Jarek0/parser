package pl.edu.pollub.parser.domain.assertions

import pl.edu.pollub.parser.domain.Computer

class ComputerAssert {

    private final Computer actual

    private ComputerAssert(Computer actual) {
        this.actual = actual
    }

    static ComputerAssert assertComputer(Computer actual) {
        return new ComputerAssert(actual)
    }

    void isDataSame(Computer other) {
        assert actual.manufacturer == other.manufacturer
        assert actual.matrixSize == other.matrixSize
        assert actual.resolution == other.resolution
        assert actual.matrixType == other.matrixType
        assert actual.touchscreen == other.touchscreen
        assert actual.processor == other.processor
        assert actual.coresCount == other.coresCount
        assert actual.timing == other.timing
        assert actual.ram == other.ram
        assert actual.discCapacity == other.discCapacity
        assert actual.discType == other.discType
        assert actual.graphicCard == other.graphicCard
        assert actual.graphicCardMemory == other.graphicCardMemory
        assert actual.operationSystem == other.operationSystem
        assert actual.opticalDrive == other.opticalDrive
    }
}

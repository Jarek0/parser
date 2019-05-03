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

    void hasData(String[] data) {
        assert actual.manufacturer == data[0]
        assert actual.matrixSize == data[1]
        assert actual.resolution == data[2]
        assert actual.matrixType == data[3]
        assert actual.touchscreen == data[4]
        assert actual.processor == data[5]
        assert actual.coresCount == data[6]
        assert actual.timing == data[7]
        assert actual.ram == data[8]
        assert actual.discCapacity == data[9]
        assert actual.discType == data[10]
        assert actual.graphicCard == data[11]
        assert actual.graphicCardMemory == data[12]
        assert actual.operationSystem == data[13]
        assert actual.opticalDrive == data[14]
    }

    void hasDataAndId(String[] data) {
        assert actual.id.value == data[0]
        assert actual.manufacturer == data[1]
        assert actual.matrixSize == data[2]
        assert actual.resolution == data[3]
        assert actual.matrixType == data[4]
        assert actual.touchscreen == data[5]
        assert actual.processor == data[6]
        assert actual.coresCount == data[7]
        assert actual.timing == data[8]
        assert actual.ram == data[9]
        assert actual.discCapacity == data[10]
        assert actual.discType == data[11]
        assert actual.graphicCard == data[12]
        assert actual.graphicCardMemory == data[13]
        assert actual.operationSystem == data[14]
        assert actual.opticalDrive == data[15]
    }
}

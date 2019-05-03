package pl.edu.pollub.parser.domain.assertions

import pl.edu.pollub.parser.application.ComputerConverters
import pl.edu.pollub.parser.domain.Computer

import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleComputer

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
        assert actual.screen == other.screen
        assert actual.processor == other.processor
        assert actual.ram == other.ram
        assert actual.disc == other.disc
        assert actual.graphicCard == other.graphicCard
        assert actual.operationSystem == other.operationSystem
        assert actual.discReader == other.discReader
    }

    void hasData(String[] data) {
        def other = ComputerConverters.@Companion.convert(data, sampleComputer())
        isDataSame(other)
    }

    void hasDataAndId(String[] data) {
        assert actual.id.value == data[0]
        def other = ComputerConverters.@Companion.convert(data.drop(1), sampleComputer())
        isDataSame(other)
    }
}

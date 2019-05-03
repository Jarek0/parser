package pl.edu.pollub.parser.domain.samples

import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.Disc
import pl.edu.pollub.parser.domain.DiscReader
import pl.edu.pollub.parser.domain.GraphicCard
import pl.edu.pollub.parser.domain.Manufacturer
import pl.edu.pollub.parser.domain.OperationSystem
import pl.edu.pollub.parser.domain.Processor
import pl.edu.pollub.parser.domain.Ram
import pl.edu.pollub.parser.domain.Screen

@SuppressWarnings("GroovyAssignabilityCheck")
class SampleComputer {

    static Computer sampleComputer(customProperties = [:]) {
        def properties = [
                manufacturer: sampleManufacturer(),
                screen: sampleScreen(),
                processor: sampleProcessor(),
                ram: sampleRam(),
                disc: sampleDisc(),
                graphicCard: sampleGraphicCard(),
                operationSystem: sampleOperationSystem(),
                discReader: sampleDiscReader()
        ] + customProperties
        return new Computer(
                (Manufacturer) properties.manufacturer,
                (Screen) properties.screen,
                (Processor) properties.processor,
                (Ram) properties.ram,
                (Disc) properties.disc,
                (GraphicCard) properties.graphicCard,
                (OperationSystem) properties.operationSystem,
                (DiscReader) properties.discReader
        )
    }

    static Manufacturer sampleManufacturer(customProperties = [:]) {
        def properties = [
                value: "",
        ] + customProperties
        return new Manufacturer((String) properties.value)
    }

    static Screen sampleScreen(customProperties = [:]) {
        def properties = [
                size: "",
                resolution: "",
                type: "",
                touchscreen: "",
        ] + customProperties
        return new Screen(
                (String) properties.size,
                (String) properties.resolution,
                (String) properties.type,
                (String) properties.touchscreen,
        )
    }

    static Processor sampleProcessor(customProperties = [:]) {
        def properties = [
                name: "",
                physicalCores: "",
                clockSpeed: ""
        ] + customProperties
        return new Processor(
                (String) properties.name,
                (String) properties.physicalCores,
                (String) properties.clockSpeed
        )
    }

    static Ram sampleRam(customProperties = [:]) {
        def properties = [
                value: ""
        ] + customProperties
        return new Ram(
                (String) properties.value
        )
    }

    static Disc sampleDisc(customProperties = [:]) {
        def properties = [
                storage: "",
                type: ""
        ] + customProperties
        return new Disc(
                (String) properties.storage,
                (String) properties.type
        )
    }

    static GraphicCard sampleGraphicCard(customProperties = [:]) {
        def properties = [
                name: "",
                memory: ""
        ] + customProperties
        return new GraphicCard(
                (String) properties.name,
                (String) properties.memory
        )
    }

    static OperationSystem sampleOperationSystem(customProperties = [:]) {
        def properties = [
                value: ""
        ] + customProperties
        return new OperationSystem(
                (String) properties.value
        )
    }

    static DiscReader sampleDiscReader(customProperties = [:]) {
        def properties = [
                value: ""
        ] + customProperties
        return new DiscReader(
                (String) properties.value
        )
    }

}

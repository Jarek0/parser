package pl.edu.pollub.parser.domain.samples

import pl.edu.pollub.parser.domain.Computer

class SampleComputer {

    static Computer sampleComputer(customProperties = [:]) {
        def properties = [
                manufacturer: "",
                matrixSize: "",
                resolution: "",
                matrixType: "",
                touchscreen: "",
                processor: "",
                coresCount: "",
                timing: "",
                ram: "",
                discCapacity: "",
                discType: "",
                graphicCard: "",
                graphicCardMemory: "",
                operationSystem: "",
                opticalDrive: ""
        ] + customProperties
        return new Computer(
                (String) properties.manufacturer,
                (String) properties.matrixSize,
                (String) properties.resolution,
                (String) properties.matrixType,
                (String) properties.touchscreen,
                (String) properties.processor,
                (String) properties.coresCount,
                (String) properties.timing,
                (String) properties.ram,
                (String) properties.discCapacity,
                (String) properties.discType,
                (String) properties.graphicCard,
                (String) properties.graphicCardMemory,
                (String) properties.operationSystem,
                (String) properties.opticalDrive
        )
    }

}

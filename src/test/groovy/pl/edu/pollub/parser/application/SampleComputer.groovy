package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.Computer

class SampleComputer {

    static Computer sampleComputer(customProperties = [:]) {
        def properties = [
                manufacturer: "brak",
                matrixSize: "brak",
                resolution: "brak",
                matrixType: "brak",
                touchscreen: "brak",
                processor: "brak",
                coresCount: "brak",
                timing: "brak",
                ram: "brak",
                discCapacity: "brak",
                discType: "brak",
                graphicCard: "brak",
                graphicCardMemory: "brak",
                operationSystem: "brak",
                opticalDrive: "brak"
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

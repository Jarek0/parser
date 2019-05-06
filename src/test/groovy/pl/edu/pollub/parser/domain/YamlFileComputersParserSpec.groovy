package pl.edu.pollub.parser.domain

import pl.edu.pollub.parser.domain.yaml.YamlFileComputersParser
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.domain.assertions.ComputerAssert.assertComputer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleComputer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleDisc
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleDiscReader
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleGraphicCard
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleManufacturer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleOperationSystem
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleProcessor
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleRam
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleScreen

class YamlFileComputersParserSpec extends Specification {

    @Subject
    def parser = new YamlFileComputersParser()

    def "should parse to computer when file content has single computer"() {
        given:
            def fileContent = """\
                                laptops: 
                                 laptop:  
                                   manufacturer: Fujitsu
                                   screen: 
                                    size: "14\""
                                    resolution: 1920x1080
                                    type: blyszczaca
                                    touchscreen: tak
                                   processor: 
                                    name: "intel i7"
                                    physical_cores: 8
                                    clock_speed: 1900
                                   ram: 24GB
                                   disc: 
                                    storage: 500GB
                                    type: HDD
                                   graphic_card: 
                                    name: "intel HD Graphics 520"
                                    memory: 1GB
                                   os: "brak systemu"
                                   disc_reader: "Blu-Ray"
                                """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with values surrounded by '"() {
        given:
        def fileContent = """\
                                laptops: 
                                 laptop:  
                                   manufacturer: Fujitsu
                                   screen: 
                                    size: '14\"'
                                    resolution: 1920x1080
                                    type: blyszczaca
                                    touchscreen: tak
                                   processor: 
                                    name: 'intel i7'
                                    physical_cores: 8
                                    clock_speed: 1900
                                   ram: 24GB
                                   disc: 
                                    storage: 500GB
                                    type: HDD
                                   graphic_card: 
                                    name: 'intel HD Graphics 520'
                                    memory: 1GB
                                   os: 'brak systemu'
                                   disc_reader: 'Blu-Ray'
                                """.stripIndent()
        and:
        def expectedComputer = sampleComputer(
                manufacturer: sampleManufacturer(value: "Fujitsu"),
                screen: sampleScreen(
                        size: "14\"",
                        resolution: "1920x1080",
                        type: "blyszczaca",
                        touchscreen: "tak"
                ),
                processor: sampleProcessor(
                        name: "intel i7",
                        physicalCores: "8",
                        clockSpeed: "1900"
                ),
                ram: sampleRam(value: "24GB"),
                disc: sampleDisc(
                        storage: "500GB",
                        type: "HDD"
                ),
                graphicCard: sampleGraphicCard(
                        name: "intel HD Graphics 520",
                        memory: "1GB",
                ),
                operationSystem: sampleOperationSystem(value: "brak systemu"),
                discReader: sampleDiscReader(value: "Blu-Ray")
        )
        when:
        def computers = parser.parseFrom(fileContent)
        then:
        computers.size() == 1
        assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer which has multiline value"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "
                                        
                                    14\"
                                        
                                    "
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "\n\n14\"\n\n",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer which has multiline unsafe value"() {
        given:
        def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "
                                        
                                    processor:
                                        
                                    "
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
        def expectedComputer = sampleComputer(
                manufacturer: sampleManufacturer(value: "Fujitsu"),
                screen: sampleScreen(
                        size: "\n\n14\"\n\n",
                        resolution: "1920x1080",
                        type: "blyszczaca",
                        touchscreen: "tak"
                ),
                processor: sampleProcessor(
                        name: "intel i7",
                        physicalCores: "8",
                        clockSpeed: "1900"
                ),
                ram: sampleRam(value: "24GB"),
                disc: sampleDisc(
                        storage: "500GB",
                        type: "HDD"
                ),
                graphicCard: sampleGraphicCard(
                        name: "intel HD Graphics 520",
                        memory: "1GB",
                ),
                operationSystem: sampleOperationSystem(value: "brak systemu"),
                discReader: sampleDiscReader(value: "Blu-Ray")
        )
        when:
        def computers = parser.parseFrom(fileContent)
        then:
        computers.size() == 1
        assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with unsafe value"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "resolution: 14\""
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "resolution: 14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with empty sub fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "14\""
                                        resolution: 
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with empty complex fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with missing sub fields"() {
        given:
        def fileContent = """\
                                laptops: 
                                 laptop:  
                                   manufacturer: Fujitsu
                                   screen: 
                                    size: "14\""
                                    resolution: 1920x1080
                                    type: blyszczaca
                                   processor: 
                                    name: "intel i7"
                                    physical_cores: 8
                                    clock_speed: 1900
                                   ram: 24GB
                                   disc: 
                                    storage: 500GB
                                    type: HDD
                                   graphic_card: 
                                    name: "intel HD Graphics 520"
                                    memory: 1GB
                                   os: "brak systemu"
                                   disc_reader: "Blu-Ray"
                                """.stripIndent()
        and:
        def expectedComputer = sampleComputer(
                manufacturer: sampleManufacturer(value: "Fujitsu"),
                screen: sampleScreen(
                        size: "14\"",
                        resolution: "1920x1080",
                        type: "blyszczaca"
                ),
                processor: sampleProcessor(
                        name: "intel i7",
                        physicalCores: "8",
                        clockSpeed: "1900"
                ),
                ram: sampleRam(value: "24GB"),
                disc: sampleDisc(
                        storage: "500GB",
                        type: "HDD"
                ),
                graphicCard: sampleGraphicCard(
                        name: "intel HD Graphics 520",
                        memory: "1GB",
                ),
                operationSystem: sampleOperationSystem(value: "brak systemu"),
                discReader: sampleDiscReader(value: "Blu-Ray")
        )
        when:
        def computers = parser.parseFrom(fileContent)
        then:
        computers.size() == 1
        assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with missing simple fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "14\""
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with missing complex fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "14\""
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with mixed sub fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        type: blyszczaca
                                        size: "14\""
                                        resolution: 1920x1080
                                        touchscreen: tak
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with mixed complex fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 8
                                        clock_speed: 1900
                                       screen: 
                                        size: "14\""
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        touchscreen: tak
                                       ram: 24GB
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with vertical gaps between fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:  
                                       manufacturer: Fujitsu
                                       screen: 
                                        size: "14\""
                                        
                                        resolution: 1920x1080
                                        type: blyszczaca
                                        
                                        touchscreen: tak
                                       processor: 
                                       
                                        name: "intel i7"
                                        physical_cores: 8
                                        
                                        clock_speed: 1900
                                       ram: 24GB
                                       
                                       disc: 
                                        storage: 500GB
                                        type: HDD
                                       graphic_card: 
                                        name: "intel HD Graphics 520"
                                        memory: 1GB
                                       os: "brak systemu"
                                       disc_reader: "Blu-Ray"
                                    """.stripIndent()
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computers list when file content has multiple computers with empty fields"() {
        given:
            def fileContent = """\
                                    laptops: 
                                     laptop:
                                      - 
                                       manufacturer: Dell
                                       screen: 
                                        size: "12\\""
                                        type: matowa
                                        touchscreen: nie
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 4
                                        clock_speed: 2800
                                       ram: 8GB
                                       disc: 
                                        storage: 240GB
                                        type: SSD
                                       graphic_card: 
                                        name: "intel HD Graphics 4000"
                                        memory: 1GB
                                       os: "Windows 7 Home"
                                      - 
                                       manufacturer: Asus
                                       screen: 
                                        size: "14\\""
                                        resolution: 1600x900
                                        type: matowa
                                        touchscreen: nie
                                       processor: 
                                        name: "intel i5"
                                        physical_cores: 4
                                       ram: 16GB
                                       disc: 
                                        storage: 120GB
                                        type: SSD
                                       graphic_card: 
                                        name: "intel HD Graphics 5000"
                                        memory: 1GB
                                       disc_reader: brak    
                                      - 
                                       manufacturer: Huawei
                                       screen: 
                                        size: "13\\""
                                        type: matowa
                                        touchscreen: nie
                                       processor: 
                                        name: "intel i7"
                                        physical_cores: 4
                                        clock_speed: 2400
                                       ram: 12GB
                                       disc: 
                                        storage: 24GB
                                        type: HDD
                                       graphic_card: 
                                        name: "NVIDIA GeForce GTX 1050"
                                       disc_reader: brak                       
                                      """.stripIndent()
        and:
            def expectedComputers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Dell"),
                            screen: sampleScreen(
                                    size: "12\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2800"
                            ),
                            ram: sampleRam(value: "8GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 4000",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "Windows 7 Home")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Asus"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1600x900",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i5",
                                    physicalCores: "4",
                            ),
                            ram: sampleRam(value: "16GB"),
                            disc: sampleDisc(
                                    storage: "120GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 5000",
                                    memory: "1GB",
                            ),
                            discReader: sampleDiscReader(value: "brak")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 3
            assertComputer(computers[0]).isDataSame(expectedComputers[0])
            assertComputer(computers[1]).isDataSame(expectedComputers[1])
            assertComputer(computers[2]).isDataSame(expectedComputers[2])
    }

    def "should parse to empty computers list when file content is empty"() {
        expect:
            parser.parseFrom("").size() == 0
    }

    def "should parse to empty computers list when file content has empty main tag"() {
        expect:
            parser.parseFrom("laptops:").size() == 0
    }

    def "should parse to empty computers list when multiline file content is empty"() {
        given:
        def fileContent = """\
                              
                              
                              """
                .stripIndent()
        when:
        def parsed = parser.parseFrom(fileContent)
        then:
        parsed.size() == 0
    }

    def "should parse to computer which has only empty fields when file content has empty element main tag"() {
        given:
            def fileContent = """\
                                laptops:
                                    laptop:
                              """
        and:
            def expectedComputer = sampleComputer()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computers list which contains only computers with empty fields when file content has empty elements main tags"() {
        given:
            def fileContent = """\
                                 laptops:
                                    laptop:
                                        -
                                        -""".stripIndent()
        and:
            def expectedComputer = sampleComputer()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
            assertComputer(computers[0]).isDataSame(expectedComputer)
            assertComputer(computers[1]).isDataSame(expectedComputer)
    }
}

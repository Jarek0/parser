package pl.edu.pollub.parser.domain

import pl.edu.pollub.parser.domain.csv.CsvFileComputersParser
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

class TxtFileComputerParserSpec extends Specification {

    @Subject
    def parser = new CsvFileComputersParser()

    def "should parse to computer when file content has single line without empty fields"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu;Blu-Ray;"
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

    def "should parse to file content when computers list has single computer"() {
        given:
            def computers = [
                    sampleComputer(
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
            ]
        and:
            def expectedFileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu;Blu-Ray;"
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            expectedFileContent == fileContent
    }

    def "should parse to computer when file content has single line with empty fields"() {
        given:
            def fileContent = "Huawei;13\";;matowa;nie;intel i7;4;2400;12GB;24GB;HDD;NVIDIA GeForce GTX 1050;;;brak;"
        and:
            def expectedComputer = sampleComputer(
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
                            storage: "24GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                    discReader: sampleDiscReader(value: "brak")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to file content when computers list has single computer with empty fields"() {
        given:
            def computers = [
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
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
        and:
            def expectedFileContent = "Huawei;13\";;matowa;nie;intel i7;4;2400;12GB;24GB;HDD;NVIDIA GeForce GTX 1050;;;brak;"
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            expectedFileContent == fileContent
    }

    def "should parse to computers list when file content has multiple lines with empty fields"() {
        given:
            def fileContent = """\
                                  Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;
                                  Asus;14";1600x900;matowa;nie;intel i5;4;;16GB;120GB;SSD;intel HD Graphics 5000;1GB;;brak;"""
                    .stripIndent()
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
                    )
            ]
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
            assertComputer(computers[0]).isDataSame(expectedComputers[0])
            assertComputer(computers[1]).isDataSame(expectedComputers[1])
    }

    def "should parse to multiline file content when computers list has multiple computers with empty fields"() {
        given:
            def computers = [
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
                    )
            ]
        and:
            def expectedFileContent = """\
                                      Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;
                                      Asus;14";1600x900;matowa;nie;intel i5;4;;16GB;120GB;SSD;intel HD Graphics 5000;1GB;;brak;"""
                    .stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            expectedFileContent == fileContent
    }

    def "should parse to empty computers list when file content is empty"() {
        expect:
            parser.parseFrom("").size() == 0
    }

    def "should parse to empty file content when computers list is empty"() {
        expect:
            parser.parseFrom([]) == ""
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

    def "should parse to computer which has only empty fields when file content has single line which contains only delimiters"() {
        given:
            def fileContent = ";;;;;;;;;;;;;;;"
        and:
            def expectedComputer = sampleComputer()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to file content which contains only delimiters when computers list has single computer which has only empty fields"() {
        expect:
            parser.parseFrom([sampleComputer()]) == ";;;;;;;;;;;;;;;"
    }

    def "should parse to computers list which contains only computers with empty fields when file content has multiple lines which contains only delimiters"() {
        given:
            def fileContent = """\
                              ;;;;;;;;;;;;;;;
                              ;;;;;;;;;;;;;;;"""
                    .stripIndent()
        and:
            def expectedComputer = sampleComputer()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
            assertComputer(computers[0]).isDataSame(expectedComputer)
            assertComputer(computers[1]).isDataSame(expectedComputer)
    }

    def "should parse to multiline file content which contains only delimiters when computers list has multiple computers which have only empty fields"() {
        expect:
            parser.parseFrom([sampleComputer(), sampleComputer()]) == """\
                              ;;;;;;;;;;;;;;;
                              ;;;;;;;;;;;;;;;"""
                    .stripIndent()
    }

    def "should parse to one computer when file content has unsafe value"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;null;500GB;HDD;intel HD Graphics 520;"
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
                    ram: sampleRam(value: "null"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                    )
            )
        when:
            def parsed = parser.parseFrom(fileContent)
        then:
            parsed.size() == 1
            assertComputer(parsed[0]).isDataSame(expectedComputer)
    }

    def "should parse to two computers with their own identity when file content has two identical lines"() {
        given:
            def fileContent = """\
                                  Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;
                                  Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;"""
                    .stripIndent()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
    }

    def "should parse to multiline file content witch contains two identical lines when computers list has two identical computers"() {
        given:
            def computers = [
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
                    )
            ]
        and:
            def expectedFileContent = """\
                                          Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;
                                          Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;"""
                    .stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            fileContent == expectedFileContent
    }

    def "should parse to computer with empty fields when file content has single file which has less fields count than expected"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;"
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
                    )
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single line which is not ended with delimiter"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu"
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
                            memory: "1GB"
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer with empty fields when file content has single file which has more fields count than expected"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu;Blu-Ray;additionalField;additionalField;"
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

}

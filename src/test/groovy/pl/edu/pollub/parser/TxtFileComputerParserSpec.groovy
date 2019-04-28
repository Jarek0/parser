package pl.edu.pollub.parser

import pl.edu.pollub.parser.domain.TxtFileComputerParser
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.application.SampleComputer.sampleComputer

class TxtFileComputerParserSpec extends Specification {

    @Subject
    TxtFileComputerParser parser = new TxtFileComputerParser()

    def "should return one computer when file content has single line without empty fields"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu;Blu-Ray;"
            def expectedComputer = sampleComputer(
                    manufacturer: "Fujitsu",
                    matrixSize: "14\"",
                    resolution: "1920x1080",
                    matrixType: "blyszczaca",
                    touchscreen: "tak",
                    processor: "intel i7",
                    coresCount: "8",
                    timing: "1900",
                    ram: "24GB",
                    discCapacity: "500GB",
                    discType: "HDD",
                    graphicCard: "intel HD Graphics 520",
                    graphicCardMemory: "1GB",
                    operationSystem: "brak systemu",
                    opticalDrive: "Blu-Ray"
            )
        when:
            def result = parser.parse([fileContent])
        then:
            result.size() == 1
            result.contains(expectedComputer)
    }

    def "should return one computer when file content has single line with empty fields"() {
        given:
            def fileContent = "Huawei;13\";;matowa;nie;intel i7;4;2400;12GB;24GB;HDD;NVIDIA GeForce GTX 1050;;;brak;"
        and:
            def expectedComputer = sampleComputer(
                    manufacturer: "Huawei",
                    matrixSize: "13\"",
                    matrixType: "matowa",
                    touchscreen: "nie",
                    processor: "intel i7",
                    coresCount: "4",
                    timing: "2400",
                    ram: "12GB",
                    discCapacity: "24GB",
                    discType: "HDD",
                    graphicCard: "NVIDIA GeForce GTX 1050",
                    opticalDrive: "brak"
            )
        when:
            def result = parser.parse([fileContent])
        then:
            result.size() == 1
            result.contains(expectedComputer)
    }

    def "should return computers list when file content has multiple lines with empty fields"() {
        given:
            def fileContent = """\
                              Dell;12";;matowa;nie;intel i7;4;2800;8GB;240GB;SSD;intel HD Graphics 4000;1GB;Windows 7 Home;;
                              Asus;14";1600x900;matowa;nie;intel i5;4;;16GB;120GB;SSD;intel HD Graphics 5000;1GB;;brak;"""
        and:
            def expectedComputers = [
                    sampleComputer(
                            manufacturer: "Dell",
                            matrixSize: "12\"",
                            matrixType: "matowa",
                            touchscreen: "nie",
                            processor: "intel i7",
                            coresCount: "4",
                            timing: "2800",
                            ram: "8GB",
                            discCapacity: "240GB",
                            discType: "SSD",
                            graphicCard: "intel HD Graphics 4000",
                            graphicCardMemory: "1GB",
                            operationSystem: "Windows 7 Home"
                    ),
                    sampleComputer(
                            manufacturer: "Asus",
                            matrixSize: "14\"",
                            resolution: "1600x900",
                            matrixType: "matowa",
                            touchscreen: "nie",
                            processor: "intel i5",
                            coresCount: "4",
                            ram: "16GB",
                            discCapacity: "120GB",
                            discType: "SSD",
                            graphicCard: "intel HD Graphics 5000",
                            graphicCardMemory: "1GB"
                    )
            ]
        when:
            def result = parser.parse(fileContent.stripIndent().split("\n").toList())
        then:
            result.size() == 2
            result == expectedComputers
    }

    def "should return empty computers list when file content is empty"() {
        given:
            def fileContent = ""
        when:
            def result = parser.parse([fileContent])
        then:
            result.size() == 0
    }

    def "should return one computer which has only default values when file content has single which contains only delimiters"() {
        given:
            def fileContent = ";;;;;;;;;;;;;;;"
        and:
            def expectedComputer = sampleComputer()
        when:
            def result = parser.parse([fileContent])
        then:
            result.size() == 1
            result.contains(expectedComputer)
    }

    def "should return one computer which has default values in places where line from file content has missed value"() {
        given:
        def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;"
        def expectedComputer = sampleComputer(
                manufacturer: "Fujitsu",
                matrixSize: "14\"",
                resolution: "1920x1080",
                matrixType: "blyszczaca",
                touchscreen: "tak",
                processor: "intel i7",
                coresCount: "8",
                timing: "1900",
                ram: "24GB",
                discCapacity: "500GB",
                discType: "HDD",
                graphicCard: "intel HD Graphics 520"
        )
        when:
        def result = parser.parse([fileContent])
        then:
        result.size() == 1
        result.contains(expectedComputer)
    }

}

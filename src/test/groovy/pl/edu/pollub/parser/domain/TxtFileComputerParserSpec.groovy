package pl.edu.pollub.parser.domain

import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.domain.assertions.ComputerAssert.assertComputer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleComputer

class TxtFileComputerParserSpec extends Specification {

    @Subject
    TxtFileComputerParser parser = new TxtFileComputerParser()

    def "should parse to computer when file content has single line without empty fields"() {
        given:
            def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu;Blu-Ray;"
        and:
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
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }
    
    def "should parse to file content when computers list has single computer"() {
        given:
            def computers = [
                    sampleComputer(
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
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to file content when computers list has single computer with empty fields"() {
        given:
            def computers = [
                    sampleComputer(
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
                            graphicCardMemory: "1GB",
                            opticalDrive: "brak"
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
                            graphicCardMemory: "1GB",
                            opticalDrive: "brak"
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

    def "should parse to computer which has only empty fields when file content has miltple lines which contains only delimiters"() {
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
                    manufacturer: "Fujitsu",
                    matrixSize: "14\"",
                    resolution: "1920x1080",
                    matrixType: "blyszczaca",
                    touchscreen: "tak",
                    processor: "intel i7",
                    coresCount: "8",
                    timing: "1900",
                    ram: "null",
                    discCapacity: "500GB",
                    discType: "HDD",
                    graphicCard: "intel HD Graphics 520"
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

    def "should parse to multiline file content witch conteins two identical lines when computers list has two identical computers"() {
        given:
            def computers = [
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
        def computers = parser.parseFrom(fileContent)
        then:
        computers.size() == 1
        assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single line which is not endend with delimiter"() {
        given:
        def fileContent = "Fujitsu;14\";1920x1080;blyszczaca;tak;intel i7;8;1900;24GB;500GB;HDD;intel HD Graphics 520;1GB;brak systemu"
        and:
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
                operationSystem: "brak systemu"
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
        def computers = parser.parseFrom(fileContent)
        then:
        computers.size() == 1
        assertComputer(computers[0]).isDataSame(expectedComputer)
    }

}

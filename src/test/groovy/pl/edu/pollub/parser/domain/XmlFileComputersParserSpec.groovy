package pl.edu.pollub.parser.domain

import pl.edu.pollub.parser.domain.assertions.XmlAssert
import pl.edu.pollub.parser.domain.xml.XmlFileComputersParser
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

class XmlFileComputersParserSpec extends Specification {

    XmlAssert xmlAssert = new XmlAssert()

    @Subject
    def parser = new XmlFileComputersParser()

    def "should parse to computer when file content has single computer"() {
        given:
            def fileContent = """\
                              <laptops>
                                   <laptop>
                                       <manufacturer>Fujitsu</manufacturer>
                                       <screen>
                                           <size>14"</size>
                                           <resolution>1920x1080</resolution>
                                           <type>blyszczaca</type>
                                           <touchscreen>tak</touchscreen>
                                       </screen>
                                       <processor>
                                           <name>intel i7</name>
                                           <physical_cores>8</physical_cores>
                                           <clock_speed>1900</clock_speed>
                                       </processor>
                                       <ram>24GB</ram>
                                       <disc>
                                           <storage>500GB</storage>
                                           <type>HDD</type>
                                       </disc>
                                       <graphic_card>
                                           <name>intel HD Graphics 520</name>
                                           <memory>1GB</memory>
                                       </graphic_card>
                                       <os>brak systemu</os>
                                       <disc_reader>Blu-Ray</disc_reader>
                                   </laptop>
                              </laptops>                            
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
            def expectedFileContent = """\
                                      <laptops>
                                          <laptop>
                                              <manufacturer>Fujitsu</manufacturer>
                                              <screen>
                                                  <size>14"</size>
                                                  <resolution>1920x1080</resolution>
                                                  <type>blyszczaca</type>
                                                  <touchscreen>tak</touchscreen>
                                              </screen>
                                              <processor>
                                                  <name>intel i7</name>
                                                  <physical_cores>8</physical_cores>
                                                  <clock_speed>1900</clock_speed>
                                              </processor>
                                              <ram>24GB</ram>
                                              <disc>
                                                  <storage>500GB</storage>
                                                  <type>HDD</type>
                                              </disc>
                                              <graphic_card>
                                                  <name>intel HD Graphics 520</name>
                                                  <memory>1GB</memory>
                                              </graphic_card>
                                              <os>brak systemu</os>
                                              <disc_reader>Blu-Ray</disc_reader>
                                          </laptop>
                                      </laptops>""".stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            xmlAssert.diff(expectedFileContent, fileContent)
    }

    def "should parse to computer when file content has single computer with broken sub fields"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1920x1080
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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

    def "should parse to computer when file content has single computer with broken simple fields"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1920x1080</resolution>
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
            then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with broken complex fields"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1920x1080</resolution>
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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

    def "should parse to computer when file content has single computer with empty sub fields"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution></resolution>
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os></os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 1
            assertComputer(computers[0]).isDataSame(expectedComputer)
    }

    def "should parse to computer when file content has single computer with empty simple fields"() {
        given:
            def fileContent = """\
                                          <laptops>
                                               <laptop>
                                                   <manufacturer>Fujitsu</manufacturer>
                                                   <screen>
                                                       <size>14"</size>
                                                       <resolution>1920x1080</resolution>
                                                       <type>blyszczaca</type>
                                                       <touchscreen>tak</touchscreen>
                                                   </screen>
                                                   <processor>
                                                       <name>intel i7</name>
                                                       <physical_cores>8</physical_cores>
                                                       <clock_speed>1900</clock_speed>
                                                   </processor>
                                                   <ram>24GB</ram>
                                                   <disc>
                                                       <storage>500GB</storage>
                                                       <type>HDD</type>
                                                   </disc>
                                                   <graphic_card>
                                                       <name>intel HD Graphics 520</name>
                                                       <memory>1GB</memory>
                                                   </graphic_card>
                                                   <os></os>
                                                   <disc_reader>Blu-Ray</disc_reader>
                                               </laptop>
                                          </laptops>                            
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
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen></screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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
                                      <laptops>
                                           <laptop>
                                               <manufacturer>Fujitsu</manufacturer>
                                               <screen>
                                                   <size>14"</size>
                                                   <resolution>1920x1080</resolution>
                                                   <type>blyszczaca</type>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>8</physical_cores>
                                                   <clock_speed>1900</clock_speed>
                                               </processor>
                                               <ram>24GB</ram>
                                               <disc>
                                                   <storage>500GB</storage>
                                                   <type>HDD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>intel HD Graphics 520</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <os>brak systemu</os>
                                               <disc_reader>Blu-Ray</disc_reader>
                                           </laptop>
                                      </laptops>                            
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

    def "should parse to file content when computers list has single computer with missing sub fields"() {
        given:
            def computers = [
                    sampleComputer(
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
            ]
        and:
            def expectedFileContent = """\
                                              <laptops>
                                                  <laptop>
                                                      <manufacturer>Fujitsu</manufacturer>
                                                      <screen>
                                                          <size>14"</size>
                                                          <type>blyszczaca</type>
                                                          <touchscreen>tak</touchscreen>
                                                      </screen>
                                                      <processor>
                                                          <name>intel i7</name>
                                                          <physical_cores>8</physical_cores>
                                                          <clock_speed>1900</clock_speed>
                                                      </processor>
                                                      <ram>24GB</ram>
                                                      <disc>
                                                          <storage>500GB</storage>
                                                          <type>HDD</type>
                                                      </disc>
                                                      <graphic_card>
                                                          <name>intel HD Graphics 520</name>
                                                          <memory>1GB</memory>
                                                      </graphic_card>
                                                      <os>brak systemu</os>
                                                      <disc_reader>Blu-Ray</disc_reader>
                                                  </laptop>
                                              </laptops>""".stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            xmlAssert.diff(expectedFileContent, fileContent)
    }

    def "should parse to computer when file content has single computer with missing simple fields"() {
        given:
        def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1920x1080</resolution>
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                       </laptop>
                                  </laptops>                            
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

    def "should parse to file content when computers list has single computer with missing simple fields"() {
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
                            operationSystem: sampleOperationSystem(value: "brak systemu")
                    )
            ]
        and:
            def expectedFileContent = """\
                                          <laptops>
                                              <laptop>
                                                  <manufacturer>Fujitsu</manufacturer>
                                                  <screen>
                                                      <size>14"</size>
                                                      <resolution>1920x1080</resolution>
                                                      <type>blyszczaca</type>
                                                      <touchscreen>tak</touchscreen>
                                                  </screen>
                                                  <processor>
                                                      <name>intel i7</name>
                                                      <physical_cores>8</physical_cores>
                                                      <clock_speed>1900</clock_speed>
                                                  </processor>
                                                  <ram>24GB</ram>
                                                  <disc>
                                                      <storage>500GB</storage>
                                                      <type>HDD</type>
                                                  </disc>
                                                  <graphic_card>
                                                      <name>intel HD Graphics 520</name>
                                                      <memory>1GB</memory>
                                                  </graphic_card>
                                                  <os>brak systemu</os>
                                              </laptop>
                                          </laptops>""".stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            xmlAssert.diff(expectedFileContent, fileContent)
    }

    def "should parse to computer when file content has single computer with missing complex fields"() {
        given:
            def fileContent = """\
                                      <laptops>
                                           <laptop>
                                               <manufacturer>Fujitsu</manufacturer>
                                               <screen>
                                                   <size>14"</size>
                                                   <resolution>1920x1080</resolution>
                                                   <type>blyszczaca</type>
                                                   <touchscreen>tak</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>8</physical_cores>
                                                   <clock_speed>1900</clock_speed>
                                               </processor>
                                               <ram>24GB</ram>
                                               <graphic_card>
                                                   <name>intel HD Graphics 520</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <os>brak systemu</os>
                                               <disc_reader>Blu-Ray</disc_reader>
                                           </laptop>
                                      </laptops>                            
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

    def "should parse to file content when computers list has single computer with missing complex fields"() {
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
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    )
            ]
        and:
            def expectedFileContent = """\
                                          <laptops>
                                              <laptop>
                                                  <manufacturer>Fujitsu</manufacturer>
                                                  <screen>
                                                      <size>14"</size>
                                                      <resolution>1920x1080</resolution>
                                                      <type>blyszczaca</type>
                                                      <touchscreen>tak</touchscreen>
                                                  </screen>
                                                  <processor>
                                                      <name>intel i7</name>
                                                      <physical_cores>8</physical_cores>
                                                      <clock_speed>1900</clock_speed>
                                                  </processor>
                                                  <ram>24GB</ram>
                                                  <graphic_card>
                                                      <name>intel HD Graphics 520</name>
                                                      <memory>1GB</memory>
                                                  </graphic_card>
                                                  <os>brak systemu</os>
                                                  <disc_reader>Blu-Ray</disc_reader>
                                              </laptop>
                                          </laptops>""".stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            xmlAssert.diff(expectedFileContent, fileContent)
    }

    def "should parse to computer when file content has single computer with mixed sub fields"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <type>blyszczaca</type>
                                               <size>14"</size>
                                               <resolution>1920x1080</resolution>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <screen>
                                               <size>14"</size>
                                               <type>blyszczaca</type>
                                               <resolution>1920x1080</resolution>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                       </laptop>
                                  </laptops>                            
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
                              <laptops>
                                   <laptop>
                                       <manufacturer>Fujitsu</manufacturer>
                                       
                                       <screen>
                                           <size>14"</size>
                                           <resolution>1920x1080</resolution>
                                           <type>blyszczaca</type>
                                           <touchscreen>tak</touchscreen>
                                       </screen>
                                       
                                       <processor>
                                           <name>intel i7</name>
                                           <physical_cores>8</physical_cores>
                                           <clock_speed>1900</clock_speed>
                                       </processor>
                                       
                                       <ram>24GB</ram>
                                       <disc>
                                           <storage>500GB</storage>
                                           <type>HDD</type>
                                       </disc>
                                       <graphic_card>
                                           <name>intel HD Graphics 520</name>
                                           <memory>1GB</memory>
                                       </graphic_card>
                                       <os>brak systemu</os>
                                       <disc_reader>Blu-Ray</disc_reader>
                                   </laptop>
                              </laptops>                            
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

    def "should parse to computer when file content has single computer with vertical gaps between sub fields"() {
        given:
        def fileContent = """\
                              <laptops>
                                   <laptop>
                                       <manufacturer>Fujitsu</manufacturer>
                                       <screen>
                                           <size>14"</size>
                                           
                                           <resolution>1920x1080</resolution>
                                           <type>blyszczaca</type>
                                           
                                           <touchscreen>tak</touchscreen>
                                       </screen>
                                       <processor>
                                           <name>intel i7</name>
                                           
                                           <physical_cores>8</physical_cores>
                                           <clock_speed>1900</clock_speed>
                                       </processor>
                                       
                                       <ram>24GB</ram>
                                       <disc>
                                           <storage>500GB</storage>
                                           <type>HDD</type>
                                       </disc>
                                       <graphic_card>
                                           <name>intel HD Graphics 520</name>
                                           <memory>1GB</memory>
                                       </graphic_card>
                                       <os>brak systemu</os>
                                       <disc_reader>Blu-Ray</disc_reader>
                                   </laptop>
                              </laptops>                            
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
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Dell</manufacturer>
                                           <screen>
                                               <size>12"</size>
                                               <type>matowa</type>
                                               <touchscreen>nie</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>4</physical_cores>
                                               <clock_speed>2800</clock_speed>
                                           </processor>
                                           <ram>8GB</ram>
                                           <disc>
                                               <storage>240GB</storage>
                                               <type>SSD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 4000</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>Windows 7 Home</os>
                                       </laptop>
                                       <laptop>
                                           <manufacturer>Asus</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1600x900</resolution>
                                               <type>matowa</type>
                                               <touchscreen>nie</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i5</name>
                                               <physical_cores>4</physical_cores>
                                           </processor>
                                           <ram>16GB</ram>
                                           <disc>
                                               <storage>120GB</storage>
                                               <type>SSD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 5000</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <disc_reader>brak</disc_reader>
                                       </laptop>
                                       <laptop>
                                           <manufacturer>Huawei</manufacturer>
                                           <screen>
                                               <size>13"</size>
                                               <type>matowa</type>
                                               <touchscreen>nie</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>4</physical_cores>
                                               <clock_speed>2400</clock_speed>
                                           </processor>
                                           <ram>12GB</ram>
                                           <disc>
                                               <storage>240GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>NVIDIA GeForce GTX 1050</name>
                                           </graphic_card>
                                           <disc_reader>brak</disc_reader>
                                       </laptop>
                                  </laptops>                            
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

    def "should parse to file content when computers list has multiple computers with empty fields"() {
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
        and:
            def expectedFileContent = """\
                                      <laptops>
                                           <laptop>
                                               <manufacturer>Dell</manufacturer>
                                               <screen>
                                                   <size>12"</size>
                                                   <type>matowa</type>
                                                   <touchscreen>nie</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>4</physical_cores>
                                                   <clock_speed>2800</clock_speed>
                                               </processor>
                                               <ram>8GB</ram>
                                               <disc>
                                                   <storage>240GB</storage>
                                                   <type>SSD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>intel HD Graphics 4000</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <os>Windows 7 Home</os>
                                           </laptop>
                                           <laptop>
                                               <manufacturer>Asus</manufacturer>
                                               <screen>
                                                   <size>14"</size>
                                                   <resolution>1600x900</resolution>
                                                   <type>matowa</type>
                                                   <touchscreen>nie</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i5</name>
                                                   <physical_cores>4</physical_cores>
                                               </processor>
                                               <ram>16GB</ram>
                                               <disc>
                                                   <storage>120GB</storage>
                                                   <type>SSD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>intel HD Graphics 5000</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <disc_reader>brak</disc_reader>
                                           </laptop>
                                           <laptop>
                                               <manufacturer>Huawei</manufacturer>
                                               <screen>
                                                   <size>13"</size>
                                                   <type>matowa</type>
                                                   <touchscreen>nie</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>4</physical_cores>
                                                   <clock_speed>2400</clock_speed>
                                               </processor>
                                               <ram>12GB</ram>
                                               <disc>
                                                   <storage>240GB</storage>
                                                   <type>HDD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>NVIDIA GeForce GTX 1050</name>
                                               </graphic_card>
                                               <disc_reader>brak</disc_reader>
                                           </laptop>
                                      </laptops>                            
                                      """.stripIndent()
        when:
            def fileContent = parser.parseFrom(computers)
        then:
            xmlAssert.diff(fileContent, expectedFileContent)
    }

    def "should parse to empty computers list when file content is empty"() {
        expect:
            parser.parseFrom("").size() == 0
    }

    def "should parse to empty computers list when file content has empty main tag"() {
        expect:
            parser.parseFrom("<laptops></laptops>").size() == 0
    }

    def "should parse to empty computers list when file content has single computer with broken element main tag"() {
        given:
            def fileContent = """\
                                  <laptops>
                                       <laptop>
                                           <manufacturer>Fujitsu</manufacturer>
                                           <screen>
                                               <size>14"</size>
                                               <resolution>1920x1080</resolution>
                                               <type>blyszczaca</type>
                                               <touchscreen>tak</touchscreen>
                                           </screen>
                                           <processor>
                                               <name>intel i7</name>
                                               <physical_cores>8</physical_cores>
                                               <clock_speed>1900</clock_speed>
                                           </processor>
                                           <ram>24GB</ram>
                                           <disc>
                                               <storage>500GB</storage>
                                               <type>HDD</type>
                                           </disc>
                                           <graphic_card>
                                               <name>intel HD Graphics 520</name>
                                               <memory>1GB</memory>
                                           </graphic_card>
                                           <os>brak systemu</os>
                                           <disc_reader>Blu-Ray</disc_reader>
                                  </laptops>                            
                                  """.stripIndent()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 0
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
            def fileContent = "<laptops><laptop></laptop></laptops>"
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
                                 <laptops>
                                    <laptop></laptop>
                                    <laptop></laptop>
                                 </laptops>""".stripIndent()
        and:
            def expectedComputer = sampleComputer()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
            assertComputer(computers[0]).isDataSame(expectedComputer)
            assertComputer(computers[1]).isDataSame(expectedComputer)
    }

    def "should parse to two computers with their own identity when file content has two identical elements main tags"() {
        given:
            def fileContent = """\
                                 <laptops>
                                           <laptop>
                                               <manufacturer>Dell</manufacturer>
                                               <screen>
                                                   <size>12"</size>
                                                   <type>matowa</type>
                                                   <touchscreen>nie</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>4</physical_cores>
                                                   <clock_speed>2800</clock_speed>
                                               </processor>
                                               <ram>8GB</ram>
                                               <disc>
                                                   <storage>240GB</storage>
                                                   <type>SSD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>intel HD Graphics 4000</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <os>Windows 7 Home</os>
                                           </laptop>
                                           <laptop>
                                               <manufacturer>Dell</manufacturer>
                                               <screen>
                                                   <size>12"</size>
                                                   <type>matowa</type>
                                                   <touchscreen>nie</touchscreen>
                                               </screen>
                                               <processor>
                                                   <name>intel i7</name>
                                                   <physical_cores>4</physical_cores>
                                                   <clock_speed>2800</clock_speed>
                                               </processor>
                                               <ram>8GB</ram>
                                               <disc>
                                                   <storage>240GB</storage>
                                                   <type>SSD</type>
                                               </disc>
                                               <graphic_card>
                                                   <name>intel HD Graphics 4000</name>
                                                   <memory>1GB</memory>
                                               </graphic_card>
                                               <os>Windows 7 Home</os>
                                           </laptop>
                                 </laptops>"""
                    .stripIndent()
        when:
            def computers = parser.parseFrom(fileContent)
        then:
            computers.size() == 2
    }

    def "should parse to computer when file content is not formated and has single computer"() {
        given:
        def fileContent = """\
                              <laptops><laptop><manufacturer>Fujitsu</manufacturer><screen>
                                           <size>14"</size><resolution>1920x1080</resolution><type>blyszczaca</type>
                                           <touchscreen>tak</touchscreen>
                                       </screen>
                                       <processor><name>intel i7</name><physical_cores>8</physical_cores><clock_speed>1900</clock_speed>
                                       </processor><ram>24GB</ram><disc><storage>500GB</storage><type>HDD</type>
                                       </disc><graphic_card><name>intel HD Graphics 520</name><memory>1GB</memory>
                                       </graphic_card>
                                       <os>brak systemu</os><disc_reader>Blu-Ray</disc_reader></laptop></laptops>                            
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
}

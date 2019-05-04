package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.parser.domain.*
import java.lang.StringBuilder

class ComputersXmlStructureBuilder(private val computers: Collection<Computer>) {

    fun build(): String {
        val builder = StringBuilder()
        val tagsBuilder = ComputersTagBuilder(builder)
        for(computer in computers) {
            tagsBuilder.withComputer(computer)
        }
        tagsBuilder.build().appendTag()
        return builder.toString()
    }

}

class ComputersTagBuilder(private val builder: StringBuilder) {

    private val computersTags: MutableList<ComplexField> = mutableListOf()

    fun withComputer(computer: Computer) {
        val computerTag = ComputerTagBuilder(builder)
                .withManufacturer(computer.manufacturer)
                .withScreen(computer.screen)
                .withProcessor(computer.processor)
                .withRam(computer.ram)
                .withDisc(computer.disc)
                .withGraphicCard(computer.graphicCard)
                .withOperationSystem(computer.operationSystem)
                .withDiscReader(computer.discReader)
                .build()
        computersTags.add(computerTag)
    }

    fun build() = ComplexField(builder, Tag.COMPUTERS, *computersTags.toTypedArray())

}

class ComputerTagBuilder(private val builder: StringBuilder) {

    lateinit var manufacturerTag: SimpleField
    lateinit var screenTag: ComplexField
    lateinit var processorTag: ComplexField
    lateinit var ramTag: SimpleField
    lateinit var discTag: ComplexField
    lateinit var graphicCardTag: ComplexField
    lateinit var operationSystemTag: SimpleField
    lateinit var discReaderTag: SimpleField

    fun withManufacturer(manufacturer: Manufacturer): ComputerTagBuilder {
        manufacturerTag = SimpleField(builder, Tag.MANUFACTURER, manufacturer.value)
        return this
    }

    fun withScreen(screen: Screen): ComputerTagBuilder {
        screenTag = ScreenTagBuilder(builder)
                .withSize(screen.size)
                .withResolution(screen.resolution)
                .withTouchscreen(screen.touchscreen)
                .withType(screen.type)
                .build()
        return this
    }

    fun withProcessor(processor: Processor): ComputerTagBuilder {
        processorTag = ProcessorTagBuilder(builder)
                .withName(processor.name)
                .withPhysicalCores(processor.physicalCores)
                .withClockSpeed(processor.clockSpeed)
                .build()
        return this
    }

    fun withRam(ram: Ram): ComputerTagBuilder {
        ramTag = SimpleField(builder, Tag.RAM, ram.value)
        return this
    }

    fun withDisc(disc: Disc): ComputerTagBuilder {
        discTag = DiscTagBuilder(builder)
                .withStorage(disc.storage)
                .withType(disc.type)
                .build()
        return this
    }

    fun withGraphicCard(graphicCard: GraphicCard): ComputerTagBuilder {
        graphicCardTag = GraphicCardBuilder(builder)
                .withName(graphicCard.name)
                .withMemory(graphicCard.memory)
                .build()
        return this
    }

    fun withOperationSystem(operationSystem: OperationSystem): ComputerTagBuilder {
        operationSystemTag = SimpleField(builder, Tag.OPERATION_SYSTEM, operationSystem.value)
        return this
    }

    fun withDiscReader(discReader: DiscReader): ComputerTagBuilder {
        discReaderTag = SimpleField(builder, Tag.DISC_READER, discReader.value)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, Tag.COMPUTER,
                manufacturerTag,
                screenTag,
                processorTag,
                ramTag,
                discTag,
                graphicCardTag,
                operationSystemTag,
                discReaderTag
        )
    }
}

class ScreenTagBuilder(private val builder: StringBuilder) {

    lateinit var sizeTag: SimpleField
    lateinit var resolutionTag: SimpleField
    lateinit var typeTag: SimpleField
    lateinit var touchscreenTag: SimpleField

    fun withSize(size: String): ScreenTagBuilder {
        sizeTag = SimpleField(builder, Tag.SIZE, size)
        return this
    }

    fun withResolution(resolution: String): ScreenTagBuilder {
        resolutionTag = SimpleField(builder, Tag.RESOLUTION, resolution)
        return this
    }

    fun withType(type: String): ScreenTagBuilder {
        typeTag = SimpleField(builder, Tag.TYPE, type)
        return this
    }

    fun withTouchscreen(touchscreen: String): ScreenTagBuilder {
        touchscreenTag = SimpleField(builder, Tag.TOUCHSCREEN, touchscreen)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, Tag.SCREEN, sizeTag, resolutionTag, typeTag, touchscreenTag)
    }
}

class ProcessorTagBuilder(private val builder: StringBuilder) {

    lateinit var nameTag: SimpleField
    lateinit var physicalCoresTag: SimpleField
    lateinit var clockSpeedTag: SimpleField

    fun withName(name: String): ProcessorTagBuilder {
        nameTag = SimpleField(builder, Tag.NAME, name)
        return this
    }

    fun withPhysicalCores(physicalCores: String): ProcessorTagBuilder {
        physicalCoresTag = SimpleField(builder, Tag.PHYSICAL_CORES, physicalCores)
        return this
    }

    fun withClockSpeed(clockSpeed: String): ProcessorTagBuilder {
        clockSpeedTag = SimpleField(builder, Tag.CLOCK_SPEED, clockSpeed)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, Tag.PROCESSOR, nameTag, physicalCoresTag, clockSpeedTag)
    }
}

class DiscTagBuilder(private val builder: StringBuilder) {

    lateinit var storageTag: SimpleField
    lateinit var typeTag: SimpleField

    fun withStorage(storage: String): DiscTagBuilder {
        storageTag = SimpleField(builder, Tag.STORAGE, storage)
        return this
    }

    fun withType(type: String): DiscTagBuilder {
        typeTag = SimpleField(builder, Tag.TYPE, type)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, Tag.DISC, storageTag, typeTag)
    }

}

class GraphicCardBuilder(private val builder: StringBuilder) {

    lateinit var nameTag: SimpleField
    lateinit var memoryTag: SimpleField

    fun withName(name: String): GraphicCardBuilder {
        nameTag = SimpleField(builder, Tag.NAME, name)
        return this
    }

    fun withMemory(memory: String): GraphicCardBuilder {
        memoryTag = SimpleField(builder, Tag.MEMORY, memory)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, Tag.GRAPHIC_CARD, nameTag, memoryTag)
    }

}
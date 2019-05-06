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

    fun build() = ComplexField(builder, XmlTag.COMPUTERS, *computersTags.toTypedArray())

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
        manufacturerTag = SimpleField(builder, XmlTag.MANUFACTURER, manufacturer.value)
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
        ramTag = SimpleField(builder, XmlTag.RAM, ram.value)
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
        operationSystemTag = SimpleField(builder, XmlTag.OPERATION_SYSTEM, operationSystem.value)
        return this
    }

    fun withDiscReader(discReader: DiscReader): ComputerTagBuilder {
        discReaderTag = SimpleField(builder, XmlTag.DISC_READER, discReader.value)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, XmlTag.COMPUTER,
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
        sizeTag = SimpleField(builder, XmlTag.SIZE, size)
        return this
    }

    fun withResolution(resolution: String): ScreenTagBuilder {
        resolutionTag = SimpleField(builder, XmlTag.RESOLUTION, resolution)
        return this
    }

    fun withType(type: String): ScreenTagBuilder {
        typeTag = SimpleField(builder, XmlTag.TYPE, type)
        return this
    }

    fun withTouchscreen(touchscreen: String): ScreenTagBuilder {
        touchscreenTag = SimpleField(builder, XmlTag.TOUCHSCREEN, touchscreen)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, XmlTag.SCREEN, sizeTag, resolutionTag, typeTag, touchscreenTag)
    }
}

class ProcessorTagBuilder(private val builder: StringBuilder) {

    lateinit var nameTag: SimpleField
    lateinit var physicalCoresTag: SimpleField
    lateinit var clockSpeedTag: SimpleField

    fun withName(name: String): ProcessorTagBuilder {
        nameTag = SimpleField(builder, XmlTag.NAME, name)
        return this
    }

    fun withPhysicalCores(physicalCores: String): ProcessorTagBuilder {
        physicalCoresTag = SimpleField(builder, XmlTag.PHYSICAL_CORES, physicalCores)
        return this
    }

    fun withClockSpeed(clockSpeed: String): ProcessorTagBuilder {
        clockSpeedTag = SimpleField(builder, XmlTag.CLOCK_SPEED, clockSpeed)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, XmlTag.PROCESSOR, nameTag, physicalCoresTag, clockSpeedTag)
    }
}

class DiscTagBuilder(private val builder: StringBuilder) {

    lateinit var storageTag: SimpleField
    lateinit var typeTag: SimpleField

    fun withStorage(storage: String): DiscTagBuilder {
        storageTag = SimpleField(builder, XmlTag.STORAGE, storage)
        return this
    }

    fun withType(type: String): DiscTagBuilder {
        typeTag = SimpleField(builder, XmlTag.TYPE, type)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, XmlTag.DISC, storageTag, typeTag)
    }

}

class GraphicCardBuilder(private val builder: StringBuilder) {

    lateinit var nameTag: SimpleField
    lateinit var memoryTag: SimpleField

    fun withName(name: String): GraphicCardBuilder {
        nameTag = SimpleField(builder, XmlTag.NAME, name)
        return this
    }

    fun withMemory(memory: String): GraphicCardBuilder {
        memoryTag = SimpleField(builder, XmlTag.MEMORY, memory)
        return this
    }

    fun build(): ComplexField {
        return ComplexField(builder, XmlTag.GRAPHIC_CARD, nameTag, memoryTag)
    }

}


abstract class TagValue(open val builder: StringBuilder, open val xmlTag: XmlTag) {

    abstract fun isEmpty(): Boolean

    abstract fun appendValue()

    fun appendTag() {
        if(isEmpty()) return
        builder.appendStartTag(xmlTag)
        appendValue()
        builder.appendEndTag(xmlTag)
    }

}

class SimpleField(override val builder: StringBuilder, override val xmlTag: XmlTag, val value: String): TagValue(builder, xmlTag) {

    override fun isEmpty() = value.isEmpty()

    override fun appendValue() = builder.appendTagValue(xmlTag, value)

}

class ComplexField(override val builder: StringBuilder, override val xmlTag: XmlTag, vararg val value: TagValue) : TagValue(builder, xmlTag) {

    override fun isEmpty() = value.all { it.isEmpty() }

    override fun appendValue() = value.forEach { it.appendTag() }

}
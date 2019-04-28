package pl.edu.pollub.dependencyinjection

import java.io.File

class ComponentScanner(val moduleName: String) {

    val allComponentClasses: Set<Class<*>>

    init {
        allComponentClasses = findComponentClassesInPackage()
                .filter { it.hasAnnotation(Component::class.java) }.toSet()
    }

    private fun findComponentClassesInPackage(): Set<Class<*>> {
        val classLoader = Thread.currentThread().contextClassLoader

        val path = moduleName.replace(SEPARATOR_DOT, SEPARATOR_SLASH)
        val resources = classLoader.getResources(path)

        val dirs = resources.toList()
                .map { File(it.file) }

        return dirs.map { it.findClassesInDirectory(moduleName) }.flatten().toSet()
    }

    private fun Class<*>.hasAnnotation(vararg annotations: Class<*>) = this.annotations.any{annotations.contains(it.annotationClass.java)}

    private fun File.findClassesInDirectory(moduleName: String): Set<Class<*>> {
        return listFiles().fold(setOf()) { foundClasses, subFile ->
            when {
                subFile.isDirectory -> foundClasses + subFile.findClassesInDirectory(subFile.resolveSubModule(moduleName))
                subFile.isClassFile() -> foundClasses + Class.forName(subFile.resolveClassName(moduleName))
                else -> foundClasses
            }
        }
    }

    private fun File.isClassFile() = this.name.endsWith(CLASS_EXTENSION)

    private fun File.resolveSubModule(moduleName: String) = "$moduleName$SEPARATOR_DOT${this.name}"

    private fun File.resolveClassName(moduleName: String) = "$moduleName$SEPARATOR_DOT${this.name}".removeSuffix(CLASS_EXTENSION)

}

const val CLASS_EXTENSION = ".class"
const val SEPARATOR_DOT = "."
const val SEPARATOR_SLASH = "/"
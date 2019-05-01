package pl.edu.pollub.dependencyinjection

import java.lang.IllegalArgumentException
import java.lang.reflect.Parameter

object ApplicationContext {

    private lateinit var allComponentClasses: Set<Class<*>>
    private lateinit var componentsInstances: Set<Any>

    fun initialize(c: Class<*>) {
        val module = c.`package`
        val componentScanner = ComponentScanner(module.name)
        allComponentClasses = componentScanner.allComponentClasses
        val componentsClasses = allComponentClasses
                .validateConstructors()
                .sortedBy { it.constructors.first().parameterCount }
                .toSet()
        componentsInstances = componentsClasses.fold(emptySet()) {
            instantiatedComponents, componentClass -> instantiatedComponents + instantiateComponent(componentClass, instantiatedComponents)
        }
    }


    private fun Set<Class<*>>.validateConstructors(): Set<Class<*>> {
        for(cl in this) {
            if(cl.constructors.size > 1) {
                throw IllegalArgumentException("Class: ${cl.name} has more than one constructor")
            }
        }
        return this
    }

    private fun instantiateComponent(componentClass: Class<*>, instantiatedComponents: Set<Any>): Any {
        val componentClassName = componentClass.name
        val newComponentClass = Class.forName(componentClassName)
        val constructor = newComponentClass.constructors.first()
        val constructorParams = constructor.parameters
        if(constructorParams.isNotEmpty()) {
            val subComponentInstances: List<Any> = instantiateSubComponents(constructorParams, instantiatedComponents)
            return constructor.newInstance(*subComponentInstances.toTypedArray())
        }
        return constructor.newInstance()
    }

    private fun instantiateSubComponents(constructorParams: Array<out Parameter>, instantiatedComponents: Set<Any>): List<Any> {
        return constructorParams.fold(listOf()) { instantiatedSubComponents, constructorParam ->
            val constructorParamClass = constructorParam.type
            val instantiatedSubComponent = findInAlreadyInstantiated(constructorParamClass, instantiatedComponents)
                    ?: instantiateComponent(constructorParamClass, instantiatedComponents)
            instantiatedSubComponents + instantiatedSubComponent
        }
    }

    private fun findInAlreadyInstantiated(componentClass: Class<*>, instantiatedComponents: Set<Any>): Any? {
        return instantiatedComponents.firstOrNull{ componentClass.isAssignableFrom(it::class.java) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(c: Class<T>): T = componentsInstances.first { c.isAssignableFrom(it::class.java) } as T

}

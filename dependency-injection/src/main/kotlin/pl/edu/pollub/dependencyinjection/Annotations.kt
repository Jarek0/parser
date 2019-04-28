package pl.edu.pollub.dependencyinjection

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject(val value: Boolean = true)

@Target(AnnotationTarget.CLASS)
annotation class Component(val value: String = "")
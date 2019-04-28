package pl.edu.pollub.dependencyinjection

object DependencyInjection {

    private var finished: Boolean = false
    private var context = ApplicationContext

    fun start(c: Class<*>) {
        if(!finished) {
            context.initialize(c)
            finished = true
        }
    }

    fun <T> inject(c: Class<T>): T {
        return context.getComponent(c)
    }

}
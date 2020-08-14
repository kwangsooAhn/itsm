package co.brainz.framework.configuration

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationContextProvider : ApplicationContextAware {
    companion object {
        private lateinit var applicationContext: ApplicationContext

        fun getApplicationContext(): ApplicationContext {
            return this.applicationContext
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext
    }
}

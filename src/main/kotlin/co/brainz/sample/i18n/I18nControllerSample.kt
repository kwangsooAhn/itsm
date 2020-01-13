package co.brainz.sample.i18n

import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.slf4j.LoggerFactory
import javax.annotation.Resource
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.beans.factory.annotation.Autowired

@Controller
class I18nControllerSample {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val sampleI18nPage: String = "sample/sampleI18n"

    fun Logging(): Unit {
        logger.info("INFO{ }", "I18nController")
    }

    fun main(args: Array<String>) {
        SpringApplication.run(this::class.java)
    }

    @Autowired
    lateinit var LocaleChangeInterceptor: LocaleChangeInterceptor

    // 테스트 페이지 링크
    @RequestMapping("/sample/sampleI18n")
    public fun sampleI18n(): String {
        return sampleI18nPage
    }

}
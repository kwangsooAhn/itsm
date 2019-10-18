package co.brainz.framework.sample.i18n

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
class I18nController {
    
    companion object {
        private val logger = LoggerFactory.getLogger(I18nController::class.java)
    }
    
    fun Logging() : Unit{
        logger.info("INFO{ }","I18nController")
    }
    
    fun main(args : Array<String>) {
        SpringApplication.run(I18nController::class.java)
    }
    
    @Autowired
    lateinit var LocaleChangeInterceptor : LocaleChangeInterceptor
    
    // 테스트 페이지 링크
    @RequestMapping("/template/i18nTest")
    public fun I18nSample() : String {
        return "i18nTest"
    }
    
}
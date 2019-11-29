package co.brainz.itsm.faq.controller


import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest
import org.springframework.web.servlet.ModelAndView
import org.springframework.ui.Model
import co.brainz.itsm.faq.service.FaqService
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.faq.entity.FaqEntity
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("faqs")
class FaqRestController {
    
    companion object {
        private val logger = LoggerFactory.getLogger(FaqRestController::class.java)
    }

    
    @Autowired
    lateinit var faqService: FaqService
       
    @RequestMapping(path = ["/",""], method = [RequestMethod.GET])
    fun getFaqs(request: HttpServletRequest): List<FaqEntity> {
        return faqService.findAll()
    }
        
    @RequestMapping(path = ["/",""], method = [RequestMethod.POST])
    fun insertFaq(request: HttpServletRequest, @RequestParam("faqGroup") faqGroupParam: String, faq: FaqEntity) {
        logger.debug("POST TEST ======================================================" + faq.faqGroup)
        faqService.save(faq)
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.GET])
    fun getFaq(request: HttpServletRequest, @PathVariable faqId: String): FaqEntity {
        return faqService.findOne(faqId)
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.PUT])
    fun updateFaq(request: HttpServletRequest, @ModelAttribute faq: FaqEntity) {
        
        faqService.save(faq)
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.DELETE])
    fun deleteFaq(request: HttpServletRequest, @PathVariable faqId: String) {
        faqService.delete(faqId)
    }        
}
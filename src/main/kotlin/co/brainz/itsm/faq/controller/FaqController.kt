package co.brainz.itsm.faq.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import org.springframework.web.servlet.ModelAndView
import org.springframework.ui.Model
import co.brainz.itsm.faq.service.FaqService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
@RequestMapping("/faq")
class FaqController {
    
    @Autowired
    lateinit var faqService: FaqService
    
    @RequestMapping(path = ["/list"], method = [RequestMethod.GET])
    fun getFaqList(request: HttpServletRequest, model: Model): ModelAndView {
        model.addAttribute("faqList",faqService.findAll())                 
        return ModelAndView("faq/list")
    }
    
    @RequestMapping(path = ["/"], method = [RequestMethod.POST])
    fun insertFaq(request: HttpServletRequest): ModelAndView {
        return ModelAndView("")
    }
        
    @RequestMapping(path = ["/"], method = [RequestMethod.GET])
    fun getFaqForm(request: HttpServletRequest): ModelAndView {
        return ModelAndView("faqForm")
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.GET])
    fun getFaq(request: HttpServletRequest): ModelAndView {
        return ModelAndView("faqDetail")
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.PUT])
    fun updateFaq(request: HttpServletRequest): ModelAndView {
        return ModelAndView("")
    }
        
    @RequestMapping(path = ["/{faqId}"], method = [RequestMethod.DELETE])
    fun deleteFaq(request: HttpServletRequest): ModelAndView {
        return ModelAndView("")
    }        
}
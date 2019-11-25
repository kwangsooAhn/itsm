package co.brainz.itsm.faq.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import org.springframework.web.servlet.ModelAndView

@RequestMapping("/faq")
class FaqController {
    
    @RequestMapping(path = ["/list"], method = [RequestMethod.GET])
    fun getFaqList(request: HttpServletRequest): ModelAndView {
        return ModelAndView("faqList")
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
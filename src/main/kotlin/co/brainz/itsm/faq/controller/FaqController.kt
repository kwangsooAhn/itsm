package co.brainz.itsm.faq.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import javax.servlet.http.HttpServletRequest
import org.springframework.web.servlet.ModelAndView
import org.springframework.ui.Model
import co.brainz.itsm.faq.service.FaqService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
@RequestMapping("/faqs")
class FaqController {
    
    @Autowired
    lateinit var faqService: FaqService
    
    @RequestMapping(path = ["/listView"], method = [RequestMethod.GET])
    fun getFaqList(request: HttpServletRequest, model: Model): String {
        return "faq/list"
    }

    @RequestMapping(path = ["/edit"], method = [RequestMethod.GET])
    fun getFaqForm(request: HttpServletRequest, model: Model): String {                 
        model.addAttribute("faqDetail",faqService.findOne(request.getParameter("faqId")))
        return "faq/edit"
    }

    @RequestMapping(path = ["/detailView"], method = [RequestMethod.GET])
    fun getFaq(request: HttpServletRequest,model: Model): String {
        model.addAttribute("faqDetail",faqService.findOne(request.getParameter("faqId")))
        return "faq/faq"
    }
         
}
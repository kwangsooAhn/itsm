package co.brainz.itsm.document.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/document")
class DocumentController {
    
    @GetMapping("/list")
    fun getDocList(request: HttpServletRequest, model: Model): String {
        return "document/list"
    }
    
    @GetMapping("/form")
    fun getDocForm(request: HttpServletRequest, model: Model): String {
        //To-do 지원 가능한 언어 목록 가져오기
        
        //To-do 템플릿 정보 가져오기
        return "document/form"
    }
    
    @GetMapping("/edit")
    fun getDocEditor(request: HttpServletRequest, model: Model): String {
        //To-do 컴포넌트 상세 정보 가져오기
        return "document/edit"
    }
}
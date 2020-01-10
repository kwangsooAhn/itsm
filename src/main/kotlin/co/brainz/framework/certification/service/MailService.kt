package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.MailDto
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import javax.mail.internet.MimeMessage

@Component
class MailService(private val javaMailSender: JavaMailSender,
                  private val springTemplateEngine: SpringTemplateEngine) {

    lateinit var mimeMessageHelper: MimeMessageHelper
    lateinit var context: Context
    lateinit var content: String
    lateinit var message: MimeMessage

    fun makeContext(params: Map<String, Any>) {
        context = Context()
        params.forEach{ (key, value) -> context.setVariable(key, value) }
    }

    fun makeTemplateEngine(template: String) {
        content = springTemplateEngine.process(template, context)
    }

    fun makeMimeMessageHelper(mailDto: MailDto) {
        message = javaMailSender.createMimeMessage()
        mimeMessageHelper = MimeMessageHelper(message, true)
        for (email in mailDto.to) mimeMessageHelper.addTo(email)
        for (email in mailDto.cc!!) mimeMessageHelper.addCc(email)
        for (email in mailDto.bcc!!) mimeMessageHelper.addBcc(email)
        mimeMessageHelper.setFrom(mailDto.from)
        mailDto.fromName?.let { mimeMessageHelper.setFrom(mailDto.from, it) }
        mailDto.subject?.let { mimeMessageHelper.setSubject(it) }
        mailDto.content?.let { mimeMessageHelper.setText(it, true) }
    }

    fun send() {
        javaMailSender.send(message)
    }

}

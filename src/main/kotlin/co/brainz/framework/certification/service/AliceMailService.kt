package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.AliceMailDto
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import javax.mail.internet.MimeMessage

@Component
class AliceMailService(
    private val javaMailSender: JavaMailSender,
    private val springTemplateEngine: SpringTemplateEngine
) {

    lateinit var mimeMessageHelper: MimeMessageHelper
    lateinit var context: Context
    lateinit var content: String
    lateinit var message: MimeMessage

    fun makeContext(params: Map<String, Any>) {
        context = Context()
        params.forEach { (key, value) -> context.setVariable(key, value) }
    }

    fun makeTemplateEngine(template: String) {
        content = springTemplateEngine.process(template, context)
    }

    fun makeMimeMessageHelper(aliceMailDto: AliceMailDto) {
        message = javaMailSender.createMimeMessage()
        mimeMessageHelper = MimeMessageHelper(message, true)
        for (email in aliceMailDto.to) mimeMessageHelper.addTo(email)
        for (email in aliceMailDto.cc!!) mimeMessageHelper.addCc(email)
        for (email in aliceMailDto.bcc!!) mimeMessageHelper.addBcc(email)
        mimeMessageHelper.setFrom(aliceMailDto.from)
        aliceMailDto.fromName?.let { mimeMessageHelper.setFrom(aliceMailDto.from, it) }
        aliceMailDto.subject?.let { mimeMessageHelper.setSubject(it) }
        aliceMailDto.content?.let { mimeMessageHelper.setText(it, true) }
    }

    fun send() {
        javaMailSender.send(message)
    }
}

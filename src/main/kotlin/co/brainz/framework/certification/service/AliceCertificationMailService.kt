package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.dto.AliceMailDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceEncryptionUtil
import java.net.Inet4Address
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AliceCertificationMailService(
    private val aliceMailService: AliceMailService,
    private val aliceCertificationService: AliceCertificationService
) {

    @Value("\${server.protocol}")
    lateinit var senderProtocol: String

    @Value("\${server.port}")
    lateinit var senderPort: String

    @Value("\${spring.mail.username}")
    lateinit var senderEmail: String

    @Value("\${spring.mail.titleName}")
    lateinit var senderName: String

    val host: String = Inet4Address.getLocalHost().hostAddress

    @Transactional
    fun sendMail(userId: String, email: String, target: String?, password: String?) {
        var certificationKey: String =
            AliceKeyGeneratorService().getKey(AliceConstants.EMAIL_CERTIFICATION_KEY_SIZE, false)
        var statusCode = AliceUserConstants.Status.SIGNUP.code

        when (target) {
            AliceUserConstants.SendMailStatus.CREATE_USER.code -> {
                statusCode = AliceUserConstants.Status.SIGNUP.code
            }
            AliceUserConstants.SendMailStatus.UPDATE_USER_EMAIL.code -> {
                statusCode = AliceUserConstants.Status.EDIT.code
            }
            AliceUserConstants.SendMailStatus.UPDATE_USER.code,
            AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> {
                statusCode = AliceUserConstants.Status.CERTIFIED.code
                certificationKey = ""
            }
        }

        val certificationDto = AliceCertificationDto(userId, email, certificationKey, statusCode, password)
        aliceCertificationService.updateUser(certificationDto)
        when (target) {
            AliceUserConstants.SendMailStatus.CREATE_USER.code,
            AliceUserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
            AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> this.sendCertificationMail(certificationDto)
        }
    }

    private fun makeLinkUrl(aliceCertificationDto: AliceCertificationDto): String {
        val uid =
            "${aliceCertificationDto.certificationCode}:${aliceCertificationDto.userId}:${aliceCertificationDto.email}"
        val encryptUid: String = AliceEncryptionUtil().twoWayEnCode(uid)
        val urlEncryptUid: String = URLEncoder.encode(encryptUid, StandardCharsets.UTF_8)
        return "$senderProtocol://$host:$senderPort/certification/valid?uid=$urlEncryptUid"
    }

    private fun makeMailInfo(aliceCertificationDto: AliceCertificationDto): AliceMailDto {
        var subject = "[Alice Project] 이메일 주소를 인증해주세요"
        if (aliceCertificationDto.password != null) {
            subject = "[Alice Project] 비밀번호 안내메일"
        }
        val content: String = aliceMailService.content
        val to: ArrayList<String> = arrayListOf(aliceCertificationDto.email)
        return AliceMailDto(subject, content, senderEmail, senderName, to, arrayListOf(), arrayListOf())
    }

    private fun sendCertificationMail(aliceCertificationDto: AliceCertificationDto) {
        aliceMailService.makeContext(makeContextValues(aliceCertificationDto))
        aliceMailService.makeTemplateEngine("certification/emailTemplate")
        val aliceMailDto: AliceMailDto = this.makeMailInfo(aliceCertificationDto)
        aliceMailService.makeMimeMessageHelper(aliceMailDto)
        aliceMailService.send()
    }

    private fun makeContextValues(aliceCertificationDto: AliceCertificationDto): Map<String, Any> {
        val params: MutableMap<String, Any> = HashMap()
        if (aliceCertificationDto.password != null) {
            params["intro"] = "사용자 계정이 생성되었습니다."
            params["message"] = "비밀번호 :" + aliceCertificationDto.password
        } else {
            params["intro"] = "안녕하세요! Alice Project에 가입해주셔서 감사합니다."
            params["message"] = "본 메일은 Alice Project 가입을 완료하기 위한 인증메일입니다.\n" +
                    "만약 인증 메일을 요청하신 적이 없다면 본 메일을 삭제해주시기 바랍니다.\n" +
                    "이메일 인증을 위해 아래 버튼을 클릭해주세요."
            params["link"] = this.makeLinkUrl(aliceCertificationDto)
            params["text"] = "이메일 인증"
            params["regards"] = "감사합니다."
        }

        return params
    }
}

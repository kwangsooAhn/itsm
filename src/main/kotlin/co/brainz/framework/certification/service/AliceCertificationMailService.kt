package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.dto.AliceMailDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.itsm.user.constants.UserConstants
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

    @Value("\${spring.mail.enabled}")
    private val mailEnabled: Boolean = false

    val host: String = Inet4Address.getLocalHost().hostAddress

    @Transactional
    fun sendMail(userId: String, email: String, target: String?, password: String?) {
        var certificationKey: String =
            AliceKeyGeneratorService().getKey(AliceConstants.EMAIL_CERTIFICATION_KEY_SIZE, false)
        var statusCode = UserConstants.UserStatus.SIGNUP.code
        var classficationCode: String? = ""

        when (target) {
            UserConstants.SendMailStatus.CREATE_USER.code -> {
                statusCode = UserConstants.UserStatus.SIGNUP.code
            }
            UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code -> {
                statusCode = UserConstants.UserStatus.EDIT.code
            }
            UserConstants.SendMailStatus.UPDATE_USER.code,
            UserConstants.SendMailStatus.UPDATE_USER.code,
            UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code,
            UserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> {
                if (target == UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code) {
                    classficationCode = UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code
                } else if (target == UserConstants.SendMailStatus.CREATE_USER_ADMIN.code) {
                    classficationCode = UserConstants.SendMailStatus.CREATE_USER_ADMIN.code
                }
                statusCode = UserConstants.UserStatus.CERTIFIED.code
                certificationKey = ""
            }
        }

        val certificationDto =
            AliceCertificationDto(userId, email, certificationKey, statusCode, password, classficationCode)
        aliceCertificationService.updateUser(certificationDto)
        when (target) {
            UserConstants.SendMailStatus.CREATE_USER.code,
            UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
            UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code,
            UserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> if (mailEnabled) this.sendCertificationMail(certificationDto)
        }
    }

    private fun makeLinkUrl(aliceCertificationDto: AliceCertificationDto): String {
        val uid =
            "${aliceCertificationDto.certificationCode}:${aliceCertificationDto.userId}:${aliceCertificationDto.email}"
        val encryptUid: String = AliceEncryptionUtil().encryptEncoder(uid, AliceConstants.EncryptionAlgorithm.AES256.value)
        val urlEncryptUid: String = URLEncoder.encode(encryptUid, StandardCharsets.UTF_8)
        return "$senderProtocol://$host:$senderPort/certification/valid?uid=$urlEncryptUid"
    }

    private fun makeMailInfo(aliceCertificationDto: AliceCertificationDto): AliceMailDto {
        var subject = "[Zenius ITSM] ????????? ????????? ??????????????????"
        if (aliceCertificationDto.password != null) {
            subject = "[Zenius ITSM] ???????????? ????????????"
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
        when (aliceCertificationDto.classificationCode) {
            UserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> {
                params["intro"] = "????????? ????????? ?????????????????????."
                params["message"] = "????????? ????????? : " + aliceCertificationDto.userId + "\n" +
                    "?????? ???????????? : " + aliceCertificationDto.password
                params["link"] = "$senderProtocol://$host:$senderPort/portals/main"
                params["text"] = "ITSM?????? ????????????"
            }
            UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code -> {
                params["intro"] = "????????? ??????????????? ????????????????????????."
                params["message"] = "???????????? :" + aliceCertificationDto.password
            }
            else -> {
                params["intro"] = "???????????????.\n" +
                    "Zenius ITSM??? ?????????????????? ???????????????."
                params["message"] = "??? ????????? Zenius ITSM ????????? ???????????? ?????? ?????????????????????.\n" +
                    "?????? ?????? ????????? ???????????? ?????? ????????? ??? ????????? ?????????????????? ????????????.\n" +
                    "????????? ????????? ?????? ?????? ????????? ??????????????????."
                params["link"] = this.makeLinkUrl(aliceCertificationDto)
                params["text"] = "????????? ??????"
            }
        }
        return params
    }
}

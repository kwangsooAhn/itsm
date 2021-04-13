/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.fileTransaction.service.AliceFileAvatarService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/certification")
class AliceCertificationRestController(
    private val aliceCertificationService: AliceCertificationService,
    private val aliceCertificationMailService: AliceCertificationMailService,
    private val aliceFileAvatarService: AliceFileAvatarService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("")
    fun setUser(@RequestBody aliceSignUpDto: AliceSignUpDto): String {
        val result = aliceCertificationService.createUser(aliceSignUpDto, AliceUserConstants.USER_ID)
        if (result == AliceUserConstants.SignUpStatus.STATUS_SUCCESS.code) {
            aliceCertificationMailService.sendMail(
                aliceSignUpDto.userId,
                aliceSignUpDto.email,
                AliceUserConstants.SendMailStatus.CREATE_USER.code,
                null
            )
        }
        return result
    }

    @GetMapping("/certifiedmail")
    fun sendCertifiedMail() {
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        aliceCertificationMailService.sendMail(
            aliceUserDto.userId,
            aliceUserDto.email,
            AliceUserConstants.SendMailStatus.CREATE_USER.code,
            null
        )
    }

    @PostMapping("/fileupload")
    fun uploadFile(
        @RequestPart("file") multipartFile: MultipartFile,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        val map: MutableMap<String, Any> = mutableMapOf()
        val fileName = request.getParameter("fileName") ?: null

        map["file"] = aliceFileAvatarService.uploadTempAvatarFile(
            multipartFile,
            fileName
        )

        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json; charset=utf-8")

        return ResponseEntity(map, headers, HttpStatus.OK)
    }
}

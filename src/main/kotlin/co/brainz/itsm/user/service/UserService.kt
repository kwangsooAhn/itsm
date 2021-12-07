/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.fileTransaction.service.AliceFileAvatarService
import co.brainz.framework.timezone.AliceTimezoneEntity
import co.brainz.framework.timezone.AliceTimezoneRepository
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserAbsenceDto
import co.brainz.itsm.user.dto.UserCustomDto
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListReturnDto
import co.brainz.itsm.user.dto.UserSearchCondition
import co.brainz.itsm.user.dto.UserSelectListDto
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserUpdatePasswordDto
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.itsm.user.repository.UserCustomRepository
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Paths
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional
import kotlin.math.ceil
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(
    private val aliceCertificationMailService: AliceCertificationMailService,
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceMessageSource: AliceMessageSource,
    private val codeService: CodeService,
    private val excelComponent: ExcelComponent,
    private val userAliceTimezoneRepository: AliceTimezoneRepository,
    private val userCustomRepository: UserCustomRepository,
    private val userRepository: UserRepository,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val roleRepository: RoleRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val aliceFileAvatarService: AliceFileAvatarService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfTokenRepository: WfTokenRepository
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    @Value("\${user.default.profile}")
    private val userDefaultProfile: String = ""

    @Value("\${password.expired.period}")
    private var passwordExpiredPeriod: Long = 90L

    init {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(userSearchCondition: UserSearchCondition): UserListReturnDto {
        val queryResult = userRepository.findAliceUserEntityList(userSearchCondition)
        val userList: MutableList<UserListDataDto> = mutableListOf()

        for (user in queryResult.results) {
            val avatarPath = userDetailsService.makeAvatarPath(user)
            user.avatarPath = avatarPath
            userList.add(user)
        }

        return UserListReturnDto(
            data = userList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = userRepository.countByUserIdNotContaining(AliceUserConstants.CREATE_USER_ID),
                currentPageNum = userSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    fun selectUser(userId: String): AliceUserEntity {
        return userRepository.findByUserId(userId)
    }

    /**
     * 사용자 oauthKey, 플랫폼으로 해당 정보를 조회한다.
     */
    fun selectByOauthKeyAndPlatform(oauthKey: String, platform: String): Optional<AliceUserEntity> {
        return userRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }

    /**
     * 사용자 KEY로 정보를 수정한다.
     */
    @Transactional
    fun updateUser(userUpdateDto: UserUpdateDto): AliceUserEntity {
        val targetEntity = updateDataInput(userUpdateDto)
        return userRepository.save(targetEntity)
    }

    /**
     * 사용자의 KEY로 해당 정보 1건을 조회한다.
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return userDetailsService.selectUserKey(userKey)
    }

    /**
     * 사용자의 정보를 수정한다.
     *
     * @param userEditType
     */
    @Transactional
    fun updateUserEdit(userUpdateDto: UserUpdateDto, userEditType: String): String {
        var code: String = userEditValid(userUpdateDto)
        when (code) {
            AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code -> {
                val userEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey =
                    attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val targetEntity = updateDataInput(userUpdateDto)

                when (userUpdateDto.password != null) {
                    targetEntity.password != userUpdateDto.password -> {
                        val password = aliceCryptoRsa.decrypt(privateKey, userUpdateDto.password!!)
                        userUpdateDto.password.let { targetEntity.password = BCryptPasswordEncoder().encode(password) }
                        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)
                    }
                }

                aliceFileAvatarService.uploadAvatarFile(targetEntity, userUpdateDto.avatarUUID)

                logger.debug("targetEntity {}, update {}", targetEntity, userUpdateDto)
                userRepository.save(targetEntity)

                if (targetEntity.uploaded) {
                    aliceFileAvatarService.avatarFileNameMod(targetEntity)
                }

                when (userEditType == AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code) {
                    true -> {
                        userEntity.userRoleMapEntities.forEach {
                            userRoleMapRepository.deleteById(AliceUserRoleMapPk(userUpdateDto.userKey, it.role.roleId))
                        }
                        userUpdateDto.roles!!.forEach {
                            userRoleMapRepository.save(
                                AliceUserRoleMapEntity(
                                    targetEntity,
                                    roleRepository.findByRoleId(it)
                                )
                            )
                        }
                    }
                }

                code = when (targetEntity.email == userEntity.email) {
                    true -> {
                        when (userEditType) {
                            AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code ->
                                AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_ADMIN.code
                            AliceUserConstants.UserEditType.SELF_USER_EDIT.code ->
                                AliceUserConstants.UserEditStatus.STATUS_SUCCESS.code
                            else -> AliceUserConstants.UserEditStatus.STATUS_SUCCESS.code
                        }
                    }
                    false -> AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code
                }

                // 사용자 부재 설정
                if (userUpdateDto.absenceYn) {
                    userUpdateDto.absence?.let { this.setUserAbsence(userUpdateDto.userKey, it) }
                } else {
                    this.resetUserAbsence(userUpdateDto.userKey, UserConstants.UserCustom.USER_ABSENCE.code)
                }
            }
        }
        return code
    }

    /**
     * 자기정보 수정 시, 이메일 및 ID의 중복을 검사한다.
     */
    fun userEditValid(userUpdateDto: UserUpdateDto): String {
        val targetEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
        var code: String = AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code

        when (true) {
            targetEntity.userId != userUpdateDto.userId -> {
                if (userRepository.countByUserId(userUpdateDto.userId) > 0) {
                    code = AliceUserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
                }
            }
            targetEntity.email != userUpdateDto.email -> {
                if (aliceCertificationRepository.countByEmail(userUpdateDto.email!!) > 0) {
                    code = AliceUserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                }
            }
        }
        return code
    }

    /**
     * 사용자 수정 관련 데이터 저장 공통화
     */
    fun updateDataInput(userUpdateDto: UserUpdateDto): AliceUserEntity {
        val targetEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
        userUpdateDto.userId.let { targetEntity.userId = userUpdateDto.userId }
        userUpdateDto.userName?.let { targetEntity.userName = userUpdateDto.userName!! }
        userUpdateDto.email?.let { targetEntity.email = userUpdateDto.email!! }
        userUpdateDto.position?.let { targetEntity.position = userUpdateDto.position!! }
        userUpdateDto.department?.let { targetEntity.department = userUpdateDto.department }
        userUpdateDto.officeNumber?.let { targetEntity.officeNumber = userUpdateDto.officeNumber }
        userUpdateDto.mobileNumber?.let { targetEntity.mobileNumber = userUpdateDto.mobileNumber }
        userUpdateDto.timezone?.let { targetEntity.timezone = userUpdateDto.timezone!! }
        userUpdateDto.lang?.let { targetEntity.lang = userUpdateDto.lang!! }
        userUpdateDto.timeFormat?.let { targetEntity.timeFormat = userUpdateDto.timeFormat!! }
        userUpdateDto.theme?.let { targetEntity.theme = userUpdateDto.theme!! }
        userUpdateDto.useYn?.let { targetEntity.useYn = userUpdateDto.useYn!! }
        userUpdateDto.absence.let { targetEntity.absenceYn = userUpdateDto.absenceYn }

        return targetEntity
    }

    /**
     * 자기정보 수정 시, 타임존의 데이터를 가져온다.
     */
    fun selectTimezoneList(): MutableList<AliceTimezoneEntity> {
        return userAliceTimezoneRepository.findAllByOrderByTimezoneIdAsc()
    }

    /**
     * 모든 사용자 정보를 조회한다.
     * (selectbox 용으로 key, id, name 조회)
     */
    fun selectUserListOrderByName(): MutableList<UserSelectListDto> {
        val userList = userRepository.findByOrderByUserNameAsc()
        val userDtoList = mutableListOf<UserSelectListDto>()
        for (userEntity in userList) {
            userDtoList.add(
                UserSelectListDto(
                    userKey = userEntity.userKey,
                    userId = userEntity.userId,
                    userName = userEntity.userName
                )
            )
        }
        return userDtoList
    }

    /**
     * 사용자의 비밀번호를 생성한다.
     */
    fun makePassword(): String {
        val password = StringBuffer()
        for (i in 0..9) {
            when (Random.nextInt(3)) {
                0 -> password.append(((Random.nextInt(26)) + 97).toChar())
                1 -> password.append(((Random.nextInt(26)) + 65).toChar())
                2 -> password.append((Random.nextInt(10)))
            }
        }
        return password.toString()
    }

    /**
     * 사용자의 비밀번호를 초기화한다.
     */
    @Transactional
    fun resetPassword(userKey: String, password: String): String {
        val publicKey = aliceCryptoRsa.getPublicKey()
        val encryptPassword = aliceCryptoRsa.encrypt(publicKey, password)
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val decryptPassword = aliceCryptoRsa.decrypt(privateKey, encryptPassword)
        val targetEntity = userDetailsService.selectUserKey(userKey)
        targetEntity.password = BCryptPasswordEncoder().encode(decryptPassword)
        targetEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)

        userRepository.save(targetEntity)

        aliceCertificationMailService.sendMail(
            targetEntity.userId,
            targetEntity.email,
            AliceUserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code,
            password
        )
        return AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_PASSWORD.code
    }

    /**
     * Avatar 이미지 사이즈 조회
     */
    fun getUserAvatarSize(userEntity: AliceUserEntity): Long {
        return if (userEntity.uploaded) {
            Paths.get(userEntity.uploadedLocation).toFile().length()
        } else {
            ClassPathResource(userDefaultProfile).inputStream.available().toLong()
        }
    }

    /**
     * 사용자 설정 기본 코드 조회
     */
    fun getInitCodeList(): LinkedHashMap<String, List<CodeDto>> {
        val codes = mutableListOf(
            UserConstants.PTHEMECODE.value,
            UserConstants.PLANGCODE.value,
            UserConstants.PDATECODE.value,
            UserConstants.PTIMECODE.value
        )
        val codeList = codeService.selectCodeByParent(codes)
        val allCodes: LinkedHashMap<String, List<CodeDto>> = LinkedHashMap()
        allCodes["themeList"] = AliceUtil().getCodes(codeList, UserConstants.PTHEMECODE.value)
        allCodes["langList"] = AliceUtil().getCodes(codeList, UserConstants.PLANGCODE.value)
        allCodes["dateList"] = AliceUtil().getCodes(codeList, UserConstants.PDATECODE.value)
        allCodes["timeList"] = AliceUtil().getCodes(codeList, UserConstants.PTIMECODE.value)
        return allCodes
    }

    /**
     * 사용자 정의 색상 조회
     */
    fun getUserCustomColors(): UserCustomDto? {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        return userCustomRepository.findByUserAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
    }

    /**
     * 사용자 정의 색상 저장
     */
    fun updateUserCustomColors(userCustomDto: UserCustomDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        // 삭제
        userCustomRepository.deleteByUserAndAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
        // 저장
        userCustomRepository.save(
            UserCustomEntity(
                user = userEntity,
                customType = UserConstants.UserCustom.COLOR.code,
                customValue = userCustomDto.customValue
            )
        )
        return true
    }

    /**
     * 사용자 정의 업무 대리인 조회
     */
    fun getUserAbsenceInfo(userKey: String): UserAbsenceDto? {
        val userEntity = userDetailsService.selectUserKey(userKey)
        var absenceInfo = ""
        run loop@{
            userEntity.userCustomEntities.forEach { custom ->
                if (custom.customType == UserConstants.UserCustom.USER_ABSENCE.code) {
                    absenceInfo = custom.customValue.toString()
                    return@loop
                }
            }
        }
        val absenceDto = UserAbsenceDto()
        if (absenceInfo.isNotEmpty()) {
            val absenceMap = mapper.readValue(absenceInfo, Map::class.java)
            absenceDto.substituteUserKey = absenceMap["substituteUserKey"].toString()
            absenceDto.substituteUser = userDetailsService.selectUserKey(absenceDto.substituteUserKey!!).userName
            absenceDto.startDt = mapper.convertValue(absenceMap["startDt"], LocalDateTime::class.java)
            absenceDto.endDt = mapper.convertValue(absenceMap["endDt"], LocalDateTime::class.java)
        }
        return absenceDto
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    fun updatePassword(userUpdatePasswordDto: UserUpdatePasswordDto): Long {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val rawNewPassword = aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.newPassword!!)
        val rawNowPassword = aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.nowPassword!!)
        val userEntity = selectUser(userUpdatePasswordDto.userId!!)

        if (!BCryptPasswordEncoder().matches(rawNowPassword, userEntity.password)) { // 현재 비밀번호가 틀릴 경우
            return UserConstants.UserUpdatePassword.NOT_EQUAL_NOW_PASSWORD.code
        }

        if (BCryptPasswordEncoder().matches(rawNewPassword, userEntity.password)) { // 새 비밀번호가 현재 비밀번호와 같을 경우
            return UserConstants.UserUpdatePassword.SAME_AS_CURRENT_PASSWORD.code
        }

        userEntity.password =
            BCryptPasswordEncoder().encode(aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.newPassword!!))
        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)

        return UserConstants.UserUpdatePassword.SUCCESS.code
    }

    /**
     * 비밀번호 다음에 변경하기
     */
    @Transactional
    fun extendExpiryDate(userUpdatePasswordDto: UserUpdatePasswordDto): Long {
        val userEntity = selectUser(userUpdatePasswordDto.userId!!)
        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)
        return UserConstants.UserUpdatePassword.SUCCESS.code
    }

    /**
     * 사용자 부재 관련 정보 초기화
     */
    fun resetUserAbsence(userKey: String, customCode: String): Boolean {
        val userEntity = userDetailsService.selectUserKey(userKey)
        userCustomRepository.deleteByUserAndAndCustomType(userEntity, customCode)
        return true
    }

    /**
     * 사용자 부재 관련 정보 설정
     */
    fun setUserAbsence(userKey: String, absenceDto: UserAbsenceDto): Boolean {
        val absenceMap = java.util.LinkedHashMap<String, Any>()
        absenceMap["startDt"] = mapper.convertValue(absenceDto.startDt, LocalDateTime::class.java)
        absenceMap["endDt"] = mapper.convertValue(absenceDto.endDt, LocalDateTime::class.java)
        absenceMap["substituteUserKey"] = absenceDto.substituteUserKey.toString()
        val userCustomEntity = UserCustomEntity(
            user = userDetailsService.selectUserKey(userKey),
            customType = UserConstants.UserCustom.USER_ABSENCE.code,
            customValue = mapper.writeValueAsString(absenceMap)
        )
        userCustomRepository.save(userCustomEntity)
        return true
    }

    /**
     * 사용자 현재 문서 업무 대리인으로 변경
     */
    @Transactional
    fun executeUserProcessingDocumentAbsence(absenceInfo: String): Boolean {
        var isSuccess = false
        val absence = mapper.readValue(absenceInfo, Map::class.java)
        val fromUser = absence["userKey"].toString()
        val toUser = absence["substituteUserKey"].toString()
        when (absence["userKey"].toString()) {
            currentSessionUser.getUserKey() -> {
                isSuccess = this.changeDocumentAssigneeToAbsenceUser(fromUser, toUser)
            }
            else -> { // 본인이 아닌 경우 사용자 관리자 권한이 있는지 확인한다.
                var hasRole = false
                val permitRoles = setOf("ROLE_admin", "ROLE_users.manager")
                run loop@{
                    currentSessionUser.getUserDto()?.grantedAuthorises?.forEach {
                        if (permitRoles.contains(it.authority)) {
                            hasRole = true
                            return@loop
                        }
                    }
                }
                if (hasRole) {
                    isSuccess = this.changeDocumentAssigneeToAbsenceUser(fromUser, toUser)
                }
            }
        }
        return isSuccess
    }

    /**
     * [fromUser] 의 처리할 문서를 [toUser] 로 변경
     */
    private fun changeDocumentAssigneeToAbsenceUser(fromUser: String, toUser: String): Boolean {
        // 현재 처리할 문서 조회
        val tokenList = wfTokenRepository.findProcessTokenByAssignee(fromUser)
        tokenList.forEach { token ->
            token.assigneeId = toUser
            wfTokenRepository.save(token)
        }
        return true
    }

    /**
     * 사용자 목록 Excel 다운로드
     */
    fun getUsersExcelDownload(userSearchCondition: UserSearchCondition): ResponseEntity<ByteArray> {
        val returnDto = selectUserList(userSearchCondition)
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.id"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.name"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.email"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.department"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.position"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.officeNumber"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.mobileNumber"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.signUpDate"),
                                    cellWidth = 5000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.usageStatus"),
                                    cellWidth = 5000
                                )
                            )
                        )
                    )
                )
            )
        )
        returnDto.data.forEach { result ->
            excelVO.sheets[0].rows.add(
                ExcelRowVO(
                    cells = mutableListOf(
                        ExcelCellVO(value = result.userId),
                        ExcelCellVO(value = result.userName),
                        ExcelCellVO(value = result.email),
                        ExcelCellVO(value = result.department ?: ""),
                        ExcelCellVO(value = result.position ?: ""),
                        ExcelCellVO(value = result.officeNumber ?: ""),
                        ExcelCellVO(value = result.mobileNumber ?: ""),
                        ExcelCellVO(
                            value = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(result.createDt)
                        ),
                        ExcelCellVO(
                            value = if (result.absenceYn) {
                                aliceMessageSource.getMessage("user.status.true")
                            } else {
                                aliceMessageSource.getMessage("user.status.false")
                            }
                        )
                    )
                )
            )
        }
        return excelComponent.download(excelVO)
    }
}

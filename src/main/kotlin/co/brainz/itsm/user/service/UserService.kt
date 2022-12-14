/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.organization.repository.OrganizationRoleMapRepository
import co.brainz.framework.organization.service.OrganizationService
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.timezone.AliceTimezoneEntity
import co.brainz.framework.timezone.AliceTimezoneRepository
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserAbsenceDto
import co.brainz.itsm.user.dto.UserCustomDto
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.dto.UserListReturnDto
import co.brainz.itsm.user.dto.UserNotificationChannelDto
import co.brainz.itsm.user.dto.UserSearchCompCondition
import co.brainz.itsm.user.dto.UserSearchCondition
import co.brainz.itsm.user.dto.UserSelectListDto
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserUpdatePasswordDto
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.itsm.user.repository.UserCustomRepository
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.LinkedHashMap
import kotlin.collections.set
import kotlin.math.ceil
import kotlin.random.Random

/**
 * ????????? ?????? ?????????
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
    private val aliceResourceProvider: AliceResourceProvider,
    private val currentSessionUser: CurrentSessionUser,
    private val wfTokenRepository: WfTokenRepository,
    private val organizationService: OrganizationService,
    private val organizationRepository: OrganizationRepository,
    private val organizationRoleMapRepository: OrganizationRoleMapRepository,
    private val roleService: RoleService,
    private val aliceRoleAuthMapRepository: AliceRoleAuthMapRepository,
    private val aliceEncryptionUtil: AliceEncryptionUtil
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    @Value("\${user.default.profile}")
    private val userDefaultProfile: String = ""

    @Value("\${password.expired.period}")
    private var passwordExpiredPeriod: Long = 90L

    @Value("\${encryption.algorithm}")
    private val algorithm: String = ""

    init {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    /**
     * ???????????? ???????????? ????????? ????????? ?????? ??????
     *   - ????????? ?????? ????????? ??? ???????????? ?????? ????????? ?????? ???????????? ????????? ????????? ??????
     */
    fun selectNotAbsenceUserList(params: LinkedHashMap<String, Any>): UserListReturnDto {
        val excludeIds = mutableSetOf<String>()
        excludeIds.add(params["userKey"].toString())
        if (params["from"].toString().isNotEmpty() && params["to"].toString().isNotEmpty()) {
            val from = ZonedDateTime.parse(params["from"].toString()).toLocalDateTime()
            val to = ZonedDateTime.parse(params["to"].toString()).toLocalDateTime()
            val absenceList =
                userCustomRepository.findByCustomType(UserConstants.UserCustom.USER_ABSENCE.code)
            absenceList?.forEach { absence ->
                val userAbsenceDto =
                    mapper.readValue(absence.customValue, UserAbsenceDto::class.java)
                if ((userAbsenceDto.startDt!! <= from && userAbsenceDto.endDt!! >= from) ||
                    (userAbsenceDto.startDt!! <= to && userAbsenceDto.endDt!! >= to)
                ) {
                    excludeIds.add(absence.userKey)
                }
            }
        }
        val userSearchCondition = UserSearchCondition(
            searchValue = params["search"].toString(),
            isFilterUseYn = true
        )
        if (excludeIds.isNotEmpty()) {
            userSearchCondition.excludeIds = excludeIds
        }

        return this.selectUserList(userSearchCondition)
    }

    /**
     * ????????? ?????? ????????? ?????? ????????????.
     */
    fun getSearchUserList(userSearchCompCondition: UserSearchCompCondition): UserListReturnDto {
        val targetKeys = mutableSetOf<String>()

        when (userSearchCompCondition.targetCriteria) {
            UserConstants.UserSearchTarget.ORGANIZATION.code -> {
                val organization = organizationRepository.findByOrganizationId(userSearchCompCondition.searchKeys)
                val organizationList = organizationRepository.findByOrganizationSearchList(OrganizationSearchCondition())
                val organizationNameList = organizationService.getOrganizationChildren(organization, organizationList, mutableListOf())
                organizationNameList.forEach { targetKeys.add(it) }
            }
            UserConstants.UserSearchTarget.CUSTOM.code -> userSearchCompCondition.searchKeys.split(" ").forEach { targetKeys.add(it) }
        }

        val userSearchCondition = UserSearchCondition(
            searchValue = userSearchCompCondition.searchValue,
            optionalCondition = userSearchCompCondition.targetCriteria,
            optionalTargets = targetKeys,
            isFilterUseYn = true
        )

        return this.selectUserList(userSearchCondition)
    }

    /**
     * ????????? ????????? ????????????.
     */
    fun selectUserList(userSearchCondition: UserSearchCondition): UserListReturnDto {
        val pagingResult = userRepository.findAliceUserEntityList(userSearchCondition)
        val totalCount = userRepository.countByUserIdNot(UserConstants.CREATE_USER_ID)
        val userList: List<UserListDataDto> = mapper.convertValue(pagingResult.dataList, object : TypeReference<List<UserListDataDto>>() {})
        userList.forEach { user ->
            val avatarPath = userDetailsService.makeAvatarPath(user)
            user.avatarPath = avatarPath
        }

        val organizationList = organizationRepository.findByOrganizationSearchList(OrganizationSearchCondition())
        userList.forEach { user ->
            val organization = organizationList.firstOrNull { it.organizationId == user.groupId }
            var organizationName = mutableListOf<String>()
            if (organization != null) {
                if (organization.pOrganization != null) {
                    organizationName = organizationService.getOrganizationParent(organization, organizationList, organizationName)
                } else {
                    organizationName.add(organization.organizationName.toString())
                }
            }
            user.groupName = organizationName.joinToString(" > ")
        }

        return UserListReturnDto(
            data = userList,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = totalCount,
                currentPageNum = userSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / userSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * ????????? ID??? ?????? ????????? 1??? ????????????.
     */
    fun selectUser(userId: String): AliceUserEntity {
        return userRepository.findByUserId(userId)
    }

    /**
     * ????????? oauthKey, ??????????????? ?????? ????????? ????????????.
     */
    fun selectByOauthKeyAndPlatform(oauthKey: String, platform: String): Optional<AliceUserEntity> {
        return userRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }

    /**
     * ????????? KEY??? ????????? ????????????.
     */
    @Transactional
    fun updateUser(userUpdateDto: UserUpdateDto): AliceUserEntity {
        val targetEntity = updateDataInput(userUpdateDto)
        return userRepository.save(targetEntity)
    }

    /**
     * ???????????? KEY??? ?????? ?????? 1?????? ????????????.
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return userDetailsService.selectUserKey(userKey)
    }

    /**
     * ???????????? ????????? ????????????.
     *
     * @param userEditType
     */
    @Transactional
    fun updateUserEdit(userUpdateDto: UserUpdateDto, userEditType: String): ZResponse {
        var code: String = userEditValid(userUpdateDto)
        when (code) {
            UserConstants.ResponseStatus.STATUS_VALID_SUCCESS.code -> {
                val userEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey =
                    attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val targetEntity = updateDataInput(userUpdateDto)

                when (userUpdateDto.password?.isNotEmpty()) {
                    targetEntity.password != userUpdateDto.password -> {
                        val password = aliceCryptoRsa.decrypt(privateKey, userUpdateDto.password!!)
                        userUpdateDto.password.let { targetEntity.password = aliceEncryptionUtil.encryptEncoder(password, this.algorithm) }
                        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)
                    }
                }

                this.setUploadAvatar(targetEntity, userUpdateDto.avatarUUID)

                logger.debug("targetEntity {}, update {}", targetEntity, userUpdateDto)
                userRepository.save(targetEntity)

                if (targetEntity.uploaded) {
                    this.setAvatarName(targetEntity)
                }

                when (userEditType == UserConstants.UserEditType.ADMIN_USER_EDIT.code) {
                    true -> {
                        userEntity.userRoleMapEntities.forEach {
                            userRoleMapRepository.deleteById(AliceUserRoleMapPk(userUpdateDto.userKey, it.role.roleId))
                        }
                        // ????????? role ??????
                        if (!targetEntity.department.isNullOrEmpty()) {
                            val organizationRoles =
                                organizationRoleMapRepository.findRoleListByOrganizationId(targetEntity.department!!)
                            organizationRoles.forEach { organizationRole ->
                                if (userUpdateDto.roles!!.contains(organizationRole.roleId)) {
                                    userUpdateDto.roles!!.remove(organizationRole.roleId)
                                }
                            }
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
                            UserConstants.UserEditType.ADMIN_USER_EDIT.code ->
                                ZResponseConstants.STATUS.SUCCESS_EDIT.code
                            UserConstants.UserEditType.SELF_USER_EDIT.code ->
                                ZResponseConstants.STATUS.SUCCESS.code
                            else -> ZResponseConstants.STATUS.SUCCESS.code
                        }
                    }
                    false -> ZResponseConstants.STATUS.SUCCESS_EDIT_EMAIL.code
                }

                // ????????? ?????? ??????
                if (userUpdateDto.absenceYn) {
                    userUpdateDto.absence?.let { this.setUserAbsence(userUpdateDto.userKey, it) }
                } else {
                    this.resetUserAbsence(userUpdateDto.userKey, UserConstants.UserCustom.USER_ABSENCE.code)
                }
            }
            ZResponseConstants.STATUS.ERROR_FAIL.code -> {
                code = ZResponseConstants.STATUS.ERROR_FAIL.code
            }
        }

        if (userEditType == UserConstants.UserEditType.SELF_USER_EDIT.code) {
            when (code) {
                ZResponseConstants.STATUS.SUCCESS_EDIT_EMAIL.code -> {
                    aliceCertificationMailService.sendMail(
                        userUpdateDto.userId,
                        userUpdateDto.email!!,
                        UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
                        null
                    )
                }
                else -> aliceCertificationMailService.sendMail(
                    userUpdateDto.userId,
                    userUpdateDto.email!!,
                    UserConstants.SendMailStatus.UPDATE_USER.code,
                    null
                )
            }
        }

        // ????????? ?????? ?????? ??? ?????? ?????? ????????? ??????
        if (userUpdateDto.absence?.transferYN == true) code = this.executeUserProcessingDocumentAbsence(userUpdateDto.absence!!)

        return ZResponse(
            status = code
        )
    }

    /**
     * ???????????? ?????? ???, ?????????, ID ???????????? ??? ???????????? ???????????? ????????????.
     */
    fun userEditValid(userUpdateDto: UserUpdateDto): String {
        val targetEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        var code: String = UserConstants.ResponseStatus.STATUS_VALID_SUCCESS.code

        when (true) {
            (targetEntity.userId != userUpdateDto.userId) -> {
                if (userRepository.countByUserId(userUpdateDto.userId) > 0) {
                    code = UserConstants.ResponseStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
                }
            }
            (targetEntity.email != userUpdateDto.email) -> {
                if (aliceCertificationRepository.countByEmail(userUpdateDto.email!!) > 0) {
                    code = UserConstants.ResponseStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                }
            }
            !roleService.isExistSystemRoleByUser(userUpdateDto.userKey, userUpdateDto.roles) -> {
                code = ZResponseConstants.STATUS.ERROR_NOT_EXIST.code
            }
            (targetEntity.password != userUpdateDto.password && !userUpdateDto.password.isNullOrEmpty()) -> {
                val password = aliceCryptoRsa.decrypt(privateKey, userUpdateDto.password!!)
                if (!this.passwordValidationCheck(password, userUpdateDto.userId, userUpdateDto.email)) {
                    code = ZResponseConstants.STATUS.ERROR_FAIL.code
                }
            }
        }
        return code
    }

    /**
     * ????????? ?????? ?????? ????????? ?????? ?????????
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
        userUpdateDto.notificationChannel?.let {
            targetEntity.notificationChannel =
                mapper.writeValueAsString(userUpdateDto.notificationChannel)
        }

        return targetEntity
    }

    /**
     * ???????????? ?????? ???, ???????????? ???????????? ????????????.
     */
    fun selectTimezoneList(): MutableList<AliceTimezoneEntity> {
        return userAliceTimezoneRepository.findAllByOrderByTimezoneIdAsc()
    }

    /**
     * ?????? ????????? ????????? ????????????.
     * (selectbox ????????? key, id, name ??????)
     */
    fun selectUserListOrderByName(): MutableList<UserSelectListDto> {
        val userList = userRepository.findByUserIdNotOrderByUserNameAsc(UserConstants.CREATE_USER_ID)
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
     * ???????????? ??????????????? ????????????.
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
     * ???????????? ??????????????? ???????????????.
     */
    @Transactional
    fun resetPassword(userKey: String, password: String): ZResponse {
        val targetEntity = userDetailsService.selectUserKey(userKey)
        targetEntity.password = aliceEncryptionUtil.encryptEncoder(password, this.algorithm)
        targetEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)

        userRepository.save(targetEntity)

        aliceCertificationMailService.sendMail(
            targetEntity.userId,
            targetEntity.email,
            UserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code,
            password
        )
        return ZResponse(
            status = ZResponseConstants.STATUS.SUCCESS_EDIT_PASSWORD.code
        )
    }

    /**
     * Avatar ????????? ????????? ??????
     */
    fun getUserAvatarSize(userEntity: AliceUserEntity): Long {
        return if (userEntity.uploaded) {
            Paths.get(userEntity.uploadedLocation).toFile().length()
        } else {
            ClassPathResource(userDefaultProfile).inputStream.available().toLong()
        }
    }

    /**
     * ????????? ?????? ?????? ?????? ??????
     */
    fun getInitCodeList(): LinkedHashMap<String, List<CodeDto>> {
        val codes = mutableListOf(
            UserConstants.UserPCode.P_THEME_CODE.code,
            UserConstants.UserPCode.P_LANG_CODE.code,
            UserConstants.UserPCode.P_DATE_CODE.code,
            UserConstants.UserPCode.P_TIME_CODE.code
        )
        val codeList = codeService.selectCodeByParent(codes)
        val allCodes: LinkedHashMap<String, List<CodeDto>> = LinkedHashMap()
        allCodes["themeList"] = AliceUtil().getCodes(codeList, UserConstants.UserPCode.P_THEME_CODE.code)
        allCodes["langList"] = AliceUtil().getCodes(codeList, UserConstants.UserPCode.P_LANG_CODE.code)
        allCodes["dateList"] = AliceUtil().getCodes(codeList, UserConstants.UserPCode.P_DATE_CODE.code)
        allCodes["timeList"] = AliceUtil().getCodes(codeList, UserConstants.UserPCode.P_TIME_CODE.code)
        return allCodes
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    fun getUserCustomColors(): UserCustomDto? {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        return userCustomRepository.findByUserAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    fun updateUserCustomColors(userCustomDto: UserCustomDto): ZResponse {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        // ??????
        userCustomRepository.deleteByUserAndAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
        // ??????
        userCustomRepository.save(
            UserCustomEntity(
                user = userEntity,
                customType = UserConstants.UserCustom.COLOR.code,
                customValue = userCustomDto.customValue
            )
        )
        return ZResponse()
    }

    /**
     * ????????? ?????? ?????? ????????? ??????
     */
    fun getUserAbsenceInfo(userKey: String): UserAbsenceDto {
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
            absenceDto.transferYN = mapper.convertValue(absenceMap["transferYN"], Boolean::class.java)
        }
        return absenceDto
    }

    /**
     * ????????? ?????? ?????? ?????? ??????
     */
    fun getNotificationChannelInfo(userEntity: AliceUserEntity): UserNotificationChannelDto {
        return mapper.readValue(userEntity.notificationChannel, UserNotificationChannelDto::class.java)
    }

    /**
     * ???????????? ??????
     */
    @Transactional
    fun updatePassword(userUpdatePasswordDto: UserUpdatePasswordDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val rawNewPassword = aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.newPassword!!)
        val rawNowPassword = aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.nowPassword!!)
        val userEntity = selectUser(userUpdatePasswordDto.userId!!)

        when (this.algorithm.toUpperCase()) {
            AliceConstants.EncryptionAlgorithm.BCRYPT.value -> {
                if (!BCryptPasswordEncoder().matches(rawNowPassword, userEntity.password)) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
                if (BCryptPasswordEncoder().matches(rawNewPassword, userEntity.password)) {
                    status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                }
            }
            AliceConstants.EncryptionAlgorithm.AES256.value, AliceConstants.EncryptionAlgorithm.SHA256.value -> {
                val encryptNowPassword = aliceEncryptionUtil.encryptEncoder(rawNowPassword, this.algorithm)
                val encryptNewPassword = aliceEncryptionUtil.encryptEncoder(rawNewPassword, this.algorithm)

                if (encryptNowPassword != userEntity.password) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
                if (encryptNewPassword == userEntity.password) {
                    status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                }
            }
            else -> {
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        }

        userEntity.password =
            aliceEncryptionUtil.encryptEncoder(aliceCryptoRsa.decrypt(privateKey, userUpdatePasswordDto.newPassword!!), this.algorithm)
        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)

        return ZResponse(
            status = status.code
        )
    }

    /**
     * ???????????? ????????? ????????????
     */
    @Transactional
    fun extendExpiryDate(userUpdatePasswordDto: UserUpdatePasswordDto): ZResponse {
        val userEntity = selectUser(userUpdatePasswordDto.userId!!)
        userEntity.expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod)
        return ZResponse()
    }

    /**
     * ????????? ?????? ?????? ?????? ?????????
     */
    fun resetUserAbsence(userKey: String, customCode: String): Boolean {
        val userEntity = userDetailsService.selectUserKey(userKey)
        userCustomRepository.deleteByUserAndAndCustomType(userEntity, customCode)
        return true
    }

    /**
     * ????????? ?????? ?????? ?????? ??????
     */
    fun setUserAbsence(userKey: String, absenceDto: UserAbsenceDto): Boolean {
        val absenceMap = java.util.LinkedHashMap<String, Any>()
        absenceMap["startDt"] = mapper.convertValue(absenceDto.startDt, LocalDateTime::class.java)
        absenceMap["endDt"] = mapper.convertValue(absenceDto.endDt, LocalDateTime::class.java)
        absenceMap["substituteUserKey"] = absenceDto.substituteUserKey.toString()
        absenceMap["transferYN"] = absenceDto.transferYN.toString()
        val userCustomEntity = UserCustomEntity(
            user = userDetailsService.selectUserKey(userKey),
            customType = UserConstants.UserCustom.USER_ABSENCE.code,
            customValue = mapper.writeValueAsString(absenceMap)
        )
        userCustomRepository.save(userCustomEntity)
        return true
    }

    /**
     * ????????? ?????? ?????? ?????? ??????????????? ??????
     */
    @Transactional
    fun executeUserProcessingDocumentAbsence(absenceInfo: UserAbsenceDto): String {
        var status = ZResponseConstants.STATUS.SUCCESS
        var isSuccess = false
        val fromUser = absenceInfo.userKey ?: ""
        val toUser = absenceInfo.substituteUserKey ?: ""
        when (fromUser) {
            currentSessionUser.getUserKey() -> {
                isSuccess = this.changeDocumentAssigneeToAbsenceUser(fromUser, toUser)
            }
            else -> { // ????????? ?????? ?????? ????????? ????????? ????????? ????????? ????????????.
                var hasAuth = false
                val permitAuths = setOf(AuthConstants.AuthType.SYSTEM_MANAGE.value)
                run loop@{
                    currentSessionUser.getUserDto()?.grantedAuthorises?.forEach {
                        if (permitAuths.contains(it.authority)) {
                            hasAuth = true
                            return@loop
                        }
                    }
                }
                if (hasAuth) {
                    isSuccess = this.changeDocumentAssigneeToAbsenceUser(fromUser, toUser)
                }
            }
        }
        if (!isSuccess) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status.code
    }

    /**
     * [fromUser] ??? ????????? ????????? [toUser] ??? ??????
     */
    private fun changeDocumentAssigneeToAbsenceUser(fromUser: String, toUser: String): Boolean {
        // ?????? ????????? ?????? ??????
        val tokenList = wfTokenRepository.findProcessTokenByAssignee(fromUser)
        tokenList.forEach { token ->
            token.assigneeId = toUser
            wfTokenRepository.save(token)
        }
        return true
    }

    /**
     * ????????? ?????? Excel ????????????
     */
    fun getUsersExcelDownload(userSearchCondition: UserSearchCondition): ResponseEntity<ByteArray> {
        val returnDto = userRepository.findUserListForExcel(userSearchCondition)
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
                                    cellWidth = 7000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.department"),
                                    cellWidth = 4000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("user.label.position"),
                                    cellWidth = 4000
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
                                    cellWidth = 4000
                                )
                            )
                        )
                    )
                )
            )
        )
        returnDto.forEach { result ->
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
                            value = if (result.useYn) {
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

    /**
     * ????????? ?????? ????????? ?????? ??????
     */
    fun getUserListInOrganization(organizationIds: Set<String>): List<AliceUserEntity> {
        return userRepository.getUserListInOrganization(organizationIds)
    }

    /**
     * ?????? ?????? ?????? ??????
     */
    fun userSessionRoleCheck(userKey: String, roleIds: Set<String>): String {
        var code = ZResponseConstants.STATUS.SUCCESS.code
        if (userKey != currentSessionUser.getUserKey()) {
            when (roleIds.isEmpty()) {
                true -> code = ZResponseConstants.STATUS.ERROR_FAIL.code
                false -> {
                    var hasRole = false
                    run loop@{
                        roleService.getUserRoleList(currentSessionUser.getUserKey()).forEach {
                            if (roleIds.contains(it.roleId)) {
                                hasRole = true
                                return@loop
                            }
                        }
                    }
                    if (!hasRole) {
                        code = ZResponseConstants.STATUS.ERROR_FAIL.code
                    }
                }
            }
        }
        return code
    }

    /**
     * ???????????? ???????????? ?????? ??????
     */
    fun userAccessAuthCheck(createUserKey: String, auths: String?) {
        val roleIds: MutableSet<String> = mutableSetOf()
        val roleDtoList = mutableListOf<RoleListDto>()
        if (!auths.isNullOrEmpty()) {
            roleDtoList.addAll(aliceRoleAuthMapRepository.findRoleByAuths(auths))
            for (role in roleDtoList) {
                roleIds.add(role.roleId)
            }
        } else {
            roleIds.add(AliceConstants.SYSTEM_ROLE)
        }
        val result = this.userSessionRoleCheck(createUserKey, roleIds)
        if (result != ZResponseConstants.STATUS.SUCCESS.code) {
            throw AliceException(
                AliceErrorConstants.ERR_00002,
                aliceMessageSource.getMessage("auth.msg.accessDenied")
            )
        }
    }

    /**
     * ????????? ???????????? ?????? ??? rsa key ??????
     */
    fun rsaKeySend(request: HttpServletRequest): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        val session = request.getSession(true)
        session.removeAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value)
        session.setAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value, aliceCryptoRsa.getPrivateKey())

        map[AliceConstants.RsaKey.PUBLIC_MODULE.value] = aliceCryptoRsa.getPublicKeyModulus()
        map[AliceConstants.RsaKey.PUBLIC_EXPONENT.value] = aliceCryptoRsa.getPublicKeyExponent()

        return map
    }

    /**
     * ????????? ???????????? ??????
     */
    fun userPasswordConfirm(data: HashMap<String, Any>): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val password = aliceCryptoRsa.decrypt(privateKey, data.getValue("password") as String)
        val userEntity = this.selectUserKey(currentSessionUser.getUserKey())

        when (this.algorithm.toUpperCase()) {
            AliceConstants.EncryptionAlgorithm.BCRYPT.value -> {
                if (!BCryptPasswordEncoder().matches(password, userEntity.password)) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
            }
            AliceConstants.EncryptionAlgorithm.AES256.value, AliceConstants.EncryptionAlgorithm.SHA256.value -> {
                val encryptPassword = aliceEncryptionUtil.encryptEncoder(password, this.algorithm)
                if (encryptPassword != userEntity.password) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
            }
            else -> {
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * ???????????? ????????? ??????
     */
    fun passwordValidationCheck(password: String, userId: String, email: String?): Boolean {
        val upperCaseIncludeReg = """[A-Z]""".toRegex()
        val lowerCaseIncludeReg = """[a-z]""".toRegex()
        val integerIncludeReg = """[0-9]""".toRegex()
        val specialCharIncludeReg = """[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]""".toRegex()

        val upperCaseReg = "^[A-Z]*$".toRegex()
        val lowerCaseReg = "^[a-z]*$".toRegex()
        val integerReg = "^[0-9]*$".toRegex()
        val specialCharReg = "^[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\=\\(\\'\"]*$".toRegex()
        val blankReg = """[\s]""".toRegex()

        val containsUpperCase = upperCaseIncludeReg.containsMatchIn(password)
        val containsLowerCase = lowerCaseIncludeReg.containsMatchIn(password)
        val containsIntegerCase = integerIncludeReg.containsMatchIn(password)
        val containsSpecialCharCase = specialCharIncludeReg.containsMatchIn(password)

        // 1????????? ?????? ????????? ?????? 10??? ??????, 20??? ????????? ??????????????? ????????????.
        // ?????? ?????? : ?????????, ?????????, ???????????? , ??????
        if (password.matches(upperCaseReg) || password.matches(lowerCaseReg) || password.matches(integerReg) || password.matches(
                specialCharReg
            )
        ) {
            if (password.length < 10 || password.length > 20) {
                return false
            }
        }

        // 2????????? ?????? ????????? ?????? 8??? ??????, 20??? ????????? ??????????????? ????????????.
        // ?????? ?????? : ?????????, ?????????, ???????????? , ??????
        if ((!containsUpperCase && !containsLowerCase) || (!containsLowerCase && !containsSpecialCharCase) ||
            (!containsSpecialCharCase && !containsIntegerCase) || (!containsIntegerCase && !containsUpperCase) ||
            (!containsUpperCase && !containsSpecialCharCase) || (!containsLowerCase && !containsIntegerCase)
        ) {
            if (password.length < 8 || password.length > 20) {
                return false
            }
        }

        // ??????????????? ????????? ???????????? ?????????.
        if (blankReg.containsMatchIn(password)) {
            return false
        }

        // ??????????????? ???????????? ID??? ???????????? ?????????.
        if (password.contains(userId)) {
            return false
        }

        // ??????????????? ???????????? ????????? ID??? ???????????? ?????????.
        if (!email.isNullOrBlank()) {
            val emailId = email.split("@")[0]
            when (password.contains(emailId)) {
                true -> return false
            }
        }

        return true
    }

    /**
     * ???????????? ????????? ?????????????????? [userEntity] ,[avatarUUID] ??? ????????? ????????????.
     *
     * @param userEntity
     * @param avatarUUID
     */
    private fun setUploadAvatar(userEntity: AliceUserEntity, avatarUUID: String) {
        when (avatarUUID.isNotEmpty()) {
            true -> {
                val tempDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR_TEMP.code)
                val tempPath = Paths.get(tempDir.toString() + File.separator + avatarUUID)

                if (tempPath.toFile().exists()) {
                    val avatarDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR.code)
                    val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + avatarUUID)
                    Files.move(tempPath, avatarFilePath, StandardCopyOption.REPLACE_EXISTING)
                    userEntity.avatarValue = avatarUUID
                    userEntity.uploaded = true
                    userEntity.uploadedLocation = avatarFilePath.toString()
                }
            }
            false -> {
                val uploadedFile = Paths.get(userEntity.uploadedLocation)

                if (uploadedFile.toFile().exists()) {
                    Files.delete(uploadedFile)
                }
                userEntity.avatarValue = UserConstants.AVATAR_BASIC_FILE_NAME
                userEntity.uploaded = false
                userEntity.uploadedLocation = UserConstants.AVATAR_BASIC_FILE_PATH
            }
        }
    }

    /**
     * ????????? ??????????????? uuid ?????? ID ????????? ?????? ??????.
     * ?????? ????????? ?????? ??? avatar_id, user_key ??? ?????? ?????? ?????? ?????????
     * ??????????????? ????????? avatar_uuid ??? ???????????? ?????????. avatar_uuid ??? ?????? ?????? ?????? ?????? ????????? ?????????
     * ?????????, ????????? ????????? ?????? ??? ?????? ?????? ????????? ??? ????????? ??????????????? ????????????.
     *
     * @param userEntity
     */
    private fun setAvatarName(userEntity: AliceUserEntity) {
        if (userEntity.avatarType == UserConstants.AvatarType.FILE.code &&
            userEntity.userKey != userEntity.avatarValue
        ) {
            val avatarDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR.code)
            val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.avatarValue)
            val avatarIdFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.userKey)
            val avatarUploadFile = File(avatarFilePath.toString())
            if (avatarUploadFile.exists()) {
                Files.move(avatarFilePath, avatarIdFilePath, StandardCopyOption.REPLACE_EXISTING)
                userEntity.avatarValue = userEntity.userKey
                userEntity.uploadedLocation = avatarIdFilePath.toString()
                userRepository.save(userEntity)
            }
        }
    }
}

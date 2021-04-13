/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.fileTransaction.repository.AliceFileNameExtensionRepository
import co.brainz.framework.util.AliceFileUtil
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AliceFileAvatarService(
    private val aliceFileNameExtensionRepository: AliceFileNameExtensionRepository,
    private val userRepository: AliceUserRepository,
    environment: Environment
) : AliceFileUtil(environment) {

    /**
     * 회원 가입 시 아바타 파일[multipartFile]를 받아서 임시 폴더에[avatar/temp/]저장 한다.
     */
    fun uploadTempAvatarFile(multipartFile: MultipartFile, fileName: String?) {
        val fileNameExtension = File(multipartFile.originalFilename!!).extension.toUpperCase()
        val filePath: Path
        var dir = super.getPath(AliceUserConstants.AVATAR_IMAGE_TEMP_DIR)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)

        filePath = when (fileName) {
            null -> {
                Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)
            }
            else -> {
                Paths.get(dir.toString() + File.separator + fileName)
            }
        }

        if (aliceFileNameExtensionRepository.findById(fileNameExtension).isEmpty) {
            throw AliceException(AliceErrorConstants.ERR_00001, "The file extension is not allowed.")
        }

        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 업로드한 아바타 이미지정보를 [userEntity] ,[avatarUUID] 를 받아서 처리한다.
     */
    fun uploadAvatarFile(userEntity: AliceUserEntity, avatarUUID: String) {
        val tempDir = super.getPath(AliceUserConstants.AVATAR_IMAGE_TEMP_DIR)
        val tempPath = Paths.get(tempDir.toString() + File.separator + avatarUUID)
        val tempFile = File(tempPath.toString())

        val avatarDir = super.getPath(AliceUserConstants.AVATAR_IMAGE_DIR)
        val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + avatarUUID)

        if (avatarUUID !== "" && tempFile.exists()) {
            Files.move(tempPath, avatarFilePath, StandardCopyOption.REPLACE_EXISTING)
            userEntity.avatarValue = avatarUUID
            userEntity.uploaded = true
            userEntity.uploadedLocation = avatarFilePath.toString()
        } else {
            if (avatarUUID == "" || !userEntity.uploaded) {
                val uploadedFile = Paths.get(userEntity.uploadedLocation)
                if (uploadedFile.toFile().exists()) {
                    Files.delete(uploadedFile)
                }
                userEntity.avatarValue = AliceUserConstants.AVATAR_BASIC_FILE_NAME
                userEntity.uploaded = false
                userEntity.uploadedLocation = AliceUserConstants.AVATAR_BASIC_FILE_PATH
            }
        }
    }

    /**
     * 아바타 이미지명을 uuid 에서 ID 값으로 변경 한다.
     * 신규 사용자 등록 시 avatar_id, user_key 를 구할 수가 없기 때문에
     * 임시적으로 생성한 avatar_uuid 로 파일명을 만든다. avatar_uuid 가 고유 값을 보장 하지 못하기 때문에
     * 사용자, 아바타 정보를 등록 후 다시 한번 파일명 및 아바타 이미지명을 변경한다.
     */
    fun avatarFileNameMod(userEntity: AliceUserEntity) {
        if (userEntity.avatarType == AliceUserConstants.AvatarType.FILE.code &&
            userEntity.uploaded && userEntity.userKey != userEntity.avatarValue
        ) {
            val avatarDir = super.getPath(AliceUserConstants.AVATAR_IMAGE_DIR)
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

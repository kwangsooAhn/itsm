package co.brainz.itsm.certification


import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.itsm.common.CodeRepository
import co.brainz.itsm.common.Constants
import co.brainz.itsm.user.RoleRepository
import co.brainz.itsm.user.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.security.PrivateKey
import java.time.LocalDateTime

@Service
class CertificationService(private val certificationRepository: CertificationRepository,
                           private val roleRepository: RoleRepository,
                           private val codeRepository: CodeRepository,
                           private val cryptoRsa: CryptoRsa) {


    /**
     * 사용자를 등록한다.
     */
    fun insertUser(signUp: SignUpDto): Int {
        // 가입 성공 : 0, 가입 실패 - 아이디 중복 : 1, 가입 실패 : 2
        var result = 2
        if (certificationRepository.findByIdOrNull(signUp.userId) == null) {
            // 패스워드 RSA 복호화 후 암호화.
            val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
            val password = cryptoRsa.decrypt(privateKey, signUp.password)

            // 코드 테이블에서 설정된 역할 등록.
            val codeEntityList = codeRepository.findByPCode(CertificationConstants.USER_DEFAULT_ROLE.code)
            var roleIdList = mutableListOf<String>()
            codeEntityList.forEach {
                it.value?.let { value -> roleIdList.add(value) }
            }
            val roleEntityList = roleRepository.findByRoleIdIn(roleIdList)
            // Dto to Entity.
            val userEntity = UserEntity(
                    userId = signUp.userId,
                    password = BCryptPasswordEncoder().encode(password),
                    userName = signUp.userName,
                    email = signUp.email,
                    position = signUp.position,
                    department = signUp.department,
                    extensionNumber = signUp.extensionNumber,
                    createUserid = Constants.CREATE_USER_ID,
                    createDt = LocalDateTime.now(),
                    expiredDt = LocalDateTime.now().plusMonths(3),
                    roleEntities = roleEntityList
            )
            certificationRepository.save(userEntity)
            result = 0
        } else {
            result = 1
        }
        return result
    }
}
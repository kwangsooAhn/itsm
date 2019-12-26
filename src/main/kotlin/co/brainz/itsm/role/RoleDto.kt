package co.brainz.itsm.role

import java.time.LocalDateTime
import co.brainz.itsm.auth.AuthEntity
import org.springframework.format.annotation.DateTimeFormat
import com.fasterxml.jackson.annotation.JsonFormat

/**
 * 역할 조회시 사용한다.
 */
data public class RoleDto(
    var roleId: String?,
    var roleName: String?,
    var roleDesc: String?,
    var createUserid: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var createDt: LocalDateTime?,
    var updateUserid: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var updateDt: LocalDateTime?,
    var arrAuthId: Array<String>?,
    var arrAuthList: List<AuthEntity>?
)

package co.brainz.itsm.code.service

import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CodeService {

    @Autowired
    lateinit var codeRepository: CodeRepository

    fun selectCodeByParent(pCode: String): MutableList<CodeEntity> {
        return codeRepository.findByPCode(pCode)
    }
}

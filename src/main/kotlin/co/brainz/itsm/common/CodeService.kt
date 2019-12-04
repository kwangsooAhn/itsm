package co.brainz.itsm.common

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
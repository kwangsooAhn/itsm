package co.brainz.itsm.code.service

import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import org.springframework.stereotype.Service

@Service
class CodeService(private val codeRepository: CodeRepository) {

    fun selectCodeByParent(code: String): MutableList<CodeEntity> {
        return codeRepository.findByPCode(codeRepository.findById(code).orElse(CodeEntity(code = code)))
    }
}

package co.brainz.itsm.layout.controller.menu

import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.repository.AliceMenuRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class AliceMenuService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var aliceMenuRepository: AliceMenuRepository

    // @Throws(EmptyResultDataAccessException::class)
    fun getMenuList(authList: MutableList<String>): List<AliceMenuEntity> {
        return aliceMenuRepository.findByMenuIdIn(authList)
    }
}

package co.brainz.itsm.meun.service

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.menu.MenuRepository
import co.brainz.itsm.menu.MenuEntity

@Service
public open class MenuService {

	companion object {
		private val logger = LoggerFactory.getLogger(MenuService::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "MenuService")
	}

	@Autowired
	lateinit var menuRepository: MenuRepository

	public fun getMenuList(): MutableList<MenuEntity> {
		return menuRepository.findByMenuId()
	}

	public fun getSubMenuList(menuId: String?): MutableList<MenuEntity> {
		return menuRepository.findBySubMenuId(menuId)
	}
}
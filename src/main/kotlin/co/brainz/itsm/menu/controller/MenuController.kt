package co.brainz.itsm.menu.controller

import org.springframework.stereotype.Controller
import org.slf4j.LoggerFactory
import javax.annotation.Resource
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import co.brainz.itsm.meun.service.MenuService
import co.brainz.itsm.menu.MenuEntity
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
public class MenuController {

	companion object {
		private val logger = LoggerFactory.getLogger(MenuController::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "MenuController")
	}

	@Resource(name = "menuService")
	private lateinit var menuService: MenuService

	@GetMapping("/processList")
	public fun menuRedirect(@RequestParam("menuId") menuId: String?, redirectAttributes: RedirectAttributes): String {
		val menuList: MutableList<MenuEntity> = menuService.getMenuList()
		val firstMenuId = menuList.first().menuId


		if (menuId == null) {
			redirectAttributes.addAttribute("menuId", firstMenuId)
		} else {
			redirectAttributes.addAttribute("menuId", menuId)
		}

		return "redirect:/redirect";
	}

	@GetMapping("/redirect")
	public fun menuRequest(@RequestParam("menuId") menuId: String?, model: Model): String {
		val menuList: MutableList<MenuEntity> = menuService.getMenuList()
		val subMenuList: MutableList<MenuEntity> = menuService.getSubMenuList(menuId)

		model.addAttribute("menuList", menuList)
		model.addAttribute("subMenuList", subMenuList)
		
		return "common/layout"
	}
}
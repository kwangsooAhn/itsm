package co.brainz.itsm.menu

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Repository
public interface MenuRepository : JpaRepository<MenuEntity, String> {
	@Query("select a from MenuEntity a where a.pMenuId is null")
	fun findByMenuId() : MutableList<MenuEntity>
	
	@Query("select a from MenuEntity a where a.pMenuId = ?1") 
	fun findBySubMenuId(pMenuId : String?) : MutableList<MenuEntity>
	
	override fun findAll() : MutableList<MenuEntity>
}
package co.brainz.itsm.menu

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column

@Entity
@Table(name = "bwfMenuMst")
public data class MenuEntity(
    @Column(name = "menuUrl") var pmenuId : String ? = null,
	@Column(name = "msgCode") var msgCode : String ? = null,
	@Column(name = "pMenuId") var pMenuId : String ? = null,
	@Column(name = "useYn") var useYN : Boolean ? = false
    ) : Serializable {
	
	companion object {
		private final val serialVersionUID : Long = -2343243243242432341L;
	}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column("menuId")
	var menuId : String? = null 
}
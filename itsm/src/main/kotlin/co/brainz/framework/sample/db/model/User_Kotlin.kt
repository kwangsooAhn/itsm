/*
package co.brainz.framework.sample.db.model

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userInfo")
public data class User_Kotlin(
	@Column(name = "userId")
    var userId : String? = null,	
    @Column(name = "userName")
    var userName : String? = null) : Serializable
    {
	companion object {
		private final val serialVersionUID : Long = -2343243243242432341L
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long? = null
	
    override fun toString() : String {
		return ("User Info [id=$id, userId=$userId, userName=$userName]")
	}

}
*/
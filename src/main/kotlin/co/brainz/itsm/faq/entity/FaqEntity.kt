package co.brainz.itsm.faq.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

/**
 * ### FAQ 엔티티 클래스.
 *
 * @author Jung heechan
 */
@Entity
@Table(name = "portal_faq")
public data class FaqEntity(

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "faq_id")
    var faqId: String = "",

    @Column(name = "faq_group")
    var faqGroup: String = "",

    @Column(name = "faq_title")
    var faqTitle: String = "",

    @Column(name = "faq_content")
    var faqContent: String = "",

    @Column(name = "create_dt")
    var createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "create_userid")
    var createUserid: String?,

    @Column(name = "update_dt")
    var updateDt: LocalDateTime?,

    @Column(name = "update_userid")
    var updateUserid: String?

) : Serializable

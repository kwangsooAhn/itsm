package co.brainz.itsm.faq.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "portalFaq")
public data class FaqEntity(
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid") @Column var faqId: String = "",
    @Column var faqGroup: String?,
    @Column var faqTitle: String?,
    @Column var faqContent: String?,
    @Column var createDt: LocalDateTime = LocalDateTime.now(),
    @Column var createUserid: String?,
    @Column var updateDt: LocalDateTime?,
    @Column var updateUserid: String?

) : Serializable {
}
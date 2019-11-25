package co.brainz.itsm.faq.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime

@Entity
@Table(name = "awfFaq")
public data class FaqEntity(
    @Id @GeneratedValue @Column var faqId: String,
    @Column var faqGroup: String,
    @Column var faqTitle: String,
    @Column var fqaContents: String,
    @Column var createDt: LocalDateTime,
    @Column var createUserid: String,
    @Column var updateDt: LocalDateTime,
    @Column var updateUserid: String

) : Serializable {
}
    
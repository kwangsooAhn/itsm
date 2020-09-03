/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.portal.dto.PortalDto
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.NamedNativeQueries
import javax.persistence.NamedNativeQuery
import javax.persistence.SqlResultSetMapping
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@SqlResultSetMapping(
    name = "portalSearchMapping",
    classes = (
            arrayOf(
                ConstructorResult(
                    targetClass = PortalDto::class,
                    columns = (
                            arrayOf(
                                ColumnResult(name = "portal_id", type = String::class),
                                ColumnResult(name = "portal_title", type = String::class),
                                ColumnResult(name = "portal_content", type = String::class),
                                ColumnResult(name = "create_dt", type = LocalDateTime::class),
                                ColumnResult(name = "update_dt", type = LocalDateTime::class),
                                ColumnResult(name = "table_name", type = String::class),
                                ColumnResult(name = "total_count", type = Int::class)
                            )
                            )
                )
            )
            )
)

@NamedNativeQueries(
    NamedNativeQuery(
        name = "portalSearchMapping",
        query = "select portal_id, portal_title, portal_content, create_dt, update_dt, table_name, 0 as total_count " +
                "from (" +
                "select notice_no as portal_id, notice_title as portal_title, notice_contents as portal_content, " +
                "create_dt, update_dt, 'notice' table_name " +
                "from portal_notice " +
                "where (lower(notice_title) like lower(concat('%', :searchValue, '%'))) " +
                "union all " +
                "select faq_id as portal_id, faq_title, faq_content, create_dt, update_dt, 'faq' table_name " +
                "from portal_faq " +
                "where (lower(faq_title) like lower(concat('%', :searchValue, '%'))) " +
                "union all " +
                "select download_id as portal_id, download_title, download_category, create_dt, update_dt, 'download' " +
                "table_name " +
                "from awf_download " +
                "where (lower(download_title) like lower(concat('%', :searchValue, '%'))) " +
                ") as portal " +
                "order by create_dt desc limit :limit offset :offset",
        resultSetMapping = "portalSearchMapping",
        resultClass = PortalDto::class
    ),
    NamedNativeQuery(
        name = "portalSearchMappingCount",
        query = "select '' as portal_id, '' as portal_title, '' as portal_content, now() as create_dt, now() as update_dt, " +
                "''as table_name, count(*) as total_count " +
                "from (" +
                "select notice_no as portal_id, notice_title as portal_title, notice_contents as portal_content, " +
                "create_dt, update_dt, 'notice' table_name " +
                "from portal_notice " +
                "where (lower(notice_title) like lower(concat('%', :searchValue, '%'))) " +
                "union all " +
                "select faq_id as portal_id, faq_title, faq_content, create_dt, update_dt, 'faq' table_name " +
                "from portal_faq " +
                "where (lower(faq_title) like lower(concat('%', :searchValue, '%'))) " +
                "union all " +
                "select download_id as portal_id, download_title, download_category, create_dt, update_dt, " +
                "'download' table_name " +
                "from awf_download " +
                "where (lower(download_title) like lower(concat('%', :searchValue, '%'))) " +
                ") as portal ",
        resultSetMapping = "portalSearchMapping",
        resultClass = PortalDto::class
    )
)

@Entity
@Table(name = "portal_notice")
data class NoticeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "notice_no")
    var noticeNo: String = "",

    @Column(name = "notice_title")
    var noticeTitle: String = "",

    @Column(name = "notice_contents")
    var noticeContents: String = "",

    @Column(name = "pop_yn")
    var popYn: Boolean = true,

    @Column(name = "pop_strt_dt")
    var popStrtDt: LocalDateTime? = null,

    @Column(name = "pop_end_dt")
    var popEndDt: LocalDateTime? = null,

    @Column(name = "pop_width")
    var popWidth: Int? = 500,

    @Column(name = "pop_height")
    var popHeight: Int? = 500,

    @Column(name = "top_notice_yn")
    var topNoticeYn: Boolean = true,

    @Column(name = "top_notice_strt_dt")
    var topNoticeStrtDt: LocalDateTime? = null,

    @Column(name = "top_notice_end_dt")
    var topNoticeEndDt: LocalDateTime? = null,

    @ManyToOne(targetEntity = AliceUserEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_key", insertable = false, updatable = false)
    var aliceUserEntity: AliceUserEntity? = null

) : Serializable, AliceMetaEntity()

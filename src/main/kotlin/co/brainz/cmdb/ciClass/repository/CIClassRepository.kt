/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CIClassRepository : JpaRepository<CmdbClassEntity, String>, CIClassRepositoryCustom {

    @Query(
        value = "with recursive results as ("
                + " select * from cmdb_class c1 where c1.class_id = :classId"
                + " union all"
                + " select c2.* from cmdb_class c2"
                + " inner join results r ON r.p_class_id = c2.class_id"
                + " ) select * from results where class_id NOT IN (:classId)", nativeQuery = true
    )
    fun findRecursiveClass(classId: String): List<CmdbClassEntity>
}

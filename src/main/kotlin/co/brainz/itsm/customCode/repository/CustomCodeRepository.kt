package co.brainz.itsm.customCode.repository

import co.brainz.itsm.customCode.entity.CustomCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CustomCodeRepository : JpaRepository<CustomCodeEntity, String>{
    fun existsByCustomCodeName(customCodeName: String): Boolean

    @Query(value = "SELECT count(tablename) FROM pg_tables WHERE tablename = :targetTable", nativeQuery = true)
    fun countByTableName(targetTable: String): Int

    @Query(value = "SELECT count(column_name) FROM information_schema.columns WHERE table_name = :targetTable AND " +
            "column_name = :columnName",
           nativeQuery = true)
    fun countByColumnName(targetTable: String, columnName: String): Int
}
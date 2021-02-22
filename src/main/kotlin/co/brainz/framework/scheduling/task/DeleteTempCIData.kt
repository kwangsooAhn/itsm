package co.brainz.framework.scheduling.task

import java.lang.Exception
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import org.springframework.stereotype.Component

@Component
class DeleteTempCIData : Runnable {

    private final var props: Properties = Properties()

    constructor()
    constructor(args: Any) {
        val params = args as List<*>
        props.setProperty("dbType", params[0].toString())
        props.setProperty("host", params[1].toString())
        props.setProperty("port", params[2].toString())
        props.setProperty("database", params[3].toString())
        props.setProperty("username", params[4].toString())
        props.setProperty("password", params[5].toString())
    }

    override fun run() {
        val query = this.getTempCIDataQuery()
        val conn = this.connection()
        if (conn != null) {
            val preparedStatement = conn.prepareStatement(query)
            preparedStatement.execute()
            preparedStatement.close()
            conn.close()
        }
    }

    fun getTempCIDataQuery(): String {
        val query = StringBuilder()
        query.append(" delete from wf_component_ci_data ")
        query.append(" where instance_id in ( select ci_instance_id as instance_id ")
        query.append("                        from ( select ci.instance_id as ci_instance_id ")
        query.append("                                    , ins.instance_id as ins_instance_id ")
        query.append("                               from wf_component_ci_data ci left join wf_instance ins ")
        query.append("                               on ci.instance_id = ins.instance_id ")
        query.append("                       ) x ")
        query.append(" where ins_instance_id is null) ")

        return query.toString()
    }

    fun connection(): Connection? {
        var conn: Connection? = null
        val url: String?
        try {
            when (props.getProperty("dbType")) {
                "postgresql" -> {
                    url = "jdbc:postgresql://" + props.getProperty("host") + ":" +
                            props.getProperty("port") + "/" + props.getProperty("database")
                    conn =
                        DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"))
                }
                "oracle" -> {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return conn
    }
}

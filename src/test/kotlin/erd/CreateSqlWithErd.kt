import java.io.FileFilter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun main() {
    val executePath = Paths.get("src/main/erd/sql/execute/execute_all_tables.sql")
    val executeSql = executePath.toFile()
    if (executeSql.canRead()) {
        executeSql.delete()
    }
    executeSql.createNewFile()

    val sequenceSqlFiles = Paths.get("src/main/erd/sequence").toFile().listFiles(FileFilter { it.isFile })
    for (sql in sequenceSqlFiles!!) {
        Files.write(executePath, sql.readBytes() + "\n".toByteArray(), StandardOpenOption.APPEND)
    }

    val tableSqlFiles = Paths.get("src/main/erd/sql").toFile().listFiles(FileFilter { it.isFile })
    for (sql in tableSqlFiles!!) {
        Files.write(executePath, sql.readBytes() + "\n".toByteArray(), StandardOpenOption.APPEND)
    }
}

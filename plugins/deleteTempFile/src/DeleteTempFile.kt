
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Properties

class DeleteTempFile {

    private val oneDayAgo = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1)
    private var props: Properties = Properties()

    fun run() {
        val propertiesFile = "plugins/deleteTempFile/src/configuration.properties"
        val inputStream = FileInputStream(propertiesFile)
        props.load(inputStream)

        val fileRootDir = props.getProperty("fileUploadDir")
        val aliceImageTempDir = props.getProperty("aliceImageTempDir")
        val tempDirectoryName = props.getProperty("tempDirectoryName")

        // 아바타 temp 삭제
        val avatarTempDir = fileRootDir + File.separator + aliceImageTempDir
        val avatarFiles = Paths.get(avatarTempDir).toFile().listFiles()
        deleteFiles(avatarFiles)

        // 일반 temp 삭제
        val tempDir = fileRootDir + File.separator + tempDirectoryName
        val tempFiles = Paths.get(tempDir).toFile().listFiles()
        deleteFiles(tempFiles)

        // db file_loc 삭제
        val query = this.getFileLocDeleteQuery()
        val conn = this.connection()
        if (conn != null) {
            val preparedStatement = conn.prepareStatement(query)
            preparedStatement.execute()
            preparedStatement.close()
            conn.close()
        }
    }

    /**
     * 파일을 물리적으로 삭제한다.
     */
    private fun deleteFiles(files: Array<File>?) {
        if (files !== null) {
            for (file in files) {
                confirmAndDelete(file)
            }
        }
    }

    /**
     * 파일 종류에 따라 재귀호출 또는 삭제
     */
    private fun confirmAndDelete(file: File) {
        val filePath = file.toPath()
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files !== null) {
                this.deleteFiles(files)
            }
            Files.deleteIfExists(filePath)
        } else {
            val attribute = Files.readAttributes(filePath, BasicFileAttributes::class.java)
            val fileDateTime = LocalDateTime.ofInstant(attribute.creationTime().toInstant(), ZoneId.of("UTC"))
            val passedOneDay = fileDateTime < this.oneDayAgo
            if (passedOneDay) {
                Files.deleteIfExists(filePath)
            }
        }
    }

    /**
     * DataBase Connection
     */
    private fun connection(): Connection? {
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return conn
    }

    /**
     * awf_file_loc Delete Query
     */
    private fun getFileLocDeleteQuery(): String {
        val query = StringBuilder()
        query.append(" delete from awf_file_loc ")
        query.append(" where uploaded = FALSE and create_dt < ")
        query.append(oneDayAgo)
        return query.toString()
    }
}

fun main(args: Array<String>) {
    val deleteTempFile = DeleteTempFile()
    deleteTempFile.run()
}

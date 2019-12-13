package co.brainz.framework.fileTransaction.service

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

/**
 * 파일 서비스 인터페이스
 */
interface FileService {

    /**
     * 최초 임시경로(temp)에 파일을 업로드하며 파일 정보는 DB에 저장하고 flag 는 false(미사용) 처리한다.
     * @param multipartFile 뷰단에서 넘어온 파일
     */
    fun uploadTemp(multipartFile: MultipartFile): FileLocEntity

    /**
     * 임시저장된 물리적 파일을 실제 사용할 위치로 이동하고 파일 정보가 저장된 DB flag 는 true(사용)로 변경한다.
     * @param fileSeq 파일 고유 시퀀스번호
     */
    fun upload(fileSeq: List<Long>?)

    /**
     * 파일 목록 가져오기
     * @param task 파일 목록을 가져오기 위한 값.
     */
    fun getList(task: String): MutableList<FileLocEntity>

    /**
     * 업로드된 파일을 삭제한다.
     * @param seq 파일 고유 시퀀스 번호
     */
    fun delete(seq: String)

    /**
     * 파일 다운로드.
     * @param seq 파일 고유시퀀스번호
     */
    fun download(seq: Long): ResponseEntity<InputStreamResource>

    /**
     * 파일 다운 샘플용. TODO 삭제할거
     */
    fun download(seq: String): ResponseEntity<InputStreamResource>
}
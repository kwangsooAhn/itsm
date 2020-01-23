package co.brainz.itsm.process.service

import co.brainz.itsm.process.constants.ProcessConstants
import co.brainz.itsm.process.dto.ProcessDto
import co.brainz.itsm.process.dto.ProcessSearchDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ProcessService {

    /**
     * 프로세스 데이터 조회.
     */
    fun findProcessList(search: String): MutableList<ProcessSearchDto> {
        //TODO DB에서 리스트 조회. like 검색 필요. 조회시 form_name join해서 가져와야함.
        //임시 데이터
        val patten: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val processList = mutableListOf<ProcessSearchDto>()
        if (search.isEmpty()) {
            val process1 = ProcessSearchDto(
                    processId = "asd8acbdd784401aaad6d310df85ac2d",
                    processName = "단순문의 프로세스",
                    processDesc = "고객들이 서비스 이용간 단순한 문의사항 접수",
                    formName = "단순문의",
                    createDt = LocalDateTime.parse("2019-12-26 18:20:30", patten),
                    createUserkey = "f65d114e29664e7bbf1d47075379bbb5",
                    //createUserkey = "이소현",
                    processStatus = "process.status.edit",
                    enabled = true
            )
            processList.add(process1)
        }
        val process2 = ProcessSearchDto(
                processId = "qwe8acbdd784401aaad6d310df85ac2d",
                processName = "장애신고 프로세스",
                processDesc = "서비스 장애관련 문의사항 접수",
                formName = "장애신고",
                createDt = LocalDateTime.parse("2019-12-29 20:01:05", patten),
                createUserkey = "f65d114e29664e7bbf1d47075379bbb5",
                //createUserkey = "이소현",
                updateDt = LocalDateTime.parse("2020-01-15 09:10:35", patten),
                updateUserkey = "b0b3209dbe4042498603df7b216c4598",
                //updateUserkey = "관리자",
                processStatus = "process.status.simu",
                enabled = true
        )
        processList.add(process2)

        val process3 = ProcessSearchDto(
                processId = "zxc8acbdd784401aaad6d310df85ac2d",
                processName = "인프라 변경 관리 프로세스",
                processDesc = "인프라 변경 관련 사항 접수",
                formName = "인프라 변경 관리",
                createDt = LocalDateTime.parse("2020-01-05 10:31:55", patten),
                createUserkey = "b0b3209dbe4042498603df7b216c4598",
                //createUserkey = "관리자",
                updateDt = LocalDateTime.parse("2020-01-22 11:31:55", patten),
                updateUserkey = "b0b3209dbe4042498603df7b216c4598",
                //updateUserkey = "관리자",
                processStatus = "process.status.publish",
                enabled = false
        )
        processList.add(process3)

        val process4 = ProcessSearchDto(
                processId = "ghj8acbdd784401aaad6d310df85ac2d",
                processName = "인프라 변경 관리 프로세스",
                processDesc = "인프라 변경 관련 사항 접수22",
                formName = "인프라 변경 관리22",
                createDt = LocalDateTime.parse("2020-01-22 11:20:55", patten),
                createUserkey = "b1b3209dbe4042498603df7b216c4598",
                //createUserkey = "관리자",
                processStatus = "process.status.destroy",
                enabled = false
        )
        processList.add(process4)

        return processList
    }

    /**
     * 프로세스 신규 기본 정보 등록.
     */
    fun insertProcess(processDto: ProcessDto): String {
        //TODO DB에 저장.
        val userName: String = SecurityContextHolder.getContext().authentication.name //사용자 이름
        val status = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태

        //등록된 process_id return
        return "test8cbdd784401aaad6d310df85ac2d"
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String) {
        //TODO DB에서 삭제.
    }
}
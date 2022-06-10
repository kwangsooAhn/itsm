/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql.util

import co.brainz.itsm.zql.const.REGEX_TAG
import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.dto.ZqlCategorizedData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

object ZqlUtil {
    val spELParser: ExpressionParser = SpelExpressionParser()
    val context: EvaluationContext = StandardEvaluationContext(ZqlSpringElContext())

    /**
     * 날짜시간 데이터를 정해진 간격에 따라 카테고리에서 사용할 수 있는 날짜시간 형태로 변경한다.
     * <p>
     * 예를 들어, 카테고리가 년단위라면 2022년의 모든 날짜의 데이터는 2022-01-01T00:00 이라는 카테고리 기준일시로
     * 데이터를 모아야 하기 때문에 카테고리 간격에 따라 맞춰진 시간으로 변경해준다.
     *
     * @param dateTime 변경 대상이 되는 날짜시간
     * @param period 변경 시 기준이 되는 간격으로 년,월,일,시가 가능하다.
     * @return 변경된 LocalDateTime
     * @author Jung Hee Chan
     */
    fun changeToCategoryDT(dateTime: LocalDateTime, period: ZqlPeriodType): LocalDateTime {
        return when (period) {
            ZqlPeriodType.YEAR -> LocalDate.of(dateTime.year, 1, 1).atStartOfDay()
            ZqlPeriodType.MONTH -> LocalDate.of(dateTime.year, dateTime.month, 1).atStartOfDay()
            ZqlPeriodType.DAY -> dateTime.toLocalDate().atStartOfDay()
            ZqlPeriodType.HOUR -> dateTime.withMinute(0).withSecond(0)
        }
    }

    /**
     * 작성된 표현식을 SpEL 을 이용하여 확인한다.
     * <p>
     * 설정된 ZQL 에서 태그 부분을 해당 데이터로 치환한 뒤에 ZQL 표현식을 체크하는데 사용한다.
     * 표현식에 따라서 숫자, 혹은 Boolean 등으로 결과가 나올 수 있다.
     *
     * @param exp 표현식 스트링
     * @return 표현식을 처리한 값. Any 타입으로 리턴하며 사용하는 곳에서 해당 표현식을 감안해 타입을 변경해서 사용해야 한다.
     * @author Jung Hee Chan
     */
    fun checkSpEL(exp: String): Any? {
        return try { spELParser.parseExpression(exp).getValue(context) }
        catch (e: Exception) { null }
    }

    /**
     * ZQL String 에서 태그를 추출한다.
     * <p>
     * ZQL 에서  태그를 사용하기 위해서는 [] 사이에 입력해서 표현하도록 정해져 있다.
     * 여기서는 조건식에 사용된 태그를 추출하여 중복되지 않도록 처리 후 List 로 리턴한다.
     *
     * @param condition ZQL String
     * @return 파싱 결과 추출된 태그의 HashSet Collection.
     * @author Jung Hee Chan
     */
    fun tagParser(condition: String): List<String> {
        return Regex(REGEX_TAG).findAll(condition).map { it.groupValues[1] }.toHashSet().toList()
    }

    /**
     * 특정 기간 범위를 정해진 간격으로 카테고리로 만들어서 반환한다.
     * <p>
     * 예를 들어, 년 단위로 2022년부터 2024년까지로 카테고리를 만들면 2022, 2023, 2024 3개의 카테고리용 날짜가 생겨야 한다.
     * 또는 일단위로 2022년 5월 10일부터 2022년 10월 1일까지 카테고리를 만들면 2022-05-10부터 2022-10-01까지
     * 일자별로 카테고리가 생성된다.
     *
     * 카테고리는 시작일시와 종료일시를 포함하는 개념으로 구하기 때문에
     * 2021년이 하루라도 들어가는 기간에서 년단위인 경우 2021년이 생성된다.
     * 이렇게 만들어진 카테고리는 실제 토큰의 날짜 데이터를 매핑하며 차트에서 날짜축의 기준이 되기도 한다.
     *
     * @param from 카테고리를 시작하는 일시.
     * @param to 카테고리 종료 일시
     * @param period 카테고리 간격
     * @return 카테고리별로 토큰 데이터를 포함할 수 있는 ZqlCategorizedData 의 카테고리 리스트
     */
    fun makeCategory(from: LocalDateTime, to: LocalDateTime, period: ZqlPeriodType): List<ZqlCategorizedData> {
        val periodList = mutableListOf<LocalDateTime>()
        when (period) {
            ZqlPeriodType.YEAR -> {
                for (i in 0..ChronoUnit.YEARS.between(from, to)) {
                    periodList.add(LocalDate.of(from.plusYears(i).year, 1, 1).atStartOfDay())
                }
            }
            ZqlPeriodType.MONTH -> {
                for (i in 0..ChronoUnit.MONTHS.between(from, to)) {
                    val plusMonth = from.plusMonths(i)
                    periodList.add(LocalDate.of(plusMonth.year, plusMonth.month, 1).atStartOfDay())
                }
            }
            ZqlPeriodType.DAY -> {
                for (i in 0..ChronoUnit.DAYS.between(from, to)) {
                    val plusDay = from.plusDays(i)
                    periodList.add(LocalDate.of(plusDay.year, plusDay.month, plusDay.dayOfMonth).atStartOfDay())
                }
            }
            ZqlPeriodType.HOUR -> {
                for (i in 0..ChronoUnit.HOURS.between(from, to)) {
                    val plusHour = from.plusHours(i)
                    periodList.add(LocalDateTime.of(plusHour.year, plusHour.month, plusHour.dayOfMonth, plusHour.hour, 0, 0))
                }
            }
        }
        return periodList.map {
            ZqlCategorizedData(it, mutableListOf())
        }
    }
}

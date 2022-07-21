/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql

import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.util.ZqlUtil
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("ZQLUtil Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class ZqlUtilTest {

    @Test
    @DisplayName("Date formatting - 년,월,일,시에 따라 기준일자 포맷")
    @Order(100)
    fun changeToCategoryDTTest() {
        val testDateTime = LocalDateTime.of(2022, 5, 19, 9, 21, 37)
        val yearDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0)
        val monthDateTime = LocalDateTime.of(2022, 5, 1, 0, 0, 0)
        val dayDateTime = LocalDateTime.of(2022, 5, 19, 0, 0, 0)
        val hourDateTime = LocalDateTime.of(2022, 5, 19, 9, 0, 0)
        println("원래 날짜 : $testDateTime")

        var changedDateTime: LocalDateTime = ZqlUtil.changeToCategoryDT(testDateTime, ZqlPeriodType.YEAR)
        println("년 단위인 경우 기대 날짜 : $yearDateTime, 년 단위인 경우 변경 날짜 : $changedDateTime")
        Assertions.assertTrue(changedDateTime == yearDateTime)

        changedDateTime = ZqlUtil.changeToCategoryDT(testDateTime, ZqlPeriodType.MONTH)
        println("월 단위인 경우 기대 날짜 : $monthDateTime, 월 단위인 경우 변경 날짜 : $changedDateTime")
        Assertions.assertTrue(changedDateTime == monthDateTime)

        changedDateTime = ZqlUtil.changeToCategoryDT(testDateTime, ZqlPeriodType.DAY)
        println("일 단위인 경우 기대 날짜 : $dayDateTime, 일 단위인 경우 변경 날짜 : $changedDateTime")
        Assertions.assertTrue(changedDateTime == dayDateTime)

        changedDateTime = ZqlUtil.changeToCategoryDT(testDateTime, ZqlPeriodType.HOUR)
        println("시 단위인 경우 기대 날짜 : $hourDateTime, 시 단위인 경우 변경 날짜 : $changedDateTime")
        Assertions.assertTrue(changedDateTime == hourDateTime)
    }

    @Test
    @DisplayName("ZQL 표현식 체크")
    @Order(200)
    fun checkExpressionTest() {
        val expressionInt = "1"
        val expressionFloat = "1f"
        val expressionString = "'cake'"
        val expressionPoint = "((1==1)? 10:0) + ((1==2)? 20:0) + ((1==3)? 30:0)"
        val expDate19750623 = "1975-06-23 00:00:00"
        val expDate20210101 = "2021-01-01 00:00:00"
        val tokenDate19750623 = "1975-06-23T00:00:00.000Z"
        val tokenDate20210101 = "2020-01-01T00:00:00.000Z"

        val expDateEqT = "equalTo('$expDate19750623','$expDate19750623')"
        val expDateNeqT = "notEqualTo('$expDate20210101','$expDate19750623')"
        val expDateGtT = "greaterThan('$expDate20210101','$expDate19750623')"
        val expDateLtT = "lessThan('$expDate19750623','$expDate20210101')"
        val expDateGteGT = "greaterThanOrEqualTo('$expDate20210101','$expDate19750623')"
        val expDateGteET = "greaterThanOrEqualTo('$expDate19750623','$expDate19750623')"
        val expDateLteLT = "lessThanOrEqualTo('$expDate19750623','$expDate20210101')"
        val expDateLteET = "lessThanOrEqualTo('$expDate20210101','$expDate20210101')"

        val expDateEqF = "equalTo('$expDate20210101','$expDate19750623')"
        val expDateNeqF = "notEqualTo('$expDate19750623','$expDate19750623')"
        val expDateGtF = "greaterThan('$expDate19750623','$expDate20210101')"
        val expDateLtF = "lessThan('$expDate20210101','$expDate19750623')"
        val expDateGteF = "greaterThanOrEqualTo('$expDate19750623','$expDate20210101')"
        val expDateLteF = "lessThanOrEqualTo('$expDate20210101','$expDate19750623')"

        val tokenDateEqT = "equalTo('$tokenDate19750623','$tokenDate19750623')"
        val tokenDateNeqT = "notEqualTo('$tokenDate20210101','$tokenDate19750623')"
        val tokenDateGtT = "greaterThan('$tokenDate20210101','$tokenDate19750623')"
        val tokenDateLtT = "lessThan('$tokenDate19750623','$tokenDate20210101')"
        val tokenDateGteGT = "greaterThanOrEqualTo('$tokenDate20210101','$tokenDate19750623')"
        val tokenDateGteET = "greaterThanOrEqualTo('$tokenDate19750623','$tokenDate19750623')"
        val tokenDateLteLT = "lessThanOrEqualTo('$tokenDate19750623','$tokenDate20210101')"
        val tokenDateLteET = "lessThanOrEqualTo('$tokenDate20210101','$tokenDate20210101')"

        val tokenDateEqF = "equalTo('$tokenDate20210101','$tokenDate19750623')"
        val tokenDateNeqF = "notEqualTo('$tokenDate19750623','$tokenDate19750623')"
        val tokenDateGtF = "greaterThan('$tokenDate19750623','$tokenDate20210101')"
        val tokenDateLtF = "lessThan('$tokenDate20210101','$tokenDate19750623')"
        val tokenDateGteF = "greaterThanOrEqualTo('$tokenDate19750623','$tokenDate20210101')"
        val tokenDateLteF = "lessThanOrEqualTo('$tokenDate20210101','$tokenDate19750623')"

        Assertions.assertTrue((ZqlUtil.checkSpEL(expressionInt) as Int) == 1)
        Assertions.assertTrue((ZqlUtil.checkSpEL(expressionFloat) as Float) == 1f)
        Assertions.assertTrue((ZqlUtil.checkSpEL(expressionString) as String) == "cake")
        Assertions.assertTrue((ZqlUtil.checkSpEL(expressionPoint) as Int) == 10)

        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateEqT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateNeqT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateGtT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateLtT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateGteGT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateGteET) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateLteLT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(expDateLteET) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateEqF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateNeqF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateGtF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateLtF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateGteF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(expDateLteF) as Boolean)

        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateEqT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateNeqT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateGtT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateLtT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateGteGT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateGteET) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateLteLT) as Boolean)
        Assertions.assertTrue(ZqlUtil.checkSpEL(tokenDateLteET) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateEqF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateNeqF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateGtF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateLtF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateGteF) as Boolean)
        Assertions.assertFalse(ZqlUtil.checkSpEL(tokenDateLteF) as Boolean)
    }

    @Test
    @DisplayName("ZQL 표현식에서 태그 추출하기")
    @Order(300)
    fun tagParserTest() {
        val tagString1 = "[tag]" // 태그 1개
        val tagString2 = "[tag" // 태그 없음
        val tagString3 = "[tag1][tag2]" // 태그 복수개
        val tagString4 = "tag" // 태그 없음
        val tagString5 = "[tag1][tag2][tag1]" // 중복 태그 포함
        val tagString6 = "" // 빈값

        println(ZqlUtil.tagParser(tagString1))
        Assertions.assertEquals(listOf("tag"), ZqlUtil.tagParser(tagString1))
        Assertions.assertEquals(listOf<String>(), ZqlUtil.tagParser(tagString2))
        Assertions.assertEquals(listOf("tag1", "tag2"), ZqlUtil.tagParser(tagString3))
        Assertions.assertEquals(listOf<String>(), ZqlUtil.tagParser(tagString4))
        Assertions.assertEquals(listOf("tag1", "tag2"), ZqlUtil.tagParser(tagString5))
        Assertions.assertEquals(listOf<String>(), ZqlUtil.tagParser(tagString6))
    }

    @Test
    @DisplayName("카테고리 만들기")
    @Order(400)
    fun makeCategoryTest() {
        val from = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
        val to = LocalDateTime.of(2023, 12, 31, 23, 59, 59)

        Assertions.assertEquals(3, ZqlUtil.makeCategory(from, to, ZqlPeriodType.YEAR).size)
        Assertions.assertEquals(36, ZqlUtil.makeCategory(from, to, ZqlPeriodType.MONTH).size)
        Assertions.assertEquals(365 * 3, ZqlUtil.makeCategory(from, to, ZqlPeriodType.DAY).size)
        Assertions.assertEquals(365 * 3 * 24, ZqlUtil.makeCategory(from, to, ZqlPeriodType.HOUR).size)
    }
}

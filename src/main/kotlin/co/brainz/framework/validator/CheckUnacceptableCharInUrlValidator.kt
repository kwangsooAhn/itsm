/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.framework.validator

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 문자열에 url 호출시 허용하지 않는 특수문자가 포함되어 있는지 확인한다.
 */
class CheckUnacceptableCharInUrlValidator :
    ConstraintValidator<CheckUnacceptableCharInUrl, String> {
    override fun initialize(constraintAnnotation: CheckUnacceptableCharInUrl) {}
    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        val unacceptableChar = "[/\\\\%;]".toRegex()
        return !unacceptableChar.containsMatchIn(value)
    }
}

@Target(AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CheckUnacceptableCharInUrlValidator::class])
annotation class CheckUnacceptableCharInUrl(
    val message: String = "{validation.msg.unacceptableCharacters}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []

)

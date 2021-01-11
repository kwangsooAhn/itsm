/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.configuration

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanNameGenerator

/**
 * bean 생성시 class 명이 아닌 full package 명이 bean 이름으로 결정되도록 확장.
 */
class AliceFullBeanNameGenerator : BeanNameGenerator {
    override fun generateBeanName(definition: BeanDefinition, registry: BeanDefinitionRegistry): String {
        return definition.beanClassName!!
    }
}

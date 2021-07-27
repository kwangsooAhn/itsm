/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.product

import co.brainz.itsm.product.dto.ProductInfoDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/products")
class ProductRestController {

    @Value("\${product.version}")
    lateinit var productVersion: String

    /**
     * application.yml 파일에 위치한 제품 정보 가져오기
     */
    @GetMapping("/info")
    fun getProductInfo(): ProductInfoDto {
        return ProductInfoDto(
            version = productVersion
        )
    }
}

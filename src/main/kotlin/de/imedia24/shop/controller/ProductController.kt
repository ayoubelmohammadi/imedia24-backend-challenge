package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.exception.ProductAlreadyExistsException
import de.imedia24.shop.service.ProductService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam
import kotlin.reflect.jvm.internal.impl.resolve.constants.KClassValue

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @ApiOperation(value = "View a product by its SKU", response = ProductResponse::class)
    @GetMapping("/products/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
            @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")
        val product = productService.findProductBySku(sku)
        return if (product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product)
        }
    }

    @ApiOperation(value = "Get list of products by SKUs list", response = ProductResponse::class)
    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findProductsBySkuList(
            @RequestParam skus: List<String>
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Request for product list by skus ${skus.joinToString { "," }}")
        val products = productService.findProductsBySkuList(skus)
        return if (products.isEmpty()) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(products)
        }
    }


    @ApiOperation(value = "Add new product ", response = ProductResponse::class)
    @PostMapping("/products", produces = ["application/json;charset=utf-8"])
    fun addProduct(@RequestBody productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        logger.info("Add new product ${productRequest.sku}")
        return try {
            val productResponse = productService.addProduct(productRequest)
            ResponseEntity.ok(productResponse)
        } catch (exception: ProductAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @ApiOperation(value = "Update product partially (updating name, description and price). by SKY ", response = ProductResponse::class)
    @PutMapping("/products/{sku}")
    fun updateProduct(@PathVariable sku: String, @RequestBody productUpdateRequest: ProductUpdateRequest): ResponseEntity<ProductResponse> {
        return try {
            val productResponse = productService.updateProduct(sku, productUpdateRequest)
            ResponseEntity.ok(productResponse)
        } catch (exception: ProductAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}

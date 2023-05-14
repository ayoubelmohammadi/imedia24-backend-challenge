package de.imedia24.shop
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
@ExtendWith(MockitoExtension::class)
@AutoConfigureMockMvc
@SpringBootTest
class ShopApplicationTests {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@MockBean
	private lateinit var productService: ProductService

	@Test
	fun contextLoads() {
	}


	@Test
	fun shouldUpdateProduct() {
		val sku = "123"
		val productUpdateRequest = ProductUpdateRequest("New Product", "New Description", BigDecimal(100))
		val updatedProduct = ProductResponse(sku, "New Product", "New Description", BigDecimal(100), 10)

		Mockito.`when`(productService.updateProduct(sku, productUpdateRequest)).thenReturn(updatedProduct)

		val jsonRequest = """
            {
                "name": "New Product",
                "description": "New Description",
                "price": 100
            }
        """.trimIndent()

		mockMvc.perform(
				MockMvcRequestBuilders.put("/products/$sku")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest)
		)
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Product"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("New Description"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100))
	}

	@Test
	fun shouldGetPtoductsBySkus() {
		val skus = listOf("123", "456")
		val products = listOf(
				ProductResponse("123", "Product 1", "Description 1", BigDecimal(100), 10),
				ProductResponse("456", "Product 2", "Description 2", BigDecimal(200), 20)
		)

		Mockito.`when`(productService.findProductsBySkuList(skus)).thenReturn(products)

		mockMvc.perform(
				MockMvcRequestBuilders.get("/products?skus=123,456")
						.contentType(MediaType.APPLICATION_JSON)
		)
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].sku").value("123"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Product 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].sku").value("456"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Product 2"))
	}
}

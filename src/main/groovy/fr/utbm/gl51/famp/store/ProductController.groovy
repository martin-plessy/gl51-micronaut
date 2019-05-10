package fr.utbm.gl51.famp.store

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/products")
class ProductController {
	@Get()
	List<Product> getProducts() {
		[]
	}
}

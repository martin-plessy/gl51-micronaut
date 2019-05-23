package fr.utbm.gl51.famp.store

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

import javax.inject.Inject

@Controller("/products")
class ProductController {
	@Inject
	private ProductStorage storage;

	@Get()
	List<Product> get() {
		storage.all()
	}

	@Get("/{id}")
	Product get(String id) {
		 try {
			 storage.getById(id)
		 } catch (NoSuchElementException ignored) {
			 null
		 }
	}

	@Post()
	HttpResponse<String> add(Product product) {
		storage.save(product)
		HttpResponse.created(product.id)
	}
}

package fr.utbm.gl51.famp.store

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put

import javax.inject.Inject

@Controller("/products")
class ProductController {
	@Inject
	private ProductStorage storage

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

	@Put("/{id}")
	HttpStatus update(String id, Product product) {
		try {
			storage.update(id, product)
			HttpStatus.NO_CONTENT
		} catch (NoSuchElementException ignored) {
			null
		}
	}

	@Delete("/{id}")
	HttpStatus delete(String id) {
		storage.delete(id)
		HttpStatus.NO_CONTENT
	}
}

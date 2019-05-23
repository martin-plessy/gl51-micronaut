package fr.utbm.gl51.famp.store

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import static io.micronaut.core.type.Argument.*

class ProductControllerSpec extends Specification {
	@Shared
	@AutoCleanup
	EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

	@Shared
	@AutoCleanup
	HttpClient httpClient = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.getURL())

	BlockingHttpClient getClient() {
		httpClient.toBlocking()
	}

	@Shared
	ProductStorage storage = embeddedServer.applicationContext.getBean(ProductStorage)

	private static final String ENDPOINT_PATH = "/products"

	private List<Product> fillStorage() {
		storage.save(new Product(name: "One"))
		storage.save(new Product(name: "Two"))
		storage.save(new Product(name: "Three"))
		storage.save(new Product(name: "Four"))

		storage.all()
	}

	def "GET /products | no product => 200 empty list"() {
		given: // No product: nothing to do here.
		when: def response = (client.exchange(HttpRequest.GET(ENDPOINT_PATH), listOf(Product)) as HttpResponse<List<Product>>)
		then: response.status() == HttpStatus.OK
		and: response.body() == []
	}

	def "GET /products => 200 all products"() {
		given: def products = fillStorage()
		when: def response = (client.exchange(HttpRequest.GET(ENDPOINT_PATH), listOf(Product)) as HttpResponse<List<Product>>)
		then: response.status() == HttpStatus.OK
		and: response.body() == products
	}

	def "GET /products/<ID> | not exists => 404"() {
		given: fillStorage()
		when: client.exchange(HttpRequest.GET(ENDPOINT_PATH + "/404"), of(Product))
		then:
			def error = thrown(HttpClientResponseException)
			error.response.status() == HttpStatus.NOT_FOUND
	}

	def "GET /products/<ID> | exists => 200 wanted product"() {
		given: def product = fillStorage()[0]
		when: def response = client.exchange(HttpRequest.GET(ENDPOINT_PATH + "/" + product.id), of(Product)) as HttpResponse<Product>
		then: response.status() == HttpStatus.OK
		and: response.body() == product
	}

	def "POST /products => 201 product id"() {
		given: // Nothing to do here.
		when: def response = client.exchange(HttpRequest.POST(ENDPOINT_PATH, new Product()), of(String)) as HttpResponse<String>
		then: response.status() == HttpStatus.CREATED
		and: response.body() != ""
	}

	def "POST /products => can be retrieved back"() {
		given:
			def product = new Product()
			product.id = (client.exchange(HttpRequest.POST(ENDPOINT_PATH, product), of(String)) as HttpResponse<String>).body()
		when: def response = client.exchange(HttpRequest.GET(ENDPOINT_PATH + "/" + product.id), of(Product)) as HttpResponse<Product>
		then: response.status() == HttpStatus.OK
		and: response.body() == product
	}

	def "PUT /products/<ID> | not exists => 404"() {
		given: fillStorage()
		when: client.exchange(HttpRequest.PUT(ENDPOINT_PATH + "/404", new Product()))
		then:
			def error = thrown(HttpClientResponseException)
			error.response.status() == HttpStatus.NOT_FOUND
	}

	def "PUT /products/<ID> | exists => 204"() {
		given: def product = fillStorage()[0]
		when: def response = client.exchange(HttpRequest.PUT(ENDPOINT_PATH + "/" + product.id, new Product(name: "New Name")))
		then: response.status() == HttpStatus.NO_CONTENT
	}

	def "PUT /products/<ID> | exists => is updated"() {
		given:
			def product = fillStorage()[0]
			client.exchange(HttpRequest.PUT(ENDPOINT_PATH + "/" + product.id, new Product(name: "New Name")))
		when: def response = client.exchange(HttpRequest.GET(ENDPOINT_PATH + "/" + product.id), of(Product)) as HttpResponse<Product>
		then: response.status() == HttpStatus.OK
		and: response.body().name == "New Name"
	}

	def "DELETE /products/<ID> | exists => 204"() {
		given: def product = fillStorage()[0]
		when: def response = client.exchange(HttpRequest.DELETE(ENDPOINT_PATH + "/" + product.id))
		then: response.status() == HttpStatus.NO_CONTENT
	}

	def "DELETE /products/<ID> | not exists => 204"() {
		given: fillStorage()
		when: def response = client.exchange(HttpRequest.DELETE(ENDPOINT_PATH + "/404"))
		then: response.status() == HttpStatus.NO_CONTENT
	}

	def "DELETE /products/<ID> | exists => is deleted"() {
		given:
			def product = fillStorage()[0]
			client.exchange(HttpRequest.DELETE(ENDPOINT_PATH + "/" + product.id))
		when: client.exchange(HttpRequest.GET(ENDPOINT_PATH + "/" + product.id), of(Product))
		then:
			def error = thrown(HttpClientResponseException)
			error.response.status() == HttpStatus.NOT_FOUND
	}
}

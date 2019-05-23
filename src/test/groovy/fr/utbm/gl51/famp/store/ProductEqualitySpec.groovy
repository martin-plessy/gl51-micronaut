package fr.utbm.gl51.famp.store

import spock.lang.Specification

class ProductEqualitySpec extends Specification {
	def "Product equality"() {
		expect:
		new Product(name: "A") == new Product(name: "A")
	}

	def "Product inequality"() {
		expect:
		new Product(name: "A") != new Product(name: "B")
	}

	def "List of product equality"() {
		expect:
		[
			new Product(name: "A"),
			new Product(name: "B")
		] == [
			new Product(name: "A"),
			new Product(name: "B")
		]
	}

	def "List of product inequality"() {
		expect:
		[
			new Product(name: "A"),
			new Product(name: "B")
		] != [
			new Product(name: "B"),
			new Product(name: "C")
		]
	}
}

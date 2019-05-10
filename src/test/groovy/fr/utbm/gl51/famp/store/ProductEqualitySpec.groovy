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
}

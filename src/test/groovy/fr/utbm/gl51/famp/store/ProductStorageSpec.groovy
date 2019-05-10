package fr.utbm.gl51.famp.store

import spock.lang.Specification

class InMemoryProductStorageSpec extends Specification {
	ProductStorage products = new InMemoryProductStorage()
	ProductStorage filledProducts
	def someProducts = [
		new Product(name: "One"),
		new Product(name: "Two"),
		new Product(name: "Three")
	]

	def setup() {
		filledProducts = new InMemoryProductStorage()
		someProducts.each {  filledProducts.save(it)}
	}

	def "Empty storage | all => empty list"() {
		expect:
		products.all() == []
	}


	def "Empty storage | save => product retrievable"() {
		setup:
		def newProduct = new Product()
		products.save(newProduct)

		expect:
		products.all() == [ newProduct ]
	}

	def "Empty storage | save => generates id"() {
		setup:
		def newProduct = new Product()
		products.save(newProduct)

		expect:
		products.all().first().id != ""
		newProduct.id != ""
	}

	def "Empty storage | save * N => N product retrievable"() {
		setup:
		def N = 8
		for (i in 1 .. N) products.save(new Product())

		expect:
		products.all().size() == N
	}

	def "Empty storage | save => product retrievable by id"() {
		setup:
		def newProduct = new Product()
		products.save(newProduct)
		def retrievedProduct = products.getById(newProduct.id)

		expect:
		retrievedProduct == newProduct
	}

	def "Filled storage | getById [not exists] => exception"() {
		when:
		def _ = filledProducts.getById("AAAAAC")

		then:
		thrown NoSuchElementException
	}

	def "Empty storage | delete => empty storage"() {
		setup:
		products.delete("AAAAAB")

		expect:
		products.all() == []
	}

	def "Filled storage | delete => product removed"() {
		setup:
		filledProducts.delete(someProducts[1].id)

		expect:
		filledProducts.all() == [ someProducts[0], someProducts[2] ]
	}

	def "Filled storage | delete * 2 => idempotent delete"() {
		setup:
		filledProducts.delete(someProducts[1].id)
		filledProducts.delete(someProducts[1].id)

		expect:
		filledProducts.all() == [ someProducts[0], someProducts[2] ]
	}

	def "Empty storage | update [not exists] => exception"() {
		when:
		products.update("AAAAAD", new Product(name: "Updated"))

		then:
		thrown NoSuchElementException
	}

	def "update storage "() {
		setup:
		filledProducts.update(someProducts[1].id, new Product(name: "Four"))

		expect:
		filledProducts.all() == [
			someProducts[0],
			new Product(id: someProducts[1].id, name: "Four"),
			someProducts[2]
		]
	}
}

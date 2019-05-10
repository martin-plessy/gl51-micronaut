package fr.utbm.gl51.famp.store

import spock.lang.Specification

class InMemoryProductStorageSpec extends Specification {
	ProductStorage emptyStorage = new InMemoryProductStorage()
	ProductStorage filledStorage = new InMemoryProductStorage()

	def sampleProducts = [
		new Product(name: "One"),
		new Product(name: "Two"),
		new Product(name: "Three")
	]

	def setup() {
		sampleProducts.each { filledStorage.save(it) }
	}

	def "Empty storage | all => empty list"() {
		expect:
		emptyStorage.all() == []
	}


	def "Empty storage | save => product retrievable"() {
		setup:
		def newProduct = new Product()
		emptyStorage.save(newProduct)

		expect:
		emptyStorage.all() == [ newProduct ]
	}

	def "Empty storage | save => generates id"() {
		setup:
		def newProduct = new Product()
		emptyStorage.save(newProduct)

		expect:
		emptyStorage.all().first().id != ""
		newProduct.id != ""
	}

	def "Empty storage | save * N => N product retrievable"() {
		setup:
		def N = 8
		for (i in 1 .. N) emptyStorage.save(new Product())

		expect:
		emptyStorage.all().size() == N
	}

	def "Empty storage | save => product retrievable by id"() {
		setup:
		def newProduct = new Product()
		emptyStorage.save(newProduct)
		def retrievedProduct = emptyStorage.getById(newProduct.id)

		expect:
		retrievedProduct == newProduct
	}

	def "Filled storage | getById [not exists] => exception"() {
		when:
		def _ = filledStorage.getById("AAAAAC")

		then:
		thrown NoSuchElementException
	}

	def "Empty storage | delete => empty storage"() {
		setup:
		emptyStorage.delete("AAAAAB")

		expect:
		emptyStorage.all() == []
	}

	def "Filled storage | delete => product removed"() {
		setup:
		filledStorage.delete(sampleProducts[1].id)

		expect:
		filledStorage.all() == [ sampleProducts[0], sampleProducts[2] ]
	}

	def "Filled storage | delete * 2 => idempotent delete"() {
		setup:
		filledStorage.delete(sampleProducts[1].id)
		filledStorage.delete(sampleProducts[1].id)

		expect:
		filledStorage.all() == [ sampleProducts[0], sampleProducts[2] ]
	}

	def "Empty storage | update [not exists] => exception"() {
		when:
		emptyStorage.update("AAAAAD", new Product(name: "Updated"))

		then:
		thrown NoSuchElementException
	}

	def "update storage "() {
		setup:
		filledStorage.update(sampleProducts[1].id, new Product(name: "Four"))

		expect:
		filledStorage.all() == [
			sampleProducts[0],
			new Product(id: sampleProducts[1].id, name: "Four"),
			sampleProducts[2]
		]
	}
}

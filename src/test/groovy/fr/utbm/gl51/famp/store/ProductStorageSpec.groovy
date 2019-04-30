package fr.utbm.gl51.famp.store

import spock.lang.Specification

class InMemoryProductStorageSpec extends Specification {
	def products = new InMemoryProductStorage()

	def "Empty storage | all => empty list"() {
		expect:
		products.all() == []
	}

	def "Empty storage | getById => exception"() {
		when:
		def product = products.getById("AAAAAA")

		then:
		thrown NoSuchElementException
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
		setup:
		for (i in 1 .. 3) products.save(new Product())

		when:
		def product = products.getById("AAAAAB")

		then:
		thrown NoSuchElementException
	}
	def "Empty storage "() {
		setup:
		products.delete("AAAAAA")
		expect:
		products.all().isEmpty()
	}
	def "delete storage"() {
		setup:
		def prod=[new Product(name:"product1"),new Product(name:"product2"),new Product(name:"product3")]
		for (p in prod) products.save(p)

		products.delete(prod[1].id)

		expect:
		products.all()==[prod[0],prod[2]]
	}
	def "delete idempotent"() {
		setup:
		def prod=[new Product(name:"product1"),new Product(name:"product2"),new Product(name:"product3")]
		for (p in prod) products.save(p)

		products.delete(prod[1].id)
		products.delete(prod[1].id)

		expect:
		products.all()==[prod[0],prod[2]]
	}
	def "update storage not exist"() {
		setup:
		def prod=new Product()
		when:
		products.update("aaa",prod)
		then:
		thrown NoSuchElementException
	}
	def "update storage "() {
		setup:
		def prod=[new Product(name:"product1"),new Product(name:"product2"),new Product(name:"product3")]
		for (p in prod) products.save(p)
		products.update(prod[1].id,new Product(name: "new product2"))
		expect:
		products.all()==[prod[0],new Product(id:prod[1].id,name: "new product2"),prod[2]]

	}
}

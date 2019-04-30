package fr.utbm.gl51.famp.store

import java.util.function.Predicate

class InMemoryProductStorage implements ProductStorage {
	private List<Product> savedProducts = []

	@Override
	void save(Product product) {
		product.id = UUID.randomUUID().toString()
		savedProducts += product
	}

	@Override
	Product getById(String id) {
		def foundProduct = savedProducts.find { it.id == id }

		if (foundProduct == null)
			throw new NoSuchElementException("No product with id '$id' exists here.")
		else
			return foundProduct
	}

	@Override
	List<Product> all() {
		savedProducts
	}

	@Override
	void update(String id, Product p) {
		def prod=savedProducts.find{it.id==id }
		if(prod!=null) {
			p.id = id
			savedProducts[savedProducts.indexOf(prod)] = p
		}
		  else {
			throw new NoSuchElementException()
		}
	}

	@Override
	void delete(String id) {
		savedProducts.removeIf{it.id==id}
	}
}


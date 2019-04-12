package fr.utbm.gl51.famp.store

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
		savedProducts == null ? [] : savedProducts // TODO: Remove null check.
	}

	@Override
	void update(String id, Product p) {
	}

	@Override
	void delete(String id) {
	}
}

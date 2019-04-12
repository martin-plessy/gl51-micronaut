package fr.utbm.gl51.famp.store

interface ProductStorage {
	void save(Product product)
	Product getById(String id)
	List<Product> all()
	void update(String id, Product p)
	void delete(String id)
}

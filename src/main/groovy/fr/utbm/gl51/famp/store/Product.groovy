package fr.utbm.gl51.famp.store

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includeFields = true)
class Product {
	String id = ""
	String name = ""
	String description = ""
	double price = 0.0
	double idealTemperature = 0.0
}

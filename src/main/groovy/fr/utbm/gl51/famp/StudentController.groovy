package fr.utbm.gl51.famp

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/students")
class StudentController {
	@Get()
	List<String> index() {
		[ "Faraj Al Btadini", "Martin Plessy" ]
	}
}

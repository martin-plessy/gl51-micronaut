package fr.utbm.gl51.famp

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/student")
class StudentController {
	@Get(produces = MediaType.TEXT_PLAIN)
	String index() {
		return "Faraj Al Btadini, Martin Plessy"
	}
}

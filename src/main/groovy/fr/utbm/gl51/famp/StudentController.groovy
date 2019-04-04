package fr.utbm.gl51.famp

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus

@Controller("/student")
class StudentController {

	@Get("/")
	HttpStatus index() {
		return HttpStatus.OK
	}
}

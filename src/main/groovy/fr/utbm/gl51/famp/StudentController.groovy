package fr.utbm.gl51.famp

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/students")
class StudentController {
	@Get()
	List<Student> getStudents() {
		[
			new Student(firstName: "Faraj", lastName: "Al Btadini"),
			new Student(firstName: "Martin", lastName: "Plessy")
		]
	}
}

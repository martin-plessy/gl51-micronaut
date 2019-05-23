package fr.utbm.gl51.famp

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.jackson.JacksonConfiguration
import io.micronaut.jackson.ObjectMapperFactory
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class StudentControllerSpec extends Specification {
	@Shared
	@AutoCleanup
	EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

	@Shared
	@AutoCleanup
	RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

	@Shared
	ObjectMapper objectMapper = new ObjectMapperFactory().objectMapper(new JacksonConfiguration(), new JsonFactory())

	def "It returns the students' names"() {
		when:
		def response = client.toBlocking().retrieve("/students", Argument.listOf(Student).type)

		then:
		response.collect { objectMapper.convertValue(it, Student) } == [
			new Student(firstName: "Faraj", lastName: "Al Btadini"),
			new Student(firstName: "Martin", lastName: "Plessy")
		]
	}
}

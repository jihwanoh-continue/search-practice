package jihwan.practice.search.configuration.client

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import java.time.Duration
import java.util.Optional

class ApiClient(
    private val client: WebClient,
    private val property: HttpProperty,
) {
    fun <T> getForObject(
        path: String,
        pathVariable: List<Any> = listOf(),
        queryParams: MultiValueMap<String, String>? = null,
        returnType: Class<T>,
        authorizationToken: String? = null,
        timeout: Long? = null,
    ): T? {
        val spec = client.get()
            .uri { builder: UriBuilder ->
                val uriBuilder = builder.path(path)
                queryParams?.let { uriBuilder.queryParams(it) }
                uriBuilder.build(Optional.ofNullable(pathVariable).orElse(listOf()).toTypedArray())
            }
            .headers { headers ->
                headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                authorizationToken?.let { headers.set(HttpHeaders.AUTHORIZATION, "Bearer $it") }
            }

        return spec.retrieve()
            .bodyToMono(returnType)
            .timeout(Duration.ofMillis(timeout ?: property.timeout))
            .block()
    }
}

package jihwan.practice.search.configuration.client

import io.netty.channel.ChannelOption
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider

class WebClientBuilder(private val httpProperty: HttpProperty) {
    private val webClient = WebClient.builder()
    private var enabledDevLogging = false

    // Content-Type은 json으로 기본값 세팅
    private val headers = mutableMapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE)
    private val filters = mutableListOf<ExchangeFilterFunction>()

    fun addHeader(header: Pair<String, String>): WebClientBuilder = headers.put(header.first, header.second).let { this }

    fun build(): WebClient {
        val connectionProvider = ConnectionProvider.builder("custom")
            .pendingAcquireMaxCount(-1) // Optional: 최대 대기열 길이, -1은 무제한을 의미
            .build()

        val httpClientBuilder = HttpClient.create(connectionProvider)
            .headers { headers.forEach { header -> it.add(header.key, header.value) } }
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpProperty.timeout.toInt())

        if (this.enabledDevLogging) {
            addDevLoggingFilter()
        }

        return webClient.baseUrl(httpProperty.url)
            .filters {
                filters.forEach { filter -> it.add(filter) }
            }.clientConnector(ReactorClientHttpConnector(httpClientBuilder)).build()
    }

    private fun addDevLoggingFilter() {
        filters.add(
            ExchangeFilterFunction.ofRequestProcessor { request ->
                val sb = StringBuilder("[${request.method()}] ${request.url()} =====> \n\n")
                sb.append("Header: \n")
                request
                    .headers()
                    .forEach { name, values ->
                        sb.append("$name : [ ")
                        values.forEach { value ->
                            sb.append("$value ,")
                        }
                        sb.append(" ]")
                    }
                sb.append("======>")
                Mono.just(request)
            },
        )
    }

    fun exchangeStrategies(exchangeStrategies: ExchangeStrategies): WebClientBuilder {
        webClient.exchangeStrategies(exchangeStrategies)
        return this
    }

    fun defaultHeaders(block: (HttpHeaders) -> Unit): WebClientBuilder {
        webClient.defaultHeaders(block)
        return this
    }
}

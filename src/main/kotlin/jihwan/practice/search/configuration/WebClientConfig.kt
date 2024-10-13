package jihwan.practice.search.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jihwan.practice.search.client.KakaoPlaceSearchClient
import jihwan.practice.search.configuration.client.ApiClient
import jihwan.practice.search.configuration.client.WebClientBuilder
import jihwan.practice.search.configuration.client.property.KakaoSearchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeStrategies

@Configuration
class WebClientConfig {
    @Bean
    fun kakaoSearchClient(properties: KakaoSearchProperties,objectMapper: ObjectMapper): KakaoPlaceSearchClient {
        val exchangeStrategies = ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper)) }
            .build()

        return KakaoPlaceSearchClient(
            ApiClient(
                client = WebClientBuilder(properties)
                    .exchangeStrategies(exchangeStrategies)
                    .addHeader(Pair("Authorization", "KakaoAK ${properties.apiKey}"))
                    .build(),
                property = properties,
            )
        )
    }
}

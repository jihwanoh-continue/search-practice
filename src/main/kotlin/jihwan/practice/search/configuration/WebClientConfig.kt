package jihwan.practice.search.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jihwan.practice.search.client.KakaoPlaceSearchClient
import jihwan.practice.search.client.NaverPlaceSearchClient
import jihwan.practice.search.configuration.client.ApiClient
import jihwan.practice.search.configuration.client.WebClientBuilder
import jihwan.practice.search.configuration.client.property.KakaoPlaceSearchProperties
import jihwan.practice.search.configuration.client.property.NaverPlaceSearchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeStrategies

@Configuration
class WebClientConfig {
    @Bean
    fun kakaoPlaceSearchClient(properties: KakaoPlaceSearchProperties, objectMapper: ObjectMapper): KakaoPlaceSearchClient {
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

    @Bean
    fun naverPlaceSearchClient(properties: NaverPlaceSearchProperties, objectMapper: ObjectMapper): NaverPlaceSearchClient {
        val exchangeStrategies = ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper)) }
            .build()

        return NaverPlaceSearchClient(
            ApiClient(
                client = WebClientBuilder(properties)
                    .exchangeStrategies(exchangeStrategies)
                    .addHeader(Pair("X-Naver-Client-Id", properties.clientId))
                    .addHeader(Pair("X-Naver-Client-Secret", properties.clientSecret))
                    .build(),
                property = properties,
            )
        )
    }
}

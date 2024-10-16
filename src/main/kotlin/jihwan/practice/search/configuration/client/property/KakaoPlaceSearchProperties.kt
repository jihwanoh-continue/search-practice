package jihwan.practice.search.configuration.client.property

import jihwan.practice.search.configuration.client.HttpProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("client.kakao-place-search")
data class KakaoPlaceSearchProperties(
    override val url: String,
    override val timeout: Long,
    val apiKey: String,
) : HttpProperty

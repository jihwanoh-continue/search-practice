package jihwan.practice.search.configuration.client.property

import jihwan.practice.search.configuration.client.HttpProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("client.naver-place-search")
data class NaverPlaceSearchProperties(
    override val url: String,
    override val timeout: Long,
    val clientId: String,
    val clientSecret: String,
) : HttpProperty

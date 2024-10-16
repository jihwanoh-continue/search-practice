package jihwan.practice.search.client

import com.fasterxml.jackson.annotation.JsonProperty
import jihwan.practice.search.configuration.client.ApiClient
import jihwan.practice.search.util.log.logger
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClientResponseException

class NaverPlaceSearchClient(
    private val client: ApiClient,
) {
    companion object {
        private const val FORMAT = "json"
    }

    private val logger = logger<NaverPlaceSearchResponse>()

    fun search(keyword: String, size: Int): NaverPlaceSearchResponse? {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("query", keyword)
        params.add("display", size.toString())

        return try {
            client.getForObject(
                path = "/search/local.$FORMAT",
                returnType = NaverPlaceSearchResponse::class.java,
                queryParams = params,
            )
        } catch (ex: WebClientResponseException) {
            if (ex.statusCode.is5xxServerError) {
                logger.error("Naver Place Search API 서버 오류", ex)
            }
            null
        }
    }
}

data class NaverPlaceSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>,
)

data class Item(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String,
    val address: String,
    val roadAddress: String,
    @JsonProperty("mapx")
    val x: Int,
    @JsonProperty("mapy")
    val y: Int,
)

package jihwan.practice.search.client

import com.fasterxml.jackson.annotation.JsonProperty
import jihwan.practice.search.configuration.client.ApiClient
import jihwan.practice.search.util.log.logger
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClientResponseException

class KakaoPlaceSearchClient(
    private val client: ApiClient,
) {

    companion object {
        private const val FORMAT = "json"
    }

    private val logger = logger<KakaoPlaceSearchResponse>()

    fun search(keyword: String, size: Int): KakaoPlaceSearchResponse? {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("query", keyword)
        params.add("size", size.toString())

        return try {
            client.getForObject(
                path = "/local/search/keyword.$FORMAT",
                returnType = KakaoPlaceSearchResponse::class.java,
                queryParams = params,
            )
        } catch (ex: WebClientResponseException) {
            if (ex.statusCode.is5xxServerError) {
                logger.error("Kakao Place Search API 서버 오류", ex)
            }
            null
        }
    }
}

// 참고 : https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword-response-body
data class KakaoPlaceSearchResponse(
    val meta: Meta,
    val documents: List<Document>,
)

data class Meta(
    @JsonProperty("total_count")
    val totalCount: Int,
    @JsonProperty("pageable_count")
    val pageableCount: Int,
    @JsonProperty("is_end")
    val isEnd: Boolean,
    @JsonProperty("same_name")
    val sameName: SameName,
)

data class SameName(
    val region: List<String>,
    val keyword: String,
    @JsonProperty("selected_region")
    val selectedRegion: String,
)

data class Document(
    val id: String,
    @JsonProperty("place_name")
    val placeName: String,
    @JsonProperty("category_name")
    val categoryName: String,
    @JsonProperty("category_group_code")
    val categoryGroupCode: String,
    @JsonProperty("category_group_name")
    val categoryGroupName: String,
    val phone: String,
    @JsonProperty("address_name")
    val addressName: String,
    @JsonProperty("road_address_name")
    val roadAddressName: String,
    val x: String,
    val y: String,
    @JsonProperty("place_url")
    val placeUrl: String,
    val distance: String,
)

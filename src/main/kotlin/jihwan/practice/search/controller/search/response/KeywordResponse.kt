package jihwan.practice.search.controller.search.response

import io.swagger.v3.oas.annotations.media.Schema
import jihwan.practice.search.service.dto.Keyword

data class KeywordResponse(
    @get:Schema(description = "검색어", example = "아트빌딩")
    val keyword: String,
    @get:Schema(description = "검색 횟수", example = "10")
    val count: Int,
) {
    companion object {
        fun of(keyword: Keyword): KeywordResponse {
            return KeywordResponse(keyword.keyword, keyword.count)
        }
    }
}

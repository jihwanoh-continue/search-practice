package jihwan.practice.search.controller.search

import io.swagger.v3.oas.annotations.Operation
import jihwan.practice.search.service.dto.Keyword
import jihwan.practice.search.service.dto.Place

interface SearchSpec {
    @Operation(
        summary = "장소 검색 api",
        responses = [],
    )
    suspend fun search(keyword: String): List<Place>

    @Operation(
        summary = "인기 검색어 조회 api",
        responses = [],
    )
    fun getTopKeywords(size: Int = 10): List<Keyword>
}

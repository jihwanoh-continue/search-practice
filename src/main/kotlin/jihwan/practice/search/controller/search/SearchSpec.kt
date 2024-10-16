package jihwan.practice.search.controller.search

import io.swagger.v3.oas.annotations.Operation
import jihwan.practice.search.service.dto.Place

interface SearchSpec {
    @Operation(
        summary = "장소 검색 api",
        responses = [],
    )
    suspend fun search(keyword: String): List<Place>
}

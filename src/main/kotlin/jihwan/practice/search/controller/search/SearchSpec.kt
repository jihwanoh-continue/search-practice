package jihwan.practice.search.controller.search

import io.swagger.v3.oas.annotations.Operation

interface SearchSpec {
    @Operation(
        summary = "장소 검색 api",
        responses = [],
    )
    fun search(keyword: String): List<String>
}

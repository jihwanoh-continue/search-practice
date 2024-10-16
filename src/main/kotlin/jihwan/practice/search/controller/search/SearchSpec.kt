package jihwan.practice.search.controller.search

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jihwan.practice.search.configuration.exception.ErrorResponse
import jihwan.practice.search.controller.search.response.KeywordResponse
import jihwan.practice.search.controller.search.response.PlaceResponse

interface SearchSpec {
    @Operation(
        summary = "장소 검색 api",

        responses = [
            ApiResponse(
                responseCode = "200",
                description = "비밀번호 변경 성공",
            ),
            ApiResponse(
                responseCode = "500",
                description = "external server issue",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "50001",
                                description = "외부 서버에서 오류가 발생했습니다.",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    suspend fun search(keyword: String): List<PlaceResponse>

    @Operation(
        summary = "인기 검색어 조회 api",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "검색어 조회 성공",
            ),
        ],
    )
    fun getTopKeywords(): List<KeywordResponse>
}

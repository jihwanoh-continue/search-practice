package jihwan.practice.search.configuration.exception

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "에러 응답")
class ErrorResponse(
    @get:Schema(description = "에러 메시지", required = true, name = "message", example = "에러 메시지")
    val message: String,
    @get:Schema(description = "에러 상세 코드", required = true, name = "code", example = "40004")
    val code: String,
    @get:Schema(description = "HTTP 상태 코드", required = true, name = "status", example = "400")
    val httpStatus: HttpStatus,
) {
    companion object {
        fun of(exception: Exception): ErrorResponse {
            val dataErrorType = StandardErrorType.of(exception)
            return ErrorResponse(
                message = exception.message ?: dataErrorType.message,
                code = dataErrorType.code.toString(),
                httpStatus = dataErrorType.status,
            )
        }
    }
}

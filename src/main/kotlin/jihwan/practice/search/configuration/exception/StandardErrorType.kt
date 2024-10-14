package jihwan.practice.search.configuration.exception

import org.springframework.http.HttpStatus

enum class StandardErrorType(
    val status: HttpStatus,
    val code: Int,
    val message: String,
    val exceptions: Set<Any>,
) {
    EXTERNAL_SERVER_ISSUE(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "external server issue", setOf(ExternalServerException::class)),

    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "오류가 발생했습니다.\n 잠시 후 다시 시도해주세요.", setOf(Exception::class)),
    ;

    companion object {
        fun of(throwable: Throwable): StandardErrorType {
            return entries.find { it.exceptions.contains(throwable::class) } ?: UNKNOWN
        }
    }
}

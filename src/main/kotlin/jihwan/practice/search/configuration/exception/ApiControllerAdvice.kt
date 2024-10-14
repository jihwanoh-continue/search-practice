package jihwan.practice.search.configuration.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(ExternalServerException::class)
    fun exceptionHandler(exception: ExternalServerException): ErrorResponse {
        val dataErrorType = ErrorResponse.of(exception)

        return ErrorResponse(
            message = exception.message ?: dataErrorType.message,
            code = dataErrorType.code,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}

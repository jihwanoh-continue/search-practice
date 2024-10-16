package jihwan.practice.search.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(Info().title("search pracice API").description("Search Pracice API").version("1.0.0"))
            .path("/api/**", null)
    }
}

package jihwan.practice.search.controller.search

import jihwan.practice.search.controller.search.response.KeywordResponse
import jihwan.practice.search.controller.search.response.PlaceResponse
import jihwan.practice.search.service.SearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/search")
@RestController
class SearchController(
    private val searchService: SearchService,
) : SearchSpec {
    @GetMapping
    override suspend fun search(@RequestParam keyword: String): List<PlaceResponse> {
        return searchService.search(keyword).map { PlaceResponse.of(it) }
    }

    @GetMapping("/keywords")
    override fun getTopKeywords(): List<KeywordResponse> {
        return searchService.getTopKeywords().map { KeywordResponse.of(it) }
    }
}

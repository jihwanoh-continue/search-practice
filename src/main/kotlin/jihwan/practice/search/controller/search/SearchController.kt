package jihwan.practice.search.controller.search

import jihwan.practice.search.service.SearchService
import jihwan.practice.search.service.dto.Keyword
import jihwan.practice.search.service.dto.Place
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
    override suspend fun search(@RequestParam keyword: String): List<Place> {
        return searchService.search(keyword)
    }

    @GetMapping("/keywords")
    override fun getTopKeywords(size: Int): List<Keyword> {
        return searchService.getTopKeywords(size)
    }
}

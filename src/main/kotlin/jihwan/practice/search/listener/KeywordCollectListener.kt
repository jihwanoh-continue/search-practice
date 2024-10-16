package jihwan.practice.search.listener

import jihwan.practice.search.service.KeywordCollectService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class KeywordCollectListener(
    private val keywordCollectService: KeywordCollectService
) {
    @EventListener
    fun collect(event: KeywordCollectEvent) {
        keywordCollectService.incrementCount(event.keyword)
    }
}

data class KeywordCollectEvent(
    val keyword: String,
)

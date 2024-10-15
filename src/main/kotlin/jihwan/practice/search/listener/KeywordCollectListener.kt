package jihwan.practice.search.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class KeywordCollectListener {
    @EventListener
    fun collect(event: KeywordCollectEvent) {
        println("키워드 수집 이벤트 발생 ${event.keyword}")
    }
}

data class KeywordCollectEvent(
    val keyword: String,
)

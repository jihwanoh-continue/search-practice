package jihwan.practice.search.service

import jihwan.practice.search.repository.KeywordRepository
import org.springframework.stereotype.Service

@Service
class KeywordCollectService(
    private val keywordRepository: KeywordRepository,
) {
    fun incrementCount(keyword: String) {
        keywordRepository.incrementCount(keyword)
    }

    fun getTopKeywords(size: Int = 10): List<Pair<String, Int>> {
        return keywordRepository.getTopKeywords(size)
    }
}

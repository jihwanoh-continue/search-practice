package jihwan.practice.search.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class KeywordRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    companion object {
        private const val KEYWORD_ZSET_KEY = "keyword_counts"
    }

    fun incrementCount(keyword: String) {
        redisTemplate.opsForZSet().incrementScore(KEYWORD_ZSET_KEY, keyword, 1.0)
    }

    fun getTopKeywords(size: Int = 10): List<Pair<String, Int>> {
        val results = redisTemplate.opsForZSet().reverseRangeWithScores(KEYWORD_ZSET_KEY, 0, size - 1.toLong())
        return results?.map { it.value.toString() to it.score!!.toInt() } ?: emptyList()
    }
}

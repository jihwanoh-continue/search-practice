package jihwan.practice.search.service

import jihwan.practice.search.client.KakaoPlaceSearchClient
import jihwan.practice.search.client.KakaoPlaceSearchResponse
import jihwan.practice.search.client.NaverPlaceSearchClient
import jihwan.practice.search.client.NaverPlaceSearchResponse
import jihwan.practice.search.configuration.exception.ExternalServerException
import jihwan.practice.search.listener.KeywordCollectEvent
import jihwan.practice.search.service.dto.Keyword
import jihwan.practice.search.service.dto.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val kakaoPlaceSearchClient: KakaoPlaceSearchClient,
    private val naverPlaceSearchClient: NaverPlaceSearchClient,
    private val keywordCollectService: KeywordCollectService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val searchSize = 10
    private val searchLimitPerProvider = 5

    suspend fun search(keyword: String): List<Place> = coroutineScope {
        val kakaoDeferred = async { kakaoPlaceSearchClient.search(keyword, size = searchSize) }
        val naverDeferred = async { naverPlaceSearchClient.search(keyword, size = searchSize) }

        val kakaoResponse = kakaoDeferred.await()
        val naverResponse = naverDeferred.await()

        if (kakaoResponse == null && naverResponse == null) {
            throw ExternalServerException()
        }

        val (kakaoPlaces, naverPlaces) = getPlaces(kakaoResponse, naverResponse)

        mergePlaces(kakaoPlaces = kakaoPlaces, naverPlaces = naverPlaces)
    }.also {
        eventPublisher.publishEvent(KeywordCollectEvent(keyword))
    }

    private fun getPlaces(kakaoResponse: KakaoPlaceSearchResponse?, naverResponse: NaverPlaceSearchResponse?): Pair<List<Place>, List<Place>> {
        var kakaoPlaces = kakaoResponse?.documents?.map { Place.of(it) } ?: emptyList()
        var naverPlaces = naverResponse?.items?.map { Place.of(it) } ?: emptyList()

        if (naverPlaces.size <= searchLimitPerProvider) {
            kakaoPlaces = kakaoPlaces.take(searchSize - naverPlaces.size)
            naverPlaces = naverPlaces.take(searchLimitPerProvider)
        }

        if (kakaoPlaces.size <= searchLimitPerProvider) {
            naverPlaces = naverPlaces.take(searchSize - kakaoPlaces.size)
            kakaoPlaces = kakaoPlaces.take(searchLimitPerProvider)
        }

        return Pair(kakaoPlaces, naverPlaces)
    }

    private fun mergePlaces(kakaoPlaces: List<Place>, naverPlaces: List<Place>): List<Place> {
        val common = kakaoPlaces.intersect(naverPlaces.toSet())
        val onlyKakao = kakaoPlaces - naverPlaces.toSet()
        val onlyNaver = naverPlaces - kakaoPlaces.toSet()

        return (common + onlyKakao + onlyNaver).take(searchSize)
    }

    fun getTopKeywords(size: Int = 10): List<Keyword> {
        val topKeywords = keywordCollectService.getTopKeywords(size)
        return topKeywords.map { Keyword(it.first, it.second) }
            .sortedByDescending { it.count }
    }
}

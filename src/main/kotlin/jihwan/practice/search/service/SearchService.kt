package jihwan.practice.search.service

import jihwan.practice.search.client.KakaoPlaceSearchClient
import jihwan.practice.search.client.NaverPlaceSearchClient
import jihwan.practice.search.configuration.exception.ExternalServerException
import jihwan.practice.search.service.dto.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val kakaoPlaceSearchClient: KakaoPlaceSearchClient,
    private val naverPlaceSearchClient: NaverPlaceSearchClient,
) {
    private val searchSize = 10

    suspend fun search(keyword: String): List<Place> = coroutineScope {
        val kakaoDeferred = async { kakaoPlaceSearchClient.search(keyword, size = searchSize) }
        val naverDeferred = async { naverPlaceSearchClient.search(keyword, size = searchSize) }

        val kakaoResponse = kakaoDeferred.await()
        val naverResponse = naverDeferred.await()

        if (kakaoResponse == null && naverResponse == null) {
            throw ExternalServerException()
        }

        mergePlaces(
            kakaoPlaces = kakaoResponse?.documents?.map { Place.of(it) } ?: emptyList(),
            naverPlaces = naverResponse?.items?.map { Place.of(it) } ?: emptyList(),
        )
    }

    private fun mergePlaces(kakaoPlaces: List<Place>, naverPlaces: List<Place>): List<Place> {
        val common = kakaoPlaces.intersect(naverPlaces.toSet())
        val onlyKakao = kakaoPlaces - naverPlaces.toSet()
        val onlyNaver = naverPlaces - kakaoPlaces.toSet()

        return (common + onlyKakao + onlyNaver).take(searchSize)
    }
}

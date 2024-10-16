package jihwan.practice.search.service

import jihwan.practice.search.client.KakaoPlaceSearchClient
import jihwan.practice.search.client.NaverPlaceSearchClient
import jihwan.practice.search.configuration.exception.ExternalServerException
import jihwan.practice.search.fixture.DocumentFixture
import jihwan.practice.search.fixture.ItemFixture
import jihwan.practice.search.fixture.KakaoPlaceSearchResponseFixture
import jihwan.practice.search.fixture.NaverPlaceSearchResponseFixture
import jihwan.practice.search.service.dto.Address
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockitoExtension::class)
class SearchServiceTest {

    @Mock
    private lateinit var kakaoPlaceSearchClient: KakaoPlaceSearchClient

    @Mock
    private lateinit var naverPlaceSearchClient: NaverPlaceSearchClient

    @Mock
    private lateinit var keywordCollectService: KeywordCollectService

    @Mock
    private lateinit var eventPublisher: ApplicationEventPublisher

    @InjectMocks
    private lateinit var sut: SearchService

    @Nested
    inner class `search 메소드의 ` {
        private val searchSize = 10

        @Nested
        inner class `검색결과는 ` {
            val keyword = "곱창제일"
            val expectedKakaoDocumentList = listOf(DocumentFixture.of(name = keyword, address = "서울특별시 성동구 성수동1가 656-481"), DocumentFixture.of(name = keyword, address = "서울특별시 성동구 성수동1가 656-482"))
            val expectedNaverItemList = listOf(ItemFixture.of(name = keyword, address = "서울특별시 성동구 성수동1가 656-481"), ItemFixture.of(name = keyword, address = "서울특별시 성동구 성수동1가 656-483"))
            val expectedKakaoPlaceSearchResponse = KakaoPlaceSearchResponseFixture.of(name = keyword, expectedKakaoDocumentList)
            val expectedNaverPlaceSearchResponse = NaverPlaceSearchResponseFixture.of(name = keyword, expectedNaverItemList)

            @Test
            fun `공통 검색 결과를 우선적으로 보여준다`() {
                given(kakaoPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedKakaoPlaceSearchResponse)
                given(naverPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedNaverPlaceSearchResponse)

                val result = runBlocking { sut.search(keyword) }

                assertThat(result.first().address).isEqualTo(Address.parse(expectedKakaoDocumentList.first().addressName))
                assertThat(result.first().address).isEqualTo(Address.parse(expectedNaverItemList.first().address))
            }

            @Test
            fun `공통 검색 결과 이후에 카카오 검색 결과를 네이버 검색 결과보다 우선 사용한다`() {
                given(kakaoPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedKakaoPlaceSearchResponse)
                given(naverPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedNaverPlaceSearchResponse)

                val result = runBlocking { sut.search(keyword) }

                assertThat(result[1].address).isEqualTo(Address.parse(expectedKakaoDocumentList[1].addressName))
            }

            @Test
            fun `공통 검색 결과, 카카오 검색 결과 이후에 네이버 검색 결과를 사용한다`() {
                given(kakaoPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedKakaoPlaceSearchResponse)
                given(naverPlaceSearchClient.search(keyword, searchSize)).willReturn(expectedNaverPlaceSearchResponse)

                val result = runBlocking { sut.search(keyword) }

                assertThat(result[2].address).isEqualTo(Address.parse(expectedNaverItemList[1].address))
            }
        }

        @Test
        fun `카카오 결과가 5개 미만인 경우 네이버 검색결과로 10개를 채운다`() {
            val expectedKakaoResponse = KakaoPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-481", size = 4)
            val expectedNaverResponse = NaverPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-482", size = 6)

            given(kakaoPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedKakaoResponse)
            given(naverPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedNaverResponse)

            val result = runBlocking { sut.search("곱창제일") }

            assertThat(result.size).isEqualTo(10)

            result.forEachIndexed { index, place ->
                if (index < 4) {
                    assertThat(place.name.contains("kakao")).isEqualTo(true)
                } else {
                    assertThat(place.name.contains("naver")).isEqualTo(true)
                }
            }
        }

        @Test
        fun `네이버 결과가 5개 미만인 경우 카카오 검색결과로 10개를 채운다`() {
            val expectedKakaoResponse = KakaoPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-481", size = 6)
            val expectedNaverResponse = NaverPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-482", size = 4)

            given(kakaoPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedKakaoResponse)
            given(naverPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedNaverResponse)

            val result = runBlocking { sut.search("곱창제일") }

            assertThat(result.size).isEqualTo(10)

            result.forEachIndexed { index, place ->
                if (index < 6) {
                    assertThat(place.name.contains("kakao")).isEqualTo(true)
                } else {
                    assertThat(place.name.contains("naver")).isEqualTo(true)
                }
            }
        }

        @Test
        fun `두 결과 모두 5개 미만인 경우 있는 데이터로 결과를 만든다`() {
            val expectedKakaoResponse = KakaoPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-481", size = 4)
            val expectedNaverResponse = NaverPlaceSearchResponseFixture.of(name = "곱창제일", address = "서울특별시 성동구 성수동1가 656-482", size = 4)

            given(kakaoPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedKakaoResponse)
            given(naverPlaceSearchClient.search("곱창제일", searchSize)).willReturn(expectedNaverResponse)

            val result = runBlocking { sut.search("곱창제일") }

            assertThat(result.size).isEqualTo(8)
        }

        @Test
        fun `모든 결과가 null일 경우 예외를 발생시킨다`() {
            val keyword = "곱창제일"
            given(kakaoPlaceSearchClient.search(keyword, searchSize)).willReturn(null)
            given(naverPlaceSearchClient.search(keyword, searchSize)).willReturn(null)

            assertThrows<ExternalServerException> { runBlocking { sut.search(keyword) } }
        }
    }
}

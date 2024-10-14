package jihwan.practice.search.fixture

import jihwan.practice.search.client.Document
import jihwan.practice.search.client.KakaoPlaceSearchResponse
import jihwan.practice.search.client.Meta
import jihwan.practice.search.client.SameName

class KakaoPlaceSearchResponseFixture {
    companion object {
        fun of(name: String, address: String, size: Int): KakaoPlaceSearchResponse {
            return KakaoPlaceSearchResponse(
                meta = MetaFixture.of(name),
                documents = List(size) { index -> DocumentFixture.of(name + "kakao" + index, address + index) }
            )
        }

        fun of(name: String, documentList: List<Document>): KakaoPlaceSearchResponse {
            return KakaoPlaceSearchResponse(
                meta = MetaFixture.of(name),
                documents = documentList
            )
        }
    }
}

class DocumentFixture {
    companion object {
        fun of(name: String, address: String): Document {
            return Document(
                id = "1",
                placeName = name,
                categoryName = "음식점",
                categoryGroupCode = "FD6",
                categoryGroupName = "음식점",
                phone = "02-1234-5678",
                placeUrl = "https://place1.com",
                roadAddressName = "서울특별시 서초구 매헌로 116",
                addressName = address,
                x = "127.123456",
                y = "37.123456",
                distance = "1234"
            )
        }
    }
}

class MetaFixture {
    companion object {
        fun of(keyword: String): Meta {
            return Meta(
                totalCount = 1,
                pageableCount = 1,
                isEnd = true,
                sameName = SameNameFixture.of(keyword)
            )
        }
    }
}

class SameNameFixture {
    companion object {
        fun of(keyword: String): SameName {
            return SameName(
                region = emptyList(),
                keyword = keyword,
                selectedRegion = "서울"
            )
        }
    }
}

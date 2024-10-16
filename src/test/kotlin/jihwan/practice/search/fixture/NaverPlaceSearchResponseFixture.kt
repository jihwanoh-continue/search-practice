package jihwan.practice.search.fixture

import jihwan.practice.search.client.Item
import jihwan.practice.search.client.NaverPlaceSearchResponse

class NaverPlaceSearchResponseFixture {
    companion object {
        fun of(name: String, address: String, size: Int): NaverPlaceSearchResponse {
            return NaverPlaceSearchResponse(
                lastBuildDate = "2021-08-25T14:00:00",
                total = 1,
                start = 1,
                display = 1,
                items = List(size) { index -> ItemFixture.of(name + "naver" + index, address + index) }
            )
        }

        fun of(name: String, itemList: List<Item>): NaverPlaceSearchResponse {
            return NaverPlaceSearchResponse(
                lastBuildDate = "2021-08-25T14:00:00",
                total = 1,
                start = 1,
                display = 1,
                items = itemList
            )
        }
    }
}

class ItemFixture {
    companion object {
        fun of(name: String, address: String): Item {
            return Item(
                title = name,
                link = "https://place1.com",
                category = "음식점",
                description = "서울특별시 강남구 역삼동 123-4",
                telephone = "02-1234-5678",
                address = address,
                roadAddress = "서울특별시 강남구 테헤란로 1234",
                x = 1270383958,
                y = 374699131,
            )
        }
    }
}

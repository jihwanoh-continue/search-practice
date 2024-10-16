package jihwan.practice.search.controller.search.response

import io.swagger.v3.oas.annotations.media.Schema
import jihwan.practice.search.service.dto.Address
import jihwan.practice.search.service.dto.RoadAddress

data class PlaceResponse(
    @get:Schema(description = "장소 이름", example = "아트빌딩")
    val name: String,
    @get:Schema(description = "전화번호", example = "02-1234-5678")
    val phone: String,
    @get:Schema(description = "홈페이지 주소", example = "https://www.artbuilding.com")
    val url: String,
    val roadAddress: RoadAddressResponse,
    val address: AddressResponse,
) {
    companion object {
        fun of(place: jihwan.practice.search.service.dto.Place): PlaceResponse {
            return PlaceResponse(
                place.name,
                place.phone,
                place.url,
                RoadAddressResponse.of(place.roadAddress),
                AddressResponse.of(place.address)
            )
        }
    }
}

data class RoadAddressResponse(
    @get:Schema(description = "행정 구역", example = "서울 성동구")
    val administrative: String,
    @get:Schema(description = "도로명", example = "서울숲길45")
    val roadName: String,
    @get:Schema(description = "상세 주소", example = "아트빌딩 3층")
    val detail: String?,
) {
    companion object {
        fun of(roadAddress: RoadAddress): RoadAddressResponse {
            return RoadAddressResponse(
                roadAddress.administrative,
                roadAddress.roadName,
                roadAddress.detail,
            )
        }
    }
}

data class AddressResponse(
    @get:Schema(description = "시도", example = "서울특별시")
    val siDo: String,
    @get:Schema(description = "시군구", example = "성동구")
    val siGunGu: String,
    @get:Schema(description = "읍면동", example = "성수동1가")
    val eupMyeonDong: String,
    @get:Schema(description = "리", example = "")
    val ri: String?,
    @get:Schema(description = "지번", example = "123-45")
    val jiBeon: String,
    @get:Schema(description = "건물명", example = "아트빌딩")
    val buildingName: String?
) {
    companion object {
        fun of(address: Address): AddressResponse {
            return AddressResponse(
                address.siDo,
                address.siGunGu,
                address.eupMyeonDong,
                address.ri,
                address.jiBeon,
                address.buildingName
            )
        }
    }
}

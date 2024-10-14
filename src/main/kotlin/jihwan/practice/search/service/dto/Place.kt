package jihwan.practice.search.service.dto

import jihwan.practice.search.client.Document
import jihwan.practice.search.client.Item

data class Place(
    val name: String,
    val phone: String,
    val url: String,
    val roadAddress: RoadAddress,
    val address: Address,
    val x: String,
    val y: String,
) {
    companion object {
        fun of(document: Document): Place = Place(
            name = document.placeName,
            phone = document.phone,
            url = document.placeUrl,
            roadAddress = RoadAddress.of(document.roadAddressName),
            address = Address.parse(document.addressName),
            x = document.x,
            y = document.y,
        )

        fun of(item: Item): Place = Place(
            name = item.title,
            phone = item.telephone,
            url = item.link,
            roadAddress = RoadAddress.of(item.roadAddress),
            address = Address.parse(item.address),
            x = item.x.toString(),
            y = item.y.toString(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        return address == other.address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}

package jihwan.practice.search.service

import jihwan.practice.search.client.Document

data class Place(
    val name: String,
    val phone: String,
    val url: String,
    val roadAddress: String,
    val address: String,
    val x: String,
    val y: String,
) {
    companion object {
        fun of(document: Document): Place = Place(
            name = document.placeName,
            phone = document.phone,
            url = document.placeUrl,
            roadAddress = document.roadAddressName,
            address = document.addressName,
            x = document.x,
            y = document.y,
        )
    }
}

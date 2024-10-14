package jihwan.practice.search.service.dto

data class RoadAddress(
    val administrative: String,
    val roadName: String,
    val detail: String?,
) {
    companion object {
        fun of(plainAddress: String): RoadAddress {
            val split = plainAddress.split(SPACE)
            val administrative = getAdministrative(split.getOrNull(0), split.getOrNull(1))
            val roadName = split.getOrNull(2) ?: EMPTY
            val detail = split.getOrNull(3)

            return RoadAddress(administrative, roadName, detail)
        }

        private fun getAdministrative(sido: String?, gu: String?): String {
            return Regex(SIDO_POSTFIX).replace(sido ?: EMPTY, EMPTY) + SPACE + (gu ?: EMPTY)
        }

        private const val SPACE = " "
        private const val EMPTY = ""
        private const val SIDO_POSTFIX = "(?:특별자치|특별|광역|특례)$"
    }
}

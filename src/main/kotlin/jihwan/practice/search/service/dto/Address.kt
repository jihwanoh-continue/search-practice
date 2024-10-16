package jihwan.practice.search.service.dto

import kotlin.math.min

data class Address(
    val siDo: String,
    val siGunGu: String,
    val eupMyeonDong: String,
    val ri: String?,
    val jiBeon: String,
    val buildingName: String?
) {
    companion object {
        fun parse(addressString: String): Address {
            val parts = addressString.split(SPACE)
            var currentIndex = 0

            val siDo = Regex(SIDO_POSTFIX).replace(parts[currentIndex++], EMPTY)
            val siGunGu = parts[currentIndex++]
            var eupMyeonDong = parts[currentIndex++]
            var ri: String? = null

            if (currentIndex < parts.size && parts[currentIndex].matches(Regex("^[0-9-]+$")).not()) {
                ri = parts[currentIndex++]
            }

            val jiBeon = parts[currentIndex++]

            val buildingName = if (currentIndex < parts.size) parts.subList(currentIndex, parts.size).joinToString(" ") else null

            return Address(siDo, siGunGu, eupMyeonDong, ri, jiBeon, buildingName)
        }

        private const val SPACE = " "
        private const val EMPTY = ""
        private const val SIDO_POSTFIX = "(?:특별자치도|특별자치시|특별시|광역시|특례시)$"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (siDo != other.siDo) return false
        if (siGunGu != other.siGunGu) return false
        if (eupMyeonDong != other.eupMyeonDong) return false
        if (ri != other.ri) return false

        // 지번 비교 시 숫자 부분만 비교
        val thisNumbers = jiBeon.split("-").map { it.filter { c -> c.isDigit() } }
        val otherNumbers = other.jiBeon.split("-").map { it.filter { c -> c.isDigit() } }

        for (i in 0 until min(thisNumbers.size, otherNumbers.size)) {
            if (thisNumbers[i] != otherNumbers[i]) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = siDo.hashCode()
        result = 31 * result + siGunGu.hashCode()
        result = 31 * result + eupMyeonDong.hashCode()
        result = 31 * result + (ri?.hashCode() ?: 0)
        result = 31 * result + jiBeon.hashCode()
        result = 31 * result + (buildingName?.hashCode() ?: 0)
        return result
    }
}

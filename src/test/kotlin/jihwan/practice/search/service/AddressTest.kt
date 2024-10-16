package jihwan.practice.search.service

import jihwan.practice.search.service.dto.Address
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AddressTest {

    @Nested
    inner class `parse 메소드는` {
        @ParameterizedTest
        @CsvSource(
            "서울특별시, 서울",
            "부산광역시, 부산",
            "대구광역시, 대구",
            "인천광역시, 인천",
            "광주광역시, 광주",
            "대전광역시, 대전",
            "울산광역시, 울산",
            "세종특별자치시, 세종",
            "경기도, 경기도",
            "강원도, 강원도",
            "충청북도, 충청북도",
            "충청남도, 충청남도",
            "전라북도, 전라북도",
            "전라남도, 전라남도",
            "경상북도, 경상북도",
            "경상남도, 경상남도",
            "제주특별자치도, 제주"
        )
        fun `주소를 올바르게 파싱한다`(input: String, expected: String) {
            // given
            val sidoPostfixRegex = Regex("(?:특별자치도|특별자치시|특별시|광역시|특례시)$")

            // when
            val result = sidoPostfixRegex.replace(input, "")

            // then
            assertEquals(expected, result)
        }

        @Test
        fun `지번이 숫자로만 이루어져있는 경우에도 정상적으로 반환한다`() {
            // given
            val addressString = "서울특별시 성동구 성수동1가 656"

            // when
            val address = Address.parse(addressString)

            // then
            assertEquals("서울", address.siDo)
            assertEquals("성동구", address.siGunGu)
            assertEquals("성수동1가", address.eupMyeonDong)
            assertNull(address.ri)
            assertEquals("656", address.jiBeon)
            assertNull(address.buildingName)
        }
    }

    @Nested
    inner class `equals 메소드는` {
        @Test
        fun `동일한 주소 정보를 가진 Address는 같다고 판단한다`() {
            // given
            val address1 = Address("서울", "성동구", "성수동1가", null, "656-482", null)
            val address2 = Address("서울", "성동구", "성수동1가", null, "656-482", null)

            // when & then
            assertEquals(address1, address2)
            assertEquals(address1.hashCode(), address2.hashCode())
        }

        @Test
        fun `지번의 숫자만 같고 건물명이 다른 Address는 같다고 판단한다`() {
            // given
            val address1 = Address("서울", "성동구", "성수동1가", null, "656-482", null)
            val address2 = Address("서울", "성동구", "성수동1가", null, "656-482", "성수빌딩")

            // when & then
            assertEquals(address1, address2)
        }

        @Test
        fun `시도, 시군구, 읍면동, 리 중 하나라도 다르면 다르다고 판단한다`() {
            // given
            val baseAddress = Address("서울", "성동구", "성수동1가", null, "656-482", null)
            val differentSiDo = Address("부산", "성동구", "성수동1가", null, "656-482", null)
            val differentSiGunGu = Address("서울", "강남구", "성수동1가", null, "656-482", null)
            val differentEupMyeonDong = Address("서울", "성동구", "행당동", null, "656-482", null)
            val differentRi = Address("서울", "성동구", "성수동1가", "1리", "656-482", null)

            // when & then
            assertNotEquals(baseAddress, differentSiDo)
            assertNotEquals(baseAddress, differentSiGunGu)
            assertNotEquals(baseAddress, differentEupMyeonDong)
            assertNotEquals(baseAddress, differentRi)
        }
    }
}

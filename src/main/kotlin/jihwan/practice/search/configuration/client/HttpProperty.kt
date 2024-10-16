package jihwan.practice.search.configuration.client

interface HttpProperty {
    val url: String
        get() = ""

    val timeout: Long
        get() = 3000
}

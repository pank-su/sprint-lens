package su.pank.sprintlens

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
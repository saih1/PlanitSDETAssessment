import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import java.nio.channels.UnresolvedAddressException
import java.util.*

fun main() {
    // Challenge 2
    highestNumberOfOccur("Character")

    // Challenge 6
    runBlocking {
        makeApiCall()
    }
}

// Challenge 2
fun highestNumberOfOccur(input: String) {
    val transformedInput: List<Pair<Char, Int>> = input
        .lowercase(Locale.getDefault())
        .toList()
        .groupBy { it }
        .map { it.key to it.value.count() }

    val maxOccurrence: Int = transformedInput
        .maxOf { it.second }

    val result: Char? = transformedInput
        .find { it.second == maxOccurrence }?.first

    println(
        """
            Input: $input
            Output: $result
        """.trimIndent()
    )
}

// Challenge 6
suspend fun makeApiCall() {
    val url = "https://petstore.swagger.io/v2/pet/findByStatus?status=available"
    val client = HttpClient(engineFactory = CIO) {
        install(plugin = ContentNegotiation) { this.json(Json) }
    }
    try {
        val httpResponse: HttpResponse = client.get(url)
            .also { println(it.status) }
        val responseJsonArray: JsonArray = httpResponse.body()
        println("Number of pets in the result = ${responseJsonArray.count()}")
    } catch (e: UnresolvedAddressException) {
        println("Invalid Address!")
    } catch (e: Exception) {
        println("Issue with the network request!")
    }
}

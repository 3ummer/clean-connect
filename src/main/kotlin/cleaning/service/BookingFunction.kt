package cleaning.service
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.util.*
import java.util.function.Function

@Configuration
class BookingFunctionConfig {

    init {
        println("🔥 BookingFunctionConfig initialized")
//        val serviceAccount = FileInputStream("serviceAccountKey.json")
//        val options = FirebaseOptions.builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//            .build()
//        FirebaseApp.initializeApp(options)
    }

    private val client = OkHttpClient()

    @Bean
    fun handleBooking(): Function<Booking, String> = Function { booking ->
//        val db = FirestoreClient.getFirestore()
//        val id = UUID.randomUUID().toString()
//        val bookingMap = mapOf(
//            "id" to id,
//            "name" to booking.name,
//            "phone" to booking.phone,
//            "address" to booking.address,
//            "dateTime" to booking.dateTime,
//            "cleaningType" to booking.cleaningType,
//            "notes" to booking.notes,
//            "status" to "pending"
//        )
//        db.collection("bookings").document(id).set(bookingMap)
//        sendLineNotify(booking)
        "Booking received. Awaiting approval."
    }
//    private fun sendLineNotify(booking: Booking) {
//        val token = System.getenv("LINE_NOTIFY_TOKEN") ?: return
//        val message = "\uD83D\uDCE2 New booking from ${booking.name} on ${booking.dateTime}."
//        val body = "message=$message".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .url("https://notify-api.line.me/api/notify")
//            .addHeader("Authorization", "Bearer $token")
//            .post(body)
//            .build()
//        client.newCall(request).execute().use { }
//    }
}

data class Booking(
    val name: String,
    val phone: String,
    val address: String,
    val dateTime: String,
    val cleaningType: String,
    val notes: String?
)

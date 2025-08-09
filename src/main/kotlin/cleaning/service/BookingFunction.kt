package cleaning.service

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import okhttp3.OkHttpClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.util.*

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["*"])
class BookingController {

    init {
        println("🚀 BookingController initialized")
//        val serviceAccount = FileInputStream("serviceAccountKey.json")
//        val options = FirebaseOptions.builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//            .build()
//        FirebaseApp.initializeApp(options)
    }

    private val client = OkHttpClient()

    @PostMapping("/bookings")
    fun createBooking(@RequestBody booking: Booking): ResponseEntity<BookingResponse> {
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
        
        val id = UUID.randomUUID().toString()
        return ResponseEntity.ok(BookingResponse(
            id = id,
            message = "Booking received. Awaiting approval.",
            status = "pending"
        ))
    }

    @GetMapping("/bookings/{id}")
    fun getBooking(@PathVariable id: String): ResponseEntity<BookingResponse> {
        return ResponseEntity.ok(BookingResponse(
            id = id,
            message = "Booking details",
            status = "pending"
        ))
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "Clean Connect Backend"
        ))
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

data class BookingResponse(
    val id: String,
    val message: String,
    val status: String
)

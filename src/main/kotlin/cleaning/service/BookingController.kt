package cleaning.service

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import okhttp3.OkHttpClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["*"])
class BookingController {

    init {
        println("🚀 BookingController initialized")
        
        // Initialize Firebase (required for Firestore access)
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp() // Uses Application Default Credentials automatically
            println("✅ Firebase initialized")
        }
    }

    private val client = OkHttpClient()

    @PostMapping("/bookings")
    fun createBooking(@RequestBody booking: Booking): ResponseEntity<BookingResponse> {
        return try {
            val db = FirestoreClient.getFirestore()
            val id = UUID.randomUUID().toString()
            
            val bookingData = mapOf(
                "id" to id,
                "name" to booking.name,
                "phone" to booking.phone,
                "address" to booking.address,
                "dateTime" to booking.dateTime,
                "cleaningType" to booking.cleaningType,
                "notes" to booking.notes,
                "status" to "pending",
                "createdAt" to System.currentTimeMillis(),
                "updatedAt" to System.currentTimeMillis()
            )
            
            // Store in Firestore
            db.collection("bookings").document(id).set(bookingData).get()
            println("✅ Booking saved with ID: $id")
            
            // TODO: Send notification
            // sendLineNotify(booking)
            
            ResponseEntity.ok(BookingResponse(
                id = id,
                message = "Booking received and saved! We'll contact you soon.",
                status = "pending"
            ))
        } catch (e: Exception) {
            println("❌ Error saving booking: ${e.message}")
            ResponseEntity.status(500).body(BookingResponse(
                id = "",
                message = "Error processing booking. Please try again.",
                status = "error"
            ))
        }
    }

    @GetMapping("/bookings/{id}")
    fun getBooking(@PathVariable id: String): ResponseEntity<BookingResponse> {
        return try {
            val db = FirestoreClient.getFirestore()
            val document = db.collection("bookings").document(id).get().get()
            
            if (document.exists()) {
                val data = document.data
                ResponseEntity.ok(BookingResponse(
                    id = id,
                    message = "Booking found: ${data?.get("name")} - ${data?.get("cleaningType")}",
                    status = data?.get("status").toString()
                ))
            } else {
                ResponseEntity.status(404).body(BookingResponse(
                    id = id,
                    message = "Booking not found",
                    status = "not_found"
                ))
            }
        } catch (e: Exception) {
            println("❌ Error retrieving booking $id: ${e.message}")
            ResponseEntity.status(500).body(BookingResponse(
                id = id,
                message = "Error retrieving booking",
                status = "error"
            ))
        }
    }

    @GetMapping("/bookings")
    fun getAllBookings(): ResponseEntity<List<Map<String, Any?>>> {
        return try {
            val db = FirestoreClient.getFirestore()
            val querySnapshot = db.collection("bookings")
                .orderBy("createdAt")
                .get()
                .get()
            
            val bookings = querySnapshot.documents.map { document ->
                document.data?.plus("id" to document.id) ?: mapOf("id" to document.id)
            }
            
            ResponseEntity.ok(bookings)
        } catch (e: Exception) {
            println("❌ Error retrieving bookings: ${e.message}")
            ResponseEntity.status(500).body(emptyList())
        }
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "Clean Connect Backend",
            "database" to "Firestore Connected"
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

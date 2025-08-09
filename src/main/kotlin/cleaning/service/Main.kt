package cleaning.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["cleaning.service"])
open class CleaningServiceApp

fun main(args: Array<String>) {
    val ctx = runApplication<CleaningServiceApp>(*args)

    println("🚀 Clean Connect backend starting...")
    println("🔍 Registered beans:")
    ctx.beanDefinitionNames
//        .filter { it.contains("handleBooking", ignoreCase = true) }
        .forEach { println("✅ $it") }
}


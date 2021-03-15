package de.mayflower

import io.ktor.application.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.nats.client.*
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val natsConnection: Connection = Nats.connect()
    natsConnection.publish("frontend.hello", "Hello NATS Event Bus from Frontend!".toByteArray())

    routing {
        get("/") {
            call.respondText("Hello Frontend!", contentType = ContentType.Text.Plain)
        }
        get("/html") {
            val title = "Frontend HTML Test Route"
            val acclaim = "Welcome to our HTML Route"
            val content = "This route returns a rendered HTML body that contains templating."

            call.respondHtml {
                head {
                    title { +title }
                }
                body {
                    h1 { +acclaim }
                    p { +content }
                }
            }
        }
        get("/joke") {
            val sub: Subscription = natsConnection.subscribe("backend.joked")
            natsConnection.publish("backend.joke", "".toByteArray())
            val message: Message = sub.nextMessage(Duration.ofSeconds(2))

            val title = "Joke Frontend"
            val headline = "Joke of the Day"
            val joke = String(message.data)

            call.respondHtml {
                head {
                    title { +title }
                }
                body {
                    h1 { +headline }
                    p { +joke }
                }
            }
        }
    }
}

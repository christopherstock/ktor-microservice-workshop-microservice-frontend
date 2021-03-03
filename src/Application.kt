package de.mayflower

import io.ktor.application.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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
    }
}

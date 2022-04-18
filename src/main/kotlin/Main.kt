import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.net.InetAddress
import java.net.Socket

class Skan : CliktCommand() {
    private val host: String by option(help="The hostname to connect to").default("localhost")

    override fun run() {
        val inetAddress = InetAddress.getByName(host)
        val hostName = inetAddress.hostName
        for (port in 0..65535) {
            runCatching {
                Socket(hostName, port).run {
                    echo("Port $port is open on $hostName")
                    close()
                }
            }
        }
    }
}

fun main(args: Array<String>) = Skan().main(args)
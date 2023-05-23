import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import java.net.InetAddress
import java.net.Socket

class Skan : CliktCommand() {
    private val host: String by option("-h", "--hostname", help="The hostname to connect to").prompt("Hostname")

    override fun run() {
        val inetAddress = InetAddress.getByName(host)
        val hostName = inetAddress.hostName
        val listOfOpenPorts = mutableListOf<Int>()
        for (port in 0..65535) {
            runCatching {
                Socket(hostName, port).run {
                    echo("Port $port is open on $hostName")
                    listOfOpenPorts.add(port)
                    close()
                }
            }
        }
        listOfOpenPorts.run {
            val last = last()
            remove(last())
            echo(joinToString(prefix = "Ports ", postfix = " and $last are open on $hostName"))
        }
    }
}

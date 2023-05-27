import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking

class Skan : CliktCommand() {
    private val host: String by option("-h", "--host", "--hostname", help = "The hostname to connect to")
        .prompt(text = "Hostname", default = "localhost")

    private val lowerBound: Int by option("-l", "--lower", "--lower-bound")
        .int()
        .default(0)
        .check { it >= 0 }

    private val upperBound: Int by option("-u", "--upper", "--upper-bound")
        .int()
        .default(UShort.MAX_VALUE.toInt())
        .check { it <= UShort.MAX_VALUE.toInt() }

    override fun run() = runBlocking {
        val listOfOpenPorts = mutableListOf<Int>()
        SelectorManager(Dispatchers.IO).use { selectorManager ->
            for (port in lowerBound..upperBound) {
                runCatching {
                    aSocket(selectorManager).tcp().connect(host, port).use {
                        echo("Port $port is open on $host")
                        listOfOpenPorts.add(port)
                    }
                }
            }
            if (listOfOpenPorts.isEmpty()) {
                echo("No ports are open on $host")
            } else {
                listOfOpenPorts.run {
                    val last = last()
                    remove(last)
                    echo(joinToString(prefix = "Ports ", postfix = " and $last are open on $host"))
                }
            }
        }
    }
}

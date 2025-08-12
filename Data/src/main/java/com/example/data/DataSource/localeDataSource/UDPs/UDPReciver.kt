import android.content.Context
import android.net.wifi.WifiManager
import com.example.domain.Entitys.RoomEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPListener( private val context: Context,val onRoomFound:suspend (RoomEntity) -> Unit,) {
    private val port = 53248
    private var isListening = true

    fun startListening() {
        val wifi =  context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val lock = wifi.createMulticastLock("mylock")
        lock.setReferenceCounted(true)
        lock.acquire()
        Thread {
            val socket = DatagramSocket(port)
            val buffer = ByteArray(2048)

            while (isListening) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)

                val json = String(packet.data, 0, packet.length).trim()
                try {
                    var room = Json.decodeFromString<RoomEntity>(json)
                    room = packet.address.hostAddress?.let { room.copy(ipHost = it) }!!
                    println("📥 Found Room: $room")
                    runBlocking {
                        onRoomFound(room)
                    }
                } catch (e: Exception) {
                    println("❌ Error decoding room: $e")
                }
            }

            socket.close()
        }.start()
    }

    fun stop() {
        isListening = false
    }
}
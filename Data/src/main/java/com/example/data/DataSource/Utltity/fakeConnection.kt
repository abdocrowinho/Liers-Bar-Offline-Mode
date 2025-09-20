import org.java_websocket.WebSocket
import org.java_websocket.drafts.Draft
import org.java_websocket.enums.Opcode
import org.java_websocket.framing.Framedata
import org.java_websocket.protocols.IProtocol
import java.nio.ByteBuffer
import javax.net.ssl.SSLSession


 fun createFakeConnection(port:Int): WebSocket {
    return object : WebSocket {
        override fun send(text: String) {
            println("📤 Fake connection received: $text")
        }

        override fun send(bytes: ByteArray) {}
        override fun sendFrame(framedata: Framedata?) {
        }

        override fun sendFrame(frames: MutableCollection<Framedata>?) {
        }

        override fun send(data: java.nio.ByteBuffer?) {}
        override fun close() {}
        override fun close(code: Int, reason: String?) {}
        override fun getRemoteSocketAddress(): java.net.InetSocketAddress {
            return java.net.InetSocketAddress("127.0.0.1", 0)
        }

        override fun getLocalSocketAddress(): java.net.InetSocketAddress {
            return java.net.InetSocketAddress("127.0.0.1", port)
        }

        override fun isOpen(): Boolean = true
        override fun isClosing(): Boolean = false
        override fun isClosed(): Boolean = false
        override fun getReadyState(): org.java_websocket.enums.ReadyState {
            return org.java_websocket.enums.ReadyState.OPEN
        }

        override fun hasBufferedData(): Boolean = false
        override fun isFlushAndClose(): Boolean = false
        override fun getDraft(): Draft? = null
        override fun getProtocol(): IProtocol? = null
        override fun getResourceDescriptor(): String = "/fake"
        override fun <T : Any> setAttachment(attachment: T?) {}
        override fun <T : Any> getAttachment(): T? = null
        override fun hasSSLSupport(): Boolean = false
        override fun getSSLSession(): SSLSession? = null
        override fun sendPing() {}
        override fun sendFragmentedFrame(op: Opcode?, buffer: ByteBuffer?, fin: Boolean) {}

        override fun close(code: Int) {}
        override fun closeConnection(code: Int, message: String?) {}
    }
}

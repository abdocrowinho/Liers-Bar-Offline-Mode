package com.example.data.DataSource.Utltity

import org.java_websocket.enums.Opcode
import org.java_websocket.framing.Framedata
import org.java_websocket.protocols.IProtocol
import java.nio.ByteBuffer
import javax.net.ssl.SSLSession
import org.java_websocket.WebSocket
import org.java_websocket.drafts.Draft
import org.java_websocket.enums.ReadyState
import java.net.InetSocketAddress


class FakeBotSocket : WebSocket {
    override fun close() {}
    override fun close(code: Int) {}
    override fun close(code: Int, message: String?) {}
    override fun closeConnection(code: Int, message: String?) {}
    override fun send(text: String?) {}
    override fun send(bytes: ByteBuffer?) {}
    override fun send(bytes: ByteArray?) {}
    override fun sendFrame(framedata: Framedata?) {}
    override fun sendFrame(frames: MutableCollection<Framedata>?) {}
    override fun sendPing() {}
    override fun sendFragmentedFrame(
        op: Opcode?,
        buffer: ByteBuffer?,
        fin: Boolean
    ) {
    }

    override fun hasBufferedData(): Boolean = false
    override fun getRemoteSocketAddress(): InetSocketAddress? = null
    override fun getLocalSocketAddress(): InetSocketAddress? = null
    override fun isOpen(): Boolean = false
    override fun isClosing(): Boolean = false
    override fun isFlushAndClose(): Boolean = false
    override fun isClosed(): Boolean = true
    override fun getDraft(): Draft? = null
    override fun getReadyState(): ReadyState {
        return ReadyState.CLOSED
    }

    override fun getResourceDescriptor(): String = ""
    override fun <T : Any?> setAttachment(attachment: T) {}
    override fun <T : Any?> getAttachment(): T? = null
    override fun hasSSLSupport(): Boolean = false
    override fun getSSLSession(): SSLSession? = null
    override fun getProtocol(): IProtocol? {
        return null}
}
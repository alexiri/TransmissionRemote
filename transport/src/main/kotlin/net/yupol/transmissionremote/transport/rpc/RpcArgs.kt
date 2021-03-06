package net.yupol.transmissionremote.transport.rpc

import android.util.Base64
import net.yupol.transmissionremote.model.Parameter
import net.yupol.transmissionremote.model.Priority
import net.yupol.transmissionremote.model.Priority.*
import net.yupol.transmissionremote.model.json.TransferPriority
import net.yupol.transmissionremote.model.limitmode.LimitMode

data class RpcArgs(val method: String, val arguments: Map<String, Any>? = null) {

    companion object {

        @JvmStatic
        fun parameters(vararg params: Parameter<String, Any>) =
                params.map { it.key to it.value }.toMap()

        @JvmStatic
        fun parameters(params: List<Parameter<String, Any>>) = parameters(*params.toTypedArray())

        @JvmStatic
        @SafeVarargs
        fun parameters(torrentId: Int, vararg params: Parameter<String, Any>) =
                mapOf("ids" to arrayOf(torrentId)) + parameters(*params)

        @JvmStatic
        fun renameTorrent(id: Int, path: String, name: String) =
                mapOf<String, Any>(
                        "ids" to intArrayOf(id),
                        "path" to path,
                        "name" to name)

        @JvmStatic
        fun setLocation(location: String, move: Boolean, vararg ids: Int) =
                mapOf<String, Any>(
                        "ids" to ids,
                        "location" to location,
                        "move" to move)

        @JvmStatic
        fun addTracker(torrentId: Int, url: String): Map<String, Any> = mapOf(
                "ids" to intArrayOf(torrentId),
                "trackerAdd" to arrayOf(url))

        @JvmStatic
        fun removeTracker(torrentId: Int, trackerId: Int): Map<String, Any> = mapOf(
                "ids" to intArrayOf(torrentId),
                "trackerRemove" to intArrayOf(trackerId))

        @JvmStatic
        fun editTracker(torrentId: Int, trackerId: Int, url: String): Map<String, Any> = mapOf(
                "ids" to intArrayOf(torrentId),
                "trackerReplace" to arrayOf(trackerId, url)
        )

        @JvmStatic
        fun addTorrent(url: String, destination: String, paused: Boolean): Map<String, Any> {
            val magnetUri = if (url.matches("^[0-9a-fA-F]{40}$".toRegex()))
                "magnet:?xt=urn:btih:$url"
            else {
                url
            }

            return mapOf(
                    "filename" to magnetUri,
                    "download-dir" to destination,
                    "paused" to paused
            )
        }

        @JvmStatic
        fun addTorrent(torrentFileContent: ByteArray, destination: String, paused: Boolean): Map<String, Any> {
            val metaInfo = Base64.encodeToString(torrentFileContent, Base64.DEFAULT)
            return mapOf(
                    "metainfo" to metaInfo,
                    "download-dir" to destination,
                    "paused" to paused
            )
        }
    }
}

object SessionParameters {

    @JvmStatic
    fun altSpeedLimitEnabled(enabled: Boolean) = Parameter("alt-speed-enabled", enabled)

    @JvmStatic
    fun altSpeedLimitDown(limit: Long) = Parameter("alt-speed-down", limit)

    @JvmStatic
    fun altSpeedLimitUp(limit: Long) = Parameter("alt-speed-up", limit)

    @JvmStatic
    fun speedLimitDownEnabled(enabled: Boolean) = Parameter("speed-limit-down-enabled", enabled)

    @JvmStatic
    fun speedLimitDown(limit: Long) = Parameter("speed-limit-down", limit)

    @JvmStatic
    fun speedLimitUpEnabled(enabled: Boolean) = Parameter("speed-limit-up-enabled", enabled)

    @JvmStatic
    fun speedLimitUp(limit: Long) = Parameter("speed-limit-up", limit)
}

object TorrentParameters {

    @JvmStatic
    fun filesWanted(vararg indices: Int) =
            Parameter("files-wanted", indices)

    @JvmStatic
    fun filesUnwanted(vararg indices: Int) =
            Parameter("files-unwanted", indices)

    @JvmStatic
    fun filesWithPriority(priority: Priority, vararg indices: Int): Parameter<String, Any> {
        val key = when (priority) {
            HIGH -> "priority-high"
            NORMAL -> "priority-normal"
            LOW -> "priority-low"
        }
        return Parameter(key, indices)
    }

    @JvmStatic
    fun transferPriority(priority: TransferPriority) =
            Parameter("bandwidthPriority", priority.modelValue)

    @JvmStatic
    fun honorSessionLimits(enabled: Boolean) =
            Parameter("honorsSessionLimits", enabled)

    @JvmStatic
    fun downloadLimited(isLimited: Boolean) =
            Parameter("downloadLimited", isLimited)

    @JvmStatic
    fun downloadLimit(limit: Long) =
            Parameter("downloadLimit", limit)

    @JvmStatic
    fun uploadLimited(isLimited: Boolean) =
            Parameter("uploadLimited", isLimited)

    @JvmStatic
    fun uploadLimit(limit: Long) =
            Parameter("uploadLimit", limit)

    @JvmStatic
    fun seedRatioMode(mode: LimitMode) =
            Parameter("seedRatioMode", mode.value)

    @JvmStatic
    fun seedRatioLimit(limit: Double) =
            Parameter("seedRatioLimit", limit)

    @JvmStatic
    fun seedIdleMode(mode: LimitMode) =
            Parameter("seedIdleMode", mode.value)

    @JvmStatic
    fun seedIdleLimit(limit: Int) =
            Parameter("seedIdleLimit", limit)
}

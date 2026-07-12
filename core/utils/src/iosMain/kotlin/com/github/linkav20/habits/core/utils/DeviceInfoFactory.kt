package com.github.linkav20.habits.core.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.allocPointerTo
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.UIKit.UIDevice
import platform.darwin.freeifaddrs
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.darwin.inet_ntop
import platform.posix.AF_INET
import platform.posix.INET_ADDRSTRLEN
import platform.posix.sa_family_t
import platform.posix.sockaddr_in

@OptIn(ExperimentalForeignApi::class)
public object DeviceInfoFactory {

    public fun getDeviceId(): String? {
        return UIDevice.currentDevice.identifierForVendor()?.UUIDString
    }

    public fun getDeviceName(): String {
        return UIDevice.currentDevice.name
    }

    public fun getIpAddress(): String? {
        val ipv4Regex = Regex(
            pattern = """\b(?:\d{1,3}\.){3}\d{1,3}\b""",
            options = setOf(RegexOption.IGNORE_CASE),
        )
        val (status, interfaces) = memScoped {
            val ifap = allocPointerTo<ifaddrs>()
            getifaddrs(ifap.ptr) to ifap.value
        }
        return try {
            generateSequence(interfaces.takeIf { status == 0 }) { it.pointed.ifa_next }
                .mapNotNull { it.pointed.ifa_addr }
                .mapNotNull {
                    val addr = when (it.pointed.sa_family) {
                        AF_INET.convert<sa_family_t>() -> it.reinterpret<sockaddr_in>().pointed.sin_addr
                        else -> return@mapNotNull null
                    }
                    memScoped {
                        val len = INET_ADDRSTRLEN
                        val dst = allocArray<ByteVar>(len)
                        inet_ntop(AF_INET.convert(), addr.ptr, dst, len.convert())?.toKString()
                    }
                }.firstOrNull { ipv4Regex.matches(it) }
        } catch (_: Exception) {
            null
        } finally {
            freeifaddrs(interfaces)
        }
    }
}

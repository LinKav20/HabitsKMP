package com.github.linkav20.habits.core.utils

import platform.UIKit.UIPasteboard

public actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}

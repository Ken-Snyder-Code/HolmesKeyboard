package com.kennesnyder.firstapp.swipe

import android.graphics.Rect
import android.view.View

/**
 * Minimal registry for keys so a future swipe engine can know which key is where.
 * For now we store id + label; we'll fill bounds later when we implement swipe.
 */
class KeyRegistry {
    data class KeyInfo(val id: Int, val label: String, var bounds: Rect? = null)

    private val keys = LinkedHashMap<Int, KeyInfo>()

    fun register(view: View, label: String) {
        keys[view.id] = KeyInfo(view.id, label)
    }

    fun updateBounds(view: View) {
        val info = keys[view.id] ?: return
        val xy = IntArray(2)
        view.getLocationOnScreen(xy)
        info.bounds = Rect(xy[0], xy[1], xy[0] + view.width, xy[1] + view.height)
    }

    fun all(): Collection<KeyInfo> = keys.values
    fun labelFor(id: Int): String? = keys[id]?.label
}

package com.kennesnyder.firstapp.theme

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ThemeManager(private val context: Context) {
    companion object {
        private const val PREFS = "holmes_prefs"
        private const val KEY_SKIN = "skin_id"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    // Keep just two skins for now
    fun availableSkins(): List<Skin> = listOf(
        Skin(
            id = "classic_dark",
            displayName = "Classic Dark",
            keyBg = 0xFF3B3B3B.toInt(),
            keyText = 0xFFEFEFEF.toInt(),
            keyBgMod = 0xFF545454.toInt(),
            keyTextMod = 0xFFFFFFFF.toInt(),
            keyboardBg = 0xFF1B1B1B.toInt()
        ),
        Skin(
            id = "retro_mint",
            displayName = "Retro Mint",
            keyBg = 0xFFBBF0D3.toInt(),
            keyText = 0xFF0E3B2E.toInt(),
            keyBgMod = 0xFF8FE3BF.toInt(),
            keyTextMod = 0xFF09261D.toInt(),
            keyboardBg = 0xFFEFFFF8.toInt()
        ),
        Skin(
            id = "pink",
            displayName = "Pink",
            keyBg = 0xFFE0888F.toInt(),     // dusty suit pink
            keyText = 0xFF514136.toInt(),   // boot brown letters
            keyBgMod = 0xFF774F4F.toInt(),  // hair/deeper pink
            keyTextMod = 0xFF423227.toInt(),// darker brown for modifiers
            keyboardBg = 0xFFF1E9E8.toInt() // soft pink background
        ),
        Skin(
        id = "deep_blue",
        displayName = "Deep Blue",
        keyBg = 0xFF2C79AF.toInt(),     // primary blue
        keyText = 0xFFFFFFFF.toInt(),   // white letters
        keyBgMod = 0xFF225F8A.toInt(),  // darker blue for modifiers
        keyTextMod = 0xFFFFFFFF.toInt(),// white for modifier text
        keyboardBg = 0xFFE6F0F7.toInt() // very light blue background
    )
    )

    fun getCurrentSkin(): Skin {
        val id = prefs.getString(KEY_SKIN, null)
        return availableSkins().firstOrNull { it.id == id } ?: availableSkins().first()
    }


    fun setCurrentSkin(id: String) {
        prefs.edit().putString(KEY_SKIN, id).apply()
    }

    /** Apply a simple, uniform theme to the whole keyboard for now. */
    fun applyTo(root: View) {
        val skin = getCurrentSkin()
        root.setBackgroundColor(skin.keyboardBg)
        tintButtonsRecursively(root, skin)
    }

    private fun tintButtonsRecursively(view: View, skin: Skin) {
        when (view) {
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    tintButtonsRecursively(view.getChildAt(i), skin)
                }
            }
            is Button -> {
                // Uniform styling for now; we’ll differentiate modifiers later.
                view.setBackgroundColor(skin.keyBg)
                view.setTextColor(skin.keyText)
            }
        }
    }
}

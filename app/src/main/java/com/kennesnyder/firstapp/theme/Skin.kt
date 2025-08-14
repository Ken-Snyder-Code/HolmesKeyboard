package com.kennesnyder.firstapp.theme

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * Visual definition for a keyboard "skin".
 * - keyBg/keyText: normal keys
 * - keyBgMod/keyTextMod: modifier/action keys (shift, delete, enter, space)
 * - keyboardBg: overall IME background when no pattern is used
 * - keyboardPattern: optional tiled bitmap drawable resource for patterned backgrounds
 */
data class Skin(
    val id: String,
    val displayName: String,
    @ColorInt val keyBg: Int,
    @ColorInt val keyText: Int,
    @ColorInt val keyBgMod: Int,
    @ColorInt val keyTextMod: Int,
    @ColorInt val keyboardBg: Int,
    @DrawableRes val keyboardPattern: Int? = null
)

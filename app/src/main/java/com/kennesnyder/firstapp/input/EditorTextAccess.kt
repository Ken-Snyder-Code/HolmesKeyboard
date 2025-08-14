package com.kennesnyder.firstapp.input

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedText
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection

object EditorTextAccess {

    /** Return true if the target field is a password/sensitive field we must not read. */
    fun isPasswordField(info: EditorInfo?): Boolean {
        if (info == null) return false
        val variation = info.inputType and InputType.TYPE_MASK_VARIATION
        val klass = info.inputType and InputType.TYPE_MASK_CLASS
        val isTextPassword =
            klass == InputType.TYPE_CLASS_TEXT && (
                    variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                            variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                            variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    )
        val isNumberPassword =
            klass == InputType.TYPE_CLASS_NUMBER &&
                    variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD
        return isTextPassword || isNumberPassword
    }

    /**
     * Best-effort snapshot for AI review:
     * 1) selected text if present
     * 2) else full extracted text if the app supports it
     * 3) else before/after cursor window up to maxChars
     */
    fun readForReview(
        ic: InputConnection?,
        info: EditorInfo?,
        maxChars: Int = 2000
    ): CharSequence? {
        if (ic == null || info == null) return null
        if (isPasswordField(info)) return null

        // Prefer the selection
        val sel = ic.getSelectedText(0)
        if (!sel.isNullOrEmpty()) return clamp(sel, maxChars)

        // Try full extract (works in many standard EditTexts)
        val et: ExtractedText? = ic.getExtractedText(ExtractedTextRequest(), 0)
        if (et != null && !et.text.isNullOrEmpty()) return clamp(et.text, maxChars)

        // Fallback: window around cursor
        val half = maxChars / 2
        val before = ic.getTextBeforeCursor(half, 0) ?: ""
        val after = ic.getTextAfterCursor(half, 0) ?: ""
        val window = (before.toString() + after.toString())
        return clamp(window, maxChars).takeIf { it.isNotEmpty() }
    }

    private fun clamp(cs: CharSequence, max: Int): CharSequence =
        if (cs.length <= max) cs else cs.subSequence(0, max)
}

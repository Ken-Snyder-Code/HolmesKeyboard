package com.kennesnyder.firstapp.input

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

/**
 * Centralized text insertion so tap-typing and (later) swipe share one path.
 */
class TextOutput(private val icProvider: () -> InputConnection?) {

    fun insertChar(ch: Char) {
        icProvider()?.commitText(ch.toString(), 1)
    }

    fun insertText(text: String) {
        icProvider()?.commitText(text, 1)
    }

    fun commitWord(word: String) {
        icProvider()?.commitText(word, 1)
    }

    fun backspace() {
        icProvider()?.deleteSurroundingText(1, 0)
    }

    /** Try DONE action; if not handled, insert newline. Returns true if action handled. */
    fun newlineOrAction(): Boolean {
        val ic = icProvider() ?: return false
        val handled = ic.performEditorAction(EditorInfo.IME_ACTION_DONE)
        if (!handled) ic.commitText("\n", 1)
        return handled
    }
}

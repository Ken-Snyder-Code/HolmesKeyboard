package com.kennesnyder.firstapp

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import com.kennesnyder.firstapp.input.EditorTextAccess
import com.kennesnyder.firstapp.input.TextOutput
import com.kennesnyder.firstapp.swipe.KeyRegistry
import com.kennesnyder.firstapp.theme.ThemeManager

class HolmesKeyboardService : InputMethodService() {

    // --- State & helpers ---
    private lateinit var themeManager: ThemeManager
    private var rootView: View? = null

    private lateinit var textOut: TextOutput
    private val keyRegistry = KeyRegistry()

    private var lastEditorInfo: EditorInfo? = null
    private fun ic() = currentInputConnection

    // --- Lifecycle ---
    override fun onCreate() {
        super.onCreate()
        themeManager = ThemeManager(this)
        textOut = TextOutput { ic() } // single path for inserts (tap & future swipe)
    }

    @SuppressLint("InflateParams") // IME input view has no parent; inflating with null is correct.
    override fun onCreateInputView(): View {
        // Root wraps suggestion strip + includes your existing keyboard_view
        val v = LayoutInflater.from(this).inflate(R.layout.keyboard_root, null)
        rootView = v

        // Apply current skin
        themeManager.applyTo(v)

        // Wire keys and register them for future swipe geometry
        wireKeys(v)

        return v
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        lastEditorInfo = info
        // Re-apply theme in case user changed skins while IME was alive
        rootView?.let { themeManager.applyTo(it) }

        Toast.makeText(this, "Holmes Keyboard active", Toast.LENGTH_SHORT).show()
        Log.d("HolmesIME", "Started for ${info?.packageName}")
    }

    override fun onEvaluateInputViewShown(): Boolean {
        // Keep behavior: always allow showing even if a hardware keyboard is present
        super.onEvaluateInputViewShown()
        return true
    }

    // --- Wiring ---

    private fun wireKeys(root: View) {
        // Helper binds tap and registers for future swipe pathing
        fun key(id: Int, label: String, onTap: () -> Unit) {
            root.findViewById<Button>(id)?.apply {
                setOnClickListener { onTap() }
                keyRegistry.register(this, label)
            }
        }

        // Letters (alpha layer)
        key(R.id.key_q, "q") { textOut.insertChar('q') }
        key(R.id.key_w, "w") { textOut.insertChar('w') }
        key(R.id.key_e, "e") { textOut.insertChar('e') }
        key(R.id.key_r, "r") { textOut.insertChar('r') }
        key(R.id.key_t, "t") { textOut.insertChar('t') }
        key(R.id.key_y, "y") { textOut.insertChar('y') }
        key(R.id.key_u, "u") { textOut.insertChar('u') }
        key(R.id.key_i, "i") { textOut.insertChar('i') }
        key(R.id.key_o, "o") { textOut.insertChar('o') }
        key(R.id.key_p, "p") { textOut.insertChar('p') }

        key(R.id.key_a, "a") { textOut.insertChar('a') }
        key(R.id.key_s, "s") { textOut.insertChar('s') }
        key(R.id.key_d, "d") { textOut.insertChar('d') }
        key(R.id.key_f, "f") { textOut.insertChar('f') }
        key(R.id.key_g, "g") { textOut.insertChar('g') }
        key(R.id.key_h, "h") { textOut.insertChar('h') }
        key(R.id.key_j, "j") { textOut.insertChar('j') }
        key(R.id.key_k, "k") { textOut.insertChar('k') }
        key(R.id.key_l, "l") { textOut.insertChar('l') }

        key(R.id.key_z, "z") { textOut.insertChar('z') }
        key(R.id.key_x, "x") { textOut.insertChar('x') }
        key(R.id.key_c, "c") { textOut.insertChar('c') }
        key(R.id.key_v, "v") { textOut.insertChar('v') }
        key(R.id.key_b, "b") { textOut.insertChar('b') }
        key(R.id.key_n, "n") { textOut.insertChar('n') }
        key(R.id.key_m, "m") { textOut.insertChar('m') }

        // Controls
        root.findViewById<Button>(R.id.key_space)?.setOnClickListener { textOut.insertText(" ") }
        root.findViewById<Button>(R.id.key_del)?.setOnClickListener { textOut.backspace() }
        root.findViewById<Button>(R.id.key_enter)?.setOnClickListener { textOut.newlineOrAction() }

        // AI Review (scaffold)
        root.findViewById<Button>(R.id.key_ai_review)?.setOnClickListener { runAiReview() }
    }

    /** Fetch text safely and insert a placeholder; later we’ll call the LLM here. */
    private fun runAiReview() {
        val text = EditorTextAccess.readForReview(ic(), lastEditorInfo, maxChars = 4000)
        if (text == null) {
            Toast.makeText(this, "No text to review (or password field).", Toast.LENGTH_SHORT).show()
            return
        }
        // Replace selection or insert at cursor with a placeholder for now
        currentInputConnection?.commitText("[AI REVIEW NOT IMPLEMENTED]", 1)
    }
}

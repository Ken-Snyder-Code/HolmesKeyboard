package com.kennesnyder.firstapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kennesnyder.firstapp.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // run the app full-bleed under system bars like a modern citizen
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // keep content from hiding under the status/nav bars
        val root = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // find the input box we added in activity_main.xml
        val box = findViewById<EditText>(R.id.box)

        // focus it and ask (politely but firmly) for the soft keyboard
        focusAndShowKeyboard(box)

        // NEW: open the keyboard theme picker
        findViewById<Button>(R.id.btn_choose_theme)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    // zero ceremony: request focus and pop the IME
    private fun focusAndShowKeyboard(target: EditText) {
        target.requestFocus()
        target.post {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // SHOW_IMPLICIT lets Android bring up whatever the current IME is (ours, if selected)
            imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

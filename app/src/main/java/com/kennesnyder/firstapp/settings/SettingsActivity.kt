package com.kennesnyder.firstapp.settings

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.kennesnyder.firstapp.R
import com.kennesnyder.firstapp.theme.ThemeManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeManager = ThemeManager(this)

        val list = findViewById<ListView>(R.id.list_skins)
        val skins = themeManager.availableSkins()
        val names = skins.map { it.displayName }

        list.choiceMode = ListView.CHOICE_MODE_SINGLE
        list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, names)

        // Preselect current
        val current = themeManager.getCurrentSkin()
        val selectedIdx = skins.indexOfFirst { it.id == current.id }.coerceAtLeast(0)
        list.setItemChecked(selectedIdx, true)

        list.setOnItemClickListener { _, _, position, _ ->
            themeManager.setCurrentSkin(skins[position].id)
            // No need to restart; IME will pick it up next time it’s shown
            finish()
        }
    }
}

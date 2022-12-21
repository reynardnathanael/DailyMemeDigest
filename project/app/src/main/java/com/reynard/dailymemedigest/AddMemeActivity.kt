package com.reynard.dailymemedigest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_meme.*

class AddMemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meme)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        txtUrlAdd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var url = txtUrlAdd.text.toString()
                if (url != "") Picasso.get().load(url).into(imgMemePreview)
            }

        })
    }
}
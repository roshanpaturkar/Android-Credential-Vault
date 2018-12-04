package mykotlin.roshanpaturkar.com.keysstand.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import mykotlin.roshanpaturkar.com.keysstand.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        aboutUsCardView.setCardBackgroundColor(Color.TRANSPARENT)
        supportActionBar!!.title = "About Us!"
    }
}

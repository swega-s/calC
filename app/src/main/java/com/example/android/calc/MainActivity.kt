package com.example.android.calc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().
            replace(R.id.main_fragment, HomeFragment()).commit()

        addButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, InputFragment()).commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
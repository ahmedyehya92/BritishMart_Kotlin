package com.netservex.caf.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.netservex.caf.R
import com.netservex.caf.features.main.MainActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        timer = Timer("SettingUp", false)
        timer?.schedule(3000) {
            Intent(this@SplashActivity, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}

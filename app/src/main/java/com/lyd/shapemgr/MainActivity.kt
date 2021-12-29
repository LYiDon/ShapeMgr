package com.lyd.shapemgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lyd.shape.ShapeMgr

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ShapeMgr.bind(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
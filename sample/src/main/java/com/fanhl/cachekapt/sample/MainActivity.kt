package com.fanhl.cachekapt.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    private var count = 0
        get() {
            field++
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("counter:$count")
    }
}

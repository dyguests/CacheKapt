package com.fanhl.cachekapt.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fanhl.cachekapt.annotation.Cache
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @Cache
    var count = 0
        get() {
            field++
            return field
        }

    @Cache
    val test1 = 0
    @Cache
    val test2 = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_counter.setOnClickListener { tv_counter.text = "counter:$count" }
        btn_counter.callOnClick()

        countCache
    }
}

fun MainActivity.loadCount() {
    count = 20
}

var MainActivity.countCache22: Int
    get() {
        //todo
        return count
    }
    set(value) {
        count = value
    }

interface A {
    var a: Int
}

open class B : A {
    override var a: Int = 0
        get() {
            //print xxx
            return a
        }
}

class C : B() {
    override var a: Int = 0
        get() {
            //print yyy
            return super.a
        }
}
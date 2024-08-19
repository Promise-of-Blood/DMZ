package com.example.dmz

import android.app.Application

class DMZApplication : Application() {
    val applicationContext by lazy { this }
}
package com.example.registrationlogin


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.user_registration.*
import kotlinx.android.synthetic.main.welcome_page.*
import kotlinx.android.synthetic.main.welcome_page.view.*


class welcomeActivity:AppCompatActivity(){
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.welcome_page)
        var intent=getIntent()
       val lemail=intent.getStringExtra("M")
        val lpassword=intent.getStringExtra("P")
val tv_result=findViewById<TextView>(R.id.tv_result)
        tv_result.text= "M: $lemail\nP: $lpassword"

    }
}
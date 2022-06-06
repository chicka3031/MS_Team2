package com.example.ms_team2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_mainpage.*

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
Log.d("taga", "mainpage")
        //MyTeam 버튼
        btnMyTeam.setOnClickListener {
            val intent = Intent(this, MyTeam::class.java)
            startActivity(intent)
        }

        //MatchList 버튼
        btnMatchList.setOnClickListener {
            val intent = Intent(this, MatchList::class.java)
            startActivity(intent)
        }

        //Chatting 버튼
        btnChatting.setOnClickListener {
            val intent = Intent(this, Chatting::class.java)
            startActivity(intent)
        }

        //Predict 버튼
        btnPredict.setOnClickListener {
            val intent = Intent(this, Predict::class.java)
            startActivity(intent)
        }

        //Video 버튼
        btnVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/SPOTV오리지널"))
            startActivity(intent)
        }

        //Article 버튼
        btnArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sports.news.naver.com/wfootball/index"))
            startActivity(intent)
        }
    }
}
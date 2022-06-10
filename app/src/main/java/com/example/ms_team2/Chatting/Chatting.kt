package com.example.ms_team2.Chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ms_team2.Main.MainPage
import com.example.ms_team2.R
import com.example.ms_team2.databinding.ActivityChattingBinding
import kotlinx.android.synthetic.main.activity_chatting.*

class Chatting : AppCompatActivity() {
    private lateinit var binding : ActivityChattingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 앱 구동시 LoginFragment 표시
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, LoginFragment())
            .commit()

        //Back 버튼
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
    }

    // ChatFragment로 프래그먼트 교체 (LoginFragment에서 호출할 예정)
    fun replaceFragment(bundle: Bundle) {
        val destination = ChatFragment()
        destination.arguments = bundle      // 닉네임을 받아옴
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, destination)
            .commit()
    }
}
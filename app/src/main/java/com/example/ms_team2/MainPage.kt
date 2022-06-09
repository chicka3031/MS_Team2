package com.example.ms_team2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ms_team2.Match.MatchList
import com.example.ms_team2.Team.MyTeam
import com.example.ms_team2.databinding.ActivityMainBinding
import com.example.ms_team2.databinding.ActivityMainpageBinding
import kotlinx.android.synthetic.main.activity_mainpage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainPage : AppCompatActivity() {
    lateinit var binding: ActivityMainpageBinding
    lateinit var adapter: ArticleAdapter
    var newsurl = "https://sports.news.naver.com/wfootball/news/index?isphoto=N"
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        getNews()
    }

    fun getNews(){
        binding.ArticleRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ArticleAdapter(ArrayList<ArticleData>())


        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(newsurl).get()
            Log.d("doc", doc.toString())
            val headlines = doc.select("ul.aside_news_list>li>a>span")
            Log.d("healines", headlines.toString())

            for(headline in headlines){
                adapter.items.add(ArticleData(headline.text()))
                Log.d("ihi", headline.text())

            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }
        binding.ArticleRecycler.adapter = adapter
    }
}
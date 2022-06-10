package com.example.ms_team2.Main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ms_team2.Chatting.Chatting
import com.example.ms_team2.Match.MatchList
import com.example.ms_team2.Predict
import com.example.ms_team2.R
import com.example.ms_team2.Team.MyTeam
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_mainpage.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
        Log.d("taga", "mainpage")

        listArticle.layoutManager = LinearLayoutManager(this)

        doTask("https://sports.news.naver.com/wfootball/news/index?isphoto=N")

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
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/SPOTV오리지널"))
            startActivity(intent)
        }

        //Article 버튼
        btnArticle.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sports.news.naver.com/wfootball/index")
            )
            startActivity(intent)
        }
    }

    fun doTask(url : String) {
        var documentTitle : String = ""
        var itemList : ArrayList<articleItem> = arrayListOf()

        Single.fromCallable {
            try {
                val doc = Jsoup.connect(url).get()

                val elements : Elements = doc.select("div.news_list ul li")

                run elemLoop@{
                    elements.forEachIndexed{ index, elem ->

                        var title = elem.select("div.text a").text()
                        var explain = elem.select("p.desc").text()
                        var time = elem.select("div.source span.time span.bar").text()
                        var poster = elem.select("a.thumb img").attr("src")

                        var item = articleItem(title, explain, time, poster)
                        itemList.add(item)

                        if (index == 7) return@elemLoop
                    }
                }

                documentTitle = doc.title()
            } catch (e : Exception) {e.printStackTrace()}

            return@fromCallable documentTitle
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { text -> listArticle.adapter = ArticleAdapter(itemList) },
                { it.printStackTrace() }
            )
    }
}

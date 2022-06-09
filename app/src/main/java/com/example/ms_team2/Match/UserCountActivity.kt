package com.example.ms_team2.Match

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ms_team2.R
import com.google.firebase.database.*

class UserCountActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var databaseRef: DatabaseReference
    lateinit var matchTeamInfo: MatchTeamInfo
    private var totalVoteCnt = 0
    private var aTeamVoteCnt = 0
    private var bTeamVoteCnt = 0

    private lateinit var hometeam: String
    private lateinit var awayteam: String
    private lateinit var match_date: String
    private lateinit var match_time: String
    private lateinit var hometeamimg: String
    private lateinit var awayteamimg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_count)

        val intent = intent

        hometeam = intent.getStringExtra("hometeam").toString()
        awayteam = intent.getStringExtra("awayteam").toString()
        match_date = intent.getStringExtra("match_date").toString()
        match_time = intent.getStringExtra("match_time").toString()
        hometeamimg = intent.getStringExtra("hometeamimg").toString()
        awayteamimg = intent.getStringExtra("awayteamimg").toString()

        matchTeamInfo = MatchTeamInfo()
        matchTeamInfo.aTeamName = hometeam
        matchTeamInfo.bTeamName = awayteam
        matchTeamInfo.matchDate = match_date + match_time


        findViewById<Button>(R.id.btn_left).setOnClickListener(this)
        findViewById<Button>(R.id.btn_right).setOnClickListener(this)
        findViewById<Button>(R.id.btn_prediction).setOnClickListener(this)

        Glide.with(this).load(hometeamimg)
            .override(150, 150).into(findViewById(R.id.iv_left))
        Glide.with(this).load(awayteamimg)
            .override(150, 150).into(findViewById(R.id.iv_right))

        val tvLeft: TextView = findViewById(R.id.tv_left)
        val tvRight: TextView = findViewById(R.id.tv_right)

        databaseRef = FirebaseDatabase.getInstance().getReference("MatchTeamInfo")
        databaseRef.child("${"$match_date $match_time"} ${"$hometeam _ $awayteam"}")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        matchTeamInfo = snapshot.getValue(MatchTeamInfo::class.java)!!
                        matchTeamInfo.let {
                            // update current vote count
                            aTeamVoteCnt = it.aTeamVoteCnt
                            bTeamVoteCnt = it.bTeamVoteCnt
                            totalVoteCnt = (aTeamVoteCnt + bTeamVoteCnt)

                            tvLeft.text =
                                (String.format("%.2f", (aTeamVoteCnt * 100.0) / totalVoteCnt)) + "%"
                            tvRight.text =
                                (String.format("%.2f", (bTeamVoteCnt * 100.0) / totalVoteCnt)) + "%"
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase Error !", error.message)
                }
            })

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_left -> {
                // A 승리
                matchTeamInfo.aTeamVoteCnt++
                databaseRef.child("${"$match_date $match_time"} ${"$hometeam _ $awayteam"}")
                    .setValue(matchTeamInfo)
            }

            R.id.btn_right -> {
                // B 승리
                matchTeamInfo.bTeamVoteCnt++
                databaseRef.child("${"$match_date $match_time"} ${"$hometeam _ $awayteam"}")
                    .setValue(matchTeamInfo)
            }

            R.id.btn_prediction -> {
                // 승부 예측
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.sports.naver.com/wfootball/predict")
                )
                startActivity(intent)
            }
        }
    }


}
package com.example.ms_team2.Match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ms_team2.Team.MyDBHelper
import com.example.ms_team2.Team.MyTeamData
import com.example.ms_team2.Team.TeamAdapter
import com.example.ms_team2.Team.TeamData
import com.example.ms_team2.databinding.ActivityMatchListMyTeamBinding
import kotlinx.android.synthetic.main.myteam_row.*
import org.json.JSONObject

class MatchListMyTeam : AppCompatActivity() {
    lateinit var binding: ActivityMatchListMyTeamBinding
    lateinit var myDBHelper: MyDBHelper

    var MyTeam_League = ArrayList<String>()
    var MyTeam_Name = ArrayList<String>()
    var Match_Id = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchListMyTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()  //db가져오기

    }

    fun init(){
        MyTeam_League.clear()
        MyTeam_Name.clear()
        Match_Id.clear()

        myDBHelper = MyDBHelper(this)
        val strsql = "select * from ${MyDBHelper.TABLE_NAME};"  //질의문
        Log.d("dbcursor", MyDBHelper.TABLE_NAME)
        val db = myDBHelper.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        cursor.moveToFirst()

        if(cursor.count!=0){
            do {
                Log.d("dbcursor", cursor.getString(1))
                Log.d("dbcursor", cursor.getString(2))

                MyTeam_League.add(cursor.getString(1))  //리그 추가
                MyTeam_Name.add(cursor.getString(2))    //팀이름 추가

            }while(cursor.moveToNext())
            cursor.close()
            db.close()

            initrecycler_myteam()
            initrecycler_match()
        }else{
            Toast.makeText(this, "즐겨찾기 팀이 없습니다", Toast.LENGTH_LONG).show()
        }





    }

    fun initrecycler_myteam() {
        lateinit var adapter: MyTeamAdapter
        binding.myteamRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MyTeamAdapter(ArrayList<MyTeamData>())
        binding.myteamRecycler.adapter = adapter



        for(i in 0 .. MyTeam_League.size-1 step (1)){
            val filename = "Team_"+MyTeam_League.get(i)+".json"

            val jsonString = assets.open(filename).reader().readText()
            val jsonObject = JSONObject(jsonString)
            val standings_array = jsonObject.getJSONArray("standings").getJSONObject(0)     //type=TOTAL인 {}
            val team_array = standings_array.getJSONArray("table")  //team array

            var j=0
            while(j < team_array.length()){
                val teamname = team_array.getJSONObject(j).getJSONObject("team").getString("name")
                val teamimg = team_array.getJSONObject(j).getJSONObject("team").getString("crest")

                if(teamname.equals(MyTeam_Name.get(i))){
                    adapter.items.add(MyTeamData(MyTeam_League.get(i), MyTeam_Name.get(i), teamimg))
                    break
                }

                j++
            }
        }

        //누르면 해당 팀 경기만 출력
        adapter.itemClickListener = object: MyTeamAdapter.OnItemClickListener {
            override fun OnItemClick(data: MyTeamData) {
                MyTeam_League.clear()
                MyTeam_Name.clear()
                MyTeam_League.add(data.League)
                MyTeam_Name.add(data.teamname)
                initrecycler_match()
            }
        }

    }

    fun initrecycler_match() {
        lateinit var adapter: ScheAdapter
        binding.matchlistMyTeamRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ScheAdapter(ArrayList<ScheData>())
        binding.matchlistMyTeamRecycler.adapter = adapter

        //json 파싱해서 해당 정보 가져오기
        Log.d("Matche", (MyTeam_League.size-1).toString())
        for(i in 0 .. MyTeam_League.size-1 step (1)){
            var filename = "Match_"+MyTeam_League.get(i)+".json"
            val jsonString = assets.open(filename).reader().readText()
            val jsonObject = JSONObject(jsonString)
            val match_array = jsonObject.getJSONArray("matches")

            var j=0
            while(j< match_array.length()){
                val match_id = match_array.getJSONObject(j).getString("id")
                val hometeam = match_array.getJSONObject(j).getJSONObject("homeTeam")
                val hometeamname = hometeam.getString("shortName")
                val hometeamfullname = hometeam.getString("name")
                val hometeamimg = hometeam.getString("crest")

                val awayteam = match_array.getJSONObject(j).getJSONObject("awayTeam")
                val awayteamname = awayteam.getString("shortName")
                val awayteamfullname = awayteam.getString("name")
                val awayteamimg = awayteam.getString("crest")

                val utcDate = match_array.getJSONObject(j).getString("utcDate").toString()
                val match_date = utcDate.substring(0, utcDate.indexOf("T"))
                var match_time = utcDate.substring(11 until 16)

                if(hometeamfullname.equals(MyTeam_Name.get(i)) || awayteamfullname.equals(MyTeam_Name.get(i))){
                    //경기 중복 검사
                    var flag = true
                    var k=0
                    for(k in 0 .. Match_Id.size-1 step (1)){
                        if(Match_Id.get(k).equals(match_id)){
                            flag = false
                            break
                        }
                    }
                    if(flag){   //중복 아니면 add
                        adapter.items.add(ScheData(hometeamname, hometeamimg, awayteamname, awayteamimg, match_date, match_time, match_id))
                        Log.d("add", match_id)
                    }
                }
                j++
            }

        }

    }
}
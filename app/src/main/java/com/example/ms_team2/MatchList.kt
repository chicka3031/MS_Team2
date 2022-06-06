package com.example.ms_team2
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide.init
import com.example.ms_team2.databinding.ActivityMainBinding
import com.example.ms_team2.databinding.ActivityMatchListBinding
import org.json.JSONObject
import java.time.Month


class MatchList : AppCompatActivity() {
    lateinit var binding: ActivityMatchListBinding
    lateinit var adapter: ScheAdapter

    lateinit var League : String
    //lateinit var Season : String
    lateinit var Month : String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("taga", "matchlist")
        super.onCreate(savedInstanceState)
        binding = ActivityMatchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //기본값
        League = "PL"
        //Season = "2021"
        Month = "00"

        init()
        SelectLeague()
        SelectMonth()

        //Back 버튼
//        btnBack.setOnClickListener {
//            val intent = Intent(this, MainPage::class.java)
//            startActivity(intent)
//        }

    }

    fun init() {
        Log.d("taga", "init")
//        binding.MyTeambtn.setOnClickListener {
//            val intent = Intent(this, TeamListActivity::class.java)
//            startActivity(intent)
//        }

        //val filename = "Team_"+League+".json"
    }

    fun SelectLeague(){     //리그 선택
        Log.d("taga", "select")
        var LeagueArray = listOf("PL", "BL1", "WC")
        var Leagueadapter = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, LeagueArray)
        binding.leagueSpinner.adapter = Leagueadapter
        binding.leagueSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("selected", LeagueArray.get(position)+"선택")
                League = LeagueArray.get(position)
                //Month = "00"    //리그 바꾸면 월 초기화
                // init()
                SelectMonth()
                initRecycler()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


    fun SelectMonth(){      //월 선택
        Log.d("taga", "selectmonth")
        val jsonString = assets.open("Match_"+League+".json").reader().readText()
        val jsonObject = JSONObject(jsonString).getJSONObject("resultSet")
        var startmonth = jsonObject.getString("first").substring(5 until 7).toInt()
        var endmonth = jsonObject.getString("last").substring(5 until 7).toInt()
        //Log.d("check", startmonth)

        var diff = endmonth - startmonth
        var loop:Int
        if(diff>=0){
            loop = diff+1
        }else{
            loop = 13+diff
        }


        Log.d("date", startmonth.toString() + endmonth.toString())
        var MonthArray = mutableListOf<String>()
        MonthArray.add("00")
        for(i in 1..loop){
            if(startmonth.toInt()>12)
                startmonth = 1
            if(startmonth<10)
                MonthArray.add("0"+startmonth.toString())
            else
                MonthArray.add(startmonth.toString())
            Log.d("check", "$i $startmonth")
            startmonth++
        }

        var Monthadapter = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, MonthArray)
        binding.monthSpinner.adapter = Monthadapter
        binding.monthSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("selected", MonthArray.get(position)+"선택")
                Month = MonthArray.get(position)
                initRecycler()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


    // }
    fun initRecycler() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ScheAdapter(ArrayList<ScheData>())

//        adapter.itemClickListener = object: MyAdapter.OnItemClickListener{
//            override fun OnItemClick(data: MyData) {
//
//            }
//        }

        binding.recyclerView.adapter = adapter
        val filename = "Match_"+League+".json"
        Log.d("filename", filename)

        val jsonString = assets.open(filename).reader().readText()
        val jsonObject = JSONObject(jsonString)
        val match_array = jsonObject.getJSONArray("matches")

        // val team_array = standings_array.getJSONArray("table")  //team array
        //val matchcnt = match_array.length()
        var i=0
        var str = ""

        while(i< match_array.length()){
            val hometeam = match_array.getJSONObject(i).getJSONObject("homeTeam")
            val hometeamname = hometeam.getString("shortName")
            val hometeamimg = hometeam.getString("crest")

            val awayteam = match_array.getJSONObject(i).getJSONObject("awayTeam")
            val awayteamname = awayteam.getString("shortName")
            val awayteamimg = awayteam.getString("crest")

            val utcDate = match_array.getJSONObject(i).getString("utcDate").toString()
            val match_date = utcDate.substring(0, utcDate.indexOf("T"))
            var match_time = utcDate.substring(11 until 16)

            var str_match_date = match_date.split("-")

            //추가
            if(Month == "00"){   //아무것도 선택 X-> 다보여줌
                adapter.items.add(ScheData(hometeamname, hometeamimg, awayteamname, awayteamimg, match_date, match_time))
            }
            else{   //월 선택 -> 해당 월만 보여줌
                if(str_match_date.get(1).toString() == Month){
                    adapter.items.add(ScheData(hometeamname, hometeamimg, awayteamname, awayteamimg, match_date, match_time))
                }
            }

            i++
        }

    }

}

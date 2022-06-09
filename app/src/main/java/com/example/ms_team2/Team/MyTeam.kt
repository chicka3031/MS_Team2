package com.example.ms_team2.Team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ms_team2.Match.MyTeamAdapter
import com.example.ms_team2.databinding.ActivityMyTeamBinding
import org.json.JSONObject

class MyTeam : AppCompatActivity() {
    lateinit var binding: ActivityMyTeamBinding

    lateinit var League : String

    lateinit var myDBHelper: MyDBHelper

    var MyTeam_League = ArrayList<String>()
    var MyTeam_Name = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        League = "PL"
        init()
        //getAllRecord()
        //initDB()

    }

    fun getAllRecord(){
        myDBHelper.getAllRecord()
    }

    fun init() {
        myDBHelper = MyDBHelper(this)

        var LeagueArray = listOf("리그를 선택하세요","BL1","BSA","CL","CLI","DED","ELC","FL1","PD", "PL","PPL","SA","WC")
        var Leagueadapter = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, LeagueArray)
        binding.spinner.adapter = Leagueadapter
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Log.d("selected", LeagueArray.get(position)+"선택")
                if(position == 0){
                    League = "PL"
                }else{
                    League = LeagueArray.get(position)
                }
                //Log.d("init", "here")
                initRecycler()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }



    fun initRecycler(){
        lateinit var adapter: TeamAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = TeamAdapter(ArrayList<TeamData>())

        adapter.itemClickListener = object: TeamAdapter.OnItemClickListener {
            override fun OnItemClick(data: TeamData) {
                data.isSelected = !data.isSelected
                Log.d("click", "Activity")
                if(data.isSelected){
                    val league = League
                    val teamname = data.teamname
                    val myteam = MyTeamData(league, teamname)
                    val result = myDBHelper.insertData(myteam)

                    if(result){
                      //  getAllRecord()
                    }
                }else{  //클릭해서 false->delete
                    val result = myDBHelper.deleteData(data.teamname)
                    Log.d("delete","delete")
//                    if(result){
//                        getAllRecord()
//                    }
                }
                initDB()
                initrecycler_myteam2()
            }
        }

        binding.recyclerView.adapter = adapter
        val filename = "Team_"+League+".json"

        val jsonString = assets.open(filename).reader().readText()
        val jsonObject = JSONObject(jsonString)
        val standings_array = jsonObject.getJSONArray("standings").getJSONObject(0)     //type=TOTAL인 {}
        val team_array = standings_array.getJSONArray("table")  //team array

        var i=0
        var str = ""

        while(i < team_array.length()){
            val teamname = team_array.getJSONObject(i).getJSONObject("team").getString("name")
            val position = team_array.getJSONObject(i).getString("position").toInt()
            val teamimg = team_array.getJSONObject(i).getJSONObject("team").getString("crest")
            Log.d("img", teamimg)
            val result = myDBHelper.findProduct(teamname)
            if(result) { //존재
                adapter.items.add(TeamData(teamname, position, teamimg, true))
            }else{
                adapter.items.add(TeamData(teamname, position, teamimg, false))
            }

            i++
        }

        //binding.textView.text = teamname
    }

    fun initDB(){
        MyTeam_League.clear()
        MyTeam_Name.clear()

        //myDBHelper = MyDBHelper(this)
        val strsql = "select * from ${MyDBHelper.TABLE_NAME};"  //질의문
        Log.d("dbcursor", MyDBHelper.TABLE_NAME)
        val db = myDBHelper.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        cursor.moveToFirst()
        do {
            if(cursor.count!=0){
                Log.d("dbcursor_initdb", cursor.getString(1))
                Log.d("dbcursor_initdb", cursor.getString(2))

                MyTeam_League.add(cursor.getString(1))  //리그 추가
                MyTeam_Name.add(cursor.getString(2))    //팀이름 추가

            }
        }while(cursor.moveToNext())
        cursor.close()
        db.close()
    }

    fun initrecycler_myteam2() {
        lateinit var adapter: MyTeamAdapter
        binding.myteamRecycler2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MyTeamAdapter(ArrayList<MyTeamData>())
        binding.myteamRecycler2.adapter = adapter

        if(MyTeam_League.size!=0){
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
                        //Log.d("matchlistmyteam", "successs")
                        adapter.items.add(MyTeamData(MyTeam_League.get(i), MyTeam_Name.get(i), teamimg))
                        break
                    }

                    j++
                }
            }
        }else{
            Log.d("myteamelse","즐찾팀없")
            Toast.makeText(this, "줄겨찾기 팀이 없습니다", Toast.LENGTH_LONG).show()
        }


        //누르면 삭제
        adapter.itemClickListener = object: MyTeamAdapter.OnItemClickListener {
            override fun OnItemClick(data: MyTeamData) {
                myDBHelper.deleteData(data.teamname)
                initDB()
                initRecycler()
                initrecycler_myteam2()
            }
        }

    }
}
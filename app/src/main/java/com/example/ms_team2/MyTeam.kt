package com.example.ms_team2

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ms_team2.databinding.ActivityMyTeamBinding
import org.json.JSONObject

class MyTeam : AppCompatActivity() {
    lateinit var binding: ActivityMyTeamBinding
    lateinit var adapter: TeamAdapter
    lateinit var League : String

    lateinit var myDBHelper: MyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        League = "PL"
        init()
        getAllRecord()
        //initRecycler()
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = TeamAdapter(ArrayList<TeamData>())

        adapter.itemClickListener = object: TeamAdapter.OnItemClickListener{
            override fun OnItemClick(data: TeamData) {
                data.isSelected = !data.isSelected
                Log.d("click", "Activity")
                if(data.isSelected){
                    val league = League
                    val teamname = data.teamname
                    val myteam = MyTeamData(league, teamname)
                    val result = myDBHelper.insertData(myteam)

                    if(result){
                        getAllRecord()
                    }
                }else{  //클릭해서 false->delete
                    val result = myDBHelper.deleteData(data.teamname)
                    if(result){
                        getAllRecord()
                    }
                }
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
}
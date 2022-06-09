package com.example.ms_team2.Team

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class MyDBHelper(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{   //외부에서 입력안받고 여기서 사용하면됨
        val DB_NAME = "myteam.db"
        val DB_VERSION = 1
        val TABLE_NAME = "myteam"

        val LEAGUE = "league"
        val TEAMNAME = "teamname"
        val ID = "id"
    }

    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        showRecord(cursor)
        cursor.close()
        db.close()
    }

    private fun showRecord(cursor: Cursor) {
//        cursor.moveToFirst()
//        val attrcount = cursor.columnCount
//        val activity = context as MyTeam  //TeamListActivity 멤버 접근 가능해짐
//        activity.binding.tableLayout.removeAllViewsInLayout()   //테이블레이아웃의 모든 뷰를 지우겠다.
//
//        //타이틀 만들기
//        val tablerow = TableRow(activity)
//        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)  //레이아웃의 파라미터 설정
//        tablerow.layoutParams = rowParam
//        val viewParam = TableRow.LayoutParams(0, 100, 1f)
//        for(i in 0 until attrcount){
//            val textView = TextView(activity)
//            textView.layoutParams = viewParam
//            textView.text = cursor.getColumnName(i)
//            textView.setBackgroundColor(Color.LTGRAY)
//            textView.textSize = 15.0f
//            textView.gravity = Gravity.CENTER
//            tablerow.addView(textView)
//        }
//        activity.binding.tableLayout.addView(tablerow)
//        if(cursor.count==0) return  //여기서 멈춤, 레코드 추가X
//        // 레코드 추가
//        do {
//            val row = TableRow(activity)
//            row.layoutParams = rowParam
//            row.setOnClickListener{
//                for(i in 0 until attrcount){
//                    val textView = row.getChildAt(i) as TextView
//                   // Log.d("rowclick", textView)
//                    when(textView.tag){
//                        2->{
//                            Log.d("rowclick", textView.text.toString())
//                            deleteData(textView.text.toString())
//                            getAllRecord()
//                        }
//                    }
//                }
//            }
//            for(i in 0 until attrcount){
//                val textView = TextView(activity)
//                textView.tag = i //view 식별
//                textView.layoutParams = viewParam
//                textView.text = cursor.getString(i)
//                textView.textSize = 13.0f
//                textView.gravity = Gravity.CENTER
//                row.addView(textView)
//            }
//            activity.binding.tableLayout.addView(row)  //추가
//        }while(cursor.moveToNext()) //다음이 있으면 반복
    }

    fun insertData(myteam: MyTeamData):Boolean{
        val values = ContentValues()
        values.put(LEAGUE, myteam.League)
        values.put(TEAMNAME, myteam.teamname)
        val db = writableDatabase
        Log.d("dbhelper", myteam.League+" "+ myteam.teamname)
        //db삽입 작업
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag
    }


    fun deleteData(teamname: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $TEAMNAME ='$teamname';"  //질의문
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)  //제품 있는지 찾음
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME, "$TEAMNAME=?", arrayOf(teamname))
        }
        cursor.close()
        db.close()
        return flag
    }

    override fun onCreate(db: SQLiteDatabase?) {    //db처음 만들어질때만 호출
        val create_table = "create table if not exists $TABLE_NAME("+
                "$ID integer primary key autoincrement, "+
                "$LEAGUE text, "+
                "$TEAMNAME text);"  //SQL 구문

        db!!.execSQL(create_table)   //db가 null아니면 create_table실행
    }

    fun findProduct(teamname: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $TEAMNAME ='$teamname';"  //질의문
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        showRecord(cursor)
        cursor.close()
        db.close()
        return flag
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db바뀐경우
//        val drop_table= "drop table if exists $TABLE_NAME;"
//        db!!.execSQL(drop_table)
//        onCreate(db)
    }

}
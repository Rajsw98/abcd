package com.example.registrationlogin

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context:Context):SQLiteOpenHelper(context, Constants.DB_NAME,null,Constants.DB_VERSION) {
    override fun onCreate(p0:SQLiteDatabase?){
        p0?.execSQL(Constants.CREATE_TABLE)
        //STORE USER DATA

    }
    override fun onUpgrade(db:SQLiteDatabase?,oldVersion:Int,newVersion:Int) {
        db?.execSQL("DROP TABLE IF EXISTS"+Constants.TABLE_NAME)
        onCreate(db)
    }
    fun insertUserData(name:String?,email: String?, image:String?,password: String?,addedTime:String?,updatedTime:String?):Long{
        val db=this.writableDatabase
        val values=ContentValues()
        values.put(Constants.C_NAME,name)
        values.put(Constants.C_IMAGE,image)
        values.put(Constants.C_EMAIL,email)
        values.put(Constants.C_PASSWORD,password)
        values.put(Constants.C_ADDED_TIMESTAMP,addedTime)
        values.put(Constants.C_UPDATED_TIMESTAMP,updatedTime)
        val id=db.insert(Constants.TABLE_NAME,null,values)
        db.close()
        return id
    }





    fun userPresent(email:String,password:String):Boolean{
        val db = writableDatabase
        val query="select * from MY_RECORDS_TABLE where email = '$email' and password = '$password'"
        val cursor=db.rawQuery(query,null)
        if (cursor.count<=0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    }




package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE, null, VERSION) {
    companion object{
        const val DATABASE = "SQL13.db"
        const val VERSION = 1
        const val TABLE = "bang1"
        const val ID = "id"
        const val NAME = "name"
        const val DATE = "date"
        const val IMAGE = "image"
        const val CREATE_TABLE =
            " CREATE TABLE " + TABLE + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT, "+
                    DATE + " TEXT, "+
                    IMAGE + " INTEGER " + ");"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    fun getAllData() : MutableList<User>{
        val list = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.rawQuery(" SELECT * FROM " + TABLE,null)
        if(cursor.moveToFirst()){
            do {
                val user = User()
                user.id = cursor.getInt(0)
                user.name = cursor.getString(1)
                user.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                user.image = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE))
                list.add(user)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
    fun insertUser(user: User){
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(NAME, user.name)
            put(DATE, user.date)
            put(IMAGE, user.image)
        }
        db.insert(TABLE, null, contentValues)
        db.close()
    }
    fun checkUser(name: String): Boolean{
        val db = readableDatabase
        val cursor = db.query(TABLE, null, "$NAME = ? ", arrayOf(name), null,null,null,null)
        if(cursor.count > 0){
            cursor.close()
            db.close()
            return true
        }else{
            cursor.close()
            db.close()
            return false
        }
    }
    fun update(user: User){
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, user.name)
        contentValues.put(DATE, user.date)
        db.update(TABLE, contentValues, "$ID = ? ", arrayOf(user.id.toString()))
        db.close()
    }
    fun delete(id: Int){
        val db = writableDatabase
        db.delete(TABLE, "$ID = ? ", arrayOf(id.toString()))
        db.close()
    }
    fun searchUser(name: String): MutableList<User>{
        val list = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.query(TABLE, null, "$NAME = ? ", arrayOf(name),null,null,null)
        if(cursor.moveToFirst()){
            do {
                val user = User()
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                user.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                user.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                user.image = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE))
                list.add(user)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}
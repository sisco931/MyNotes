package com.example.mynotes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyNotesSQLiteOpenHelper(context: Context): SQLiteOpenHelper(context, "MyNotesDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

         db!!.execSQL(
             "CREATE TABLE notes("
                     + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + "title TEXT,"
                     + "description TEXT)"
         )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
         //To change body of created functions use File | Settings | File Templates.
    }
}
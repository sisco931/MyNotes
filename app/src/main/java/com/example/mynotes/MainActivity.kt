package com.example.mynotes

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var db:SQLiteDatabase? =null
    private var cursor:Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Open Note Details Activity
        fabAddNote.setOnClickListener {
            openNoteDetailsActivity(0)
        }
        listViewNotes.setOnItemClickListener { parent, view, position, id ->
            openNoteDetailsActivity(id)
        }

        //create DB & notes table
    }
    fun openNoteDetailsActivity(noteId:Long)
    {
        val intent = Intent(this, NoteDetails::class.java)
        intent.putExtra("NOTE_ID", noteId)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        var objToCreateDB = MyNotesSQLiteOpenHelper(this)
        db = objToCreateDB.readableDatabase

        //Read Data
        cursor = db!!.query("NOTES", arrayOf("_id", "title"),
            null, null, null, null, null )

        //Simple Cursor Adapter
        val listAdapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1,
            cursor,
            arrayOf("title"),
            intArrayOf(android.R.id.text1),0)

        //set adapter view
        listViewNotes.adapter = listAdapter
    }

    //Object CleanUp
    override fun onDestroy() {
        super.onDestroy()
        cursor!!.close()
        db!!.close()
    }
}

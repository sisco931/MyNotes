package com.example.mynotes

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_note_details.*

class NoteDetails : AppCompatActivity() {

    private var db: SQLiteDatabase? = null
    var noteID = 0
    var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)
        val myNotesDatabaseHelper = MyNotesSQLiteOpenHelper(this)
        db = myNotesDatabaseHelper.writableDatabase
        noteID = intent.extras!!.get("NOTE_ID").toString().toInt()

        //Code that reads a note title & description it's id & column is equal to the noteID
        if (noteID != 0) {
            cursor = db!!.query(
                "NOTES",
                arrayOf("TITLE", "DESCRIPTION"),
                "_id=?",
                arrayOf(noteID.toString()),
                null, null, null
            )
        }
        if (cursor?.moveToFirst() == true) {
            editTextTitle.setText(cursor!!.getString(0))
            editTextDescription.setText(cursor!!.getString(1))
        }
        else onNewIntent(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_note) {
            val newNotesValues = ContentValues()
            if (editTextTitle.text.isEmpty() == true) {
                newNotesValues.put("TITLE", "untitled")
            } else {
                newNotesValues.put("TITLE", editTextTitle.text.toString())
            }
            if (noteID == 0) {
                insertNote(newNotesValues)
            } else {
                updateNote(newNotesValues)
            }
        } else if (item!!.itemId == R.id.delete_note) {
            deleteNote()
        }

        return super.onOptionsItemSelected(item)
    }

    fun deleteNote() {
        var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)

        //set a title for alert dialog
        builder.setTitle("Deleting Note")
        //set message for alert dialog
        builder.setMessage("Are You Sure You Want to Delete '${editTextTitle.text}'?")
        //set an alert dialog positive (yes) button
        builder.setPositiveButton("YES", dialogClickListener)
        //set alert dialog neutral (cancel) button
        builder.setNegativeButton("CANCEL", dialogClickListener)
        //initialize the alertDialog by using the builder object
        dialog = builder.create()
        //display the alert dialog
        dialog.show()
    }

    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        if (which == DialogInterface.BUTTON_POSITIVE) {
            db!!.delete("NOTES", "_id=?", arrayOf(noteID.toString()))
            Toast.makeText(this, "Note Deleted!!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun updateNote(notesValues: ContentValues) {
        db!!.update("NOTES", notesValues, "_id=?", arrayOf(noteID.toString()))
        Toast.makeText(this, "Note Updated!!", Toast.LENGTH_LONG).show()
    }

    private fun insertNote(newNotesValues: ContentValues) {
        //Inserting a new note

        newNotesValues.put("DESCRIPTION", editTextDescription.text.toString())

        db!!.insert("NOTES", null, newNotesValues)

        Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()

        //Empty edit text
        editTextTitle.setText("")
        editTextDescription.setText("")

        //Shift focus to editTextTitle
        editTextTitle.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        db!!.close()
    }
}
package com.practice.project1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tododiffutils.R

class MainActivity : AppCompatActivity(), AdapterClickEvents {

    private lateinit var addText: EditText
    private lateinit var addButton: Button
    private lateinit var toDoList: RecyclerView

    private var list = ArrayList<String>()

    private var saveDataFile = SaveDataFile()
    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addText = findViewById(R.id.addText)
        addButton = findViewById(R.id.addButton)
        toDoList = findViewById(R.id.toDoList)

        toDoList.layoutManager = LinearLayoutManager(this)

        list = saveDataFile.read(this)

        customAdapter = CustomAdapter(list, this, this)
        toDoList.adapter = customAdapter
        customAdapter.setMediaList(list)

        addButton.setOnClickListener {
            addItem()
        }

//        addText.setOnEditorActionListener { _, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE || (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
//                addItem()
//                return@setOnEditorActionListener true
//            }
//            return@setOnEditorActionListener false
//        }
    }

    private fun addItem() {
        val newItem: String = addText.text.toString()

        if (newItem.isNotEmpty()) {
            list.add(newItem)
            addText.setText("")
            saveDataFile.write(list, applicationContext)
            customAdapter.setMediaList(list)
        }
    }

    override fun deleteClickCallback(position: Int) {
        showAlertDialogDelete(list[position], position)
    }

    override fun editClickCallback(position: Int) {
        showAlertDialogEdit(list[position], position)
    }

    private fun showAlertDialogDelete(name: String, position: Int) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Delete")
        alert.setMessage("Do you want to delete $name ?")
        alert.setCancelable(false)
        alert.setNegativeButton(
            "No"
        ) { dialog, i ->
            dialog.cancel()
        }
        alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
            list.removeAt(position)
            saveDataFile.write(list, applicationContext)
            customAdapter.setMediaList(list)
            customAdapter.notifyItemRangeChanged(position, list.size - position)
        })
        alert.create().show()
    }

    private fun showAlertDialogEdit(name: String, position: Int) {

        val inputText = EditText(this)
        inputText.setText(name)

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Edit")
        alert.setView(inputText)
        alert.setCancelable(false)
        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, i ->
                dialog.cancel()
            })
        alert.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, i ->
            val newInputText = inputText.text.toString()
            if (newInputText.isNotEmpty()) {
                list[position] = newInputText
                saveDataFile.write(list, applicationContext)
                customAdapter.setMediaList(list)
            }
        })
        alert.create().show()
    }
}

interface AdapterClickEvents {
    fun deleteClickCallback(position: Int)
    fun editClickCallback(position: Int)
}
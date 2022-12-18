package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PersonRecyclerAdapter.ContentListener {
    private lateinit var recyclerAdapter: PersonRecyclerAdapter
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.personList)
        db.collection("persons")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<Person> = ArrayList()
                for(document in it.documents){
                    val person = document.toObject(Person::class.java)
                    if (person != null){
                        person.id = document.id
                        list.add(person)
                    }
                }
                recyclerAdapter = PersonRecyclerAdapter(list, this@MainActivity)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener{
                Log.e("MainActivity", it.message.toString())
            }
        val saveButton = findViewById<Button>(R.id.buttonSave)
        val saveName = findViewById<EditText>(R.id.editTextTextPersonName)
        val saveDescription = findViewById<EditText>(R.id.editTextTextPersonDescription)
        val saveImageUrl = findViewById<EditText>(R.id.editTextTextPersonImageUrl)

        saveButton.setOnClickListener{
            val person = Person("", saveImageUrl.text.toString(), saveName.text.toString(), saveDescription.text.toString())
            db.collection("persons").add(person).addOnSuccessListener {
                person.id = it.id
                recyclerAdapter.saveItem(person)
            }
        }
    }


    override fun onItemButtonClick(index: Int, item: Person, clickType: ButtonClickType) {
        if (clickType == ButtonClickType.EDIT){
            db.collection("persons").document(item.id).set(item)
        }
        else if (clickType == ButtonClickType.DELETE){
            db.collection("persons").document(item.id).delete()
        }
    }
}
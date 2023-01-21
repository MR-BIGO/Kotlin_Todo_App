package com.example.finalsproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalsproject.R
import com.example.finalsproject.models.Todo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time

class TodoAddFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton: Button = view.findViewById(R.id.addFragmentAddBTN)

        addButton.setOnClickListener {
            val title: String = view.findViewById<View?>(R.id.addFragmentTitleET).toString()
            val description: String =
                view.findViewById<View?>(R.id.addFragmentDescriptionET).toString()


            database = FirebaseDatabase.getInstance().getReference("todo")

            val todo = Todo(title, description, false)

            database.child("title").setValue(todo).addOnSuccessListener {
                clear()

                Toast.makeText(view.context, "Successfully saved new task", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(view.context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_add, container, false)
    }

    private fun clear() {
        view?.findViewById<EditText>(R.id.addFragmentTitleET)?.text?.clear()
        view?.findViewById<EditText>(R.id.addFragmentDescriptionET)?.text?.clear()
    }
}
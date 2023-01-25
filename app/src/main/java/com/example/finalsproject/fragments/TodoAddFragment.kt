package com.example.finalsproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.finalsproject.R
import com.example.finalsproject.models.Todo
import com.example.finalsproject.notification.MyNotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class TodoAddFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var todoTitleET: EditText
    private lateinit var todoDescriptionET: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var datePicker: DatePicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker = view.findViewById(R.id.addFragmentDatePicker)

        setDatePickerDate()


        val addButton: Button = view.findViewById(R.id.addFragmentAddBTN)
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        database =
            FirebaseDatabase.getInstance("https://finals-project-96657-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Todos/$uid")
        todoTitleET = view.findViewById(R.id.addFragmentTitleET)
        todoDescriptionET = view.findViewById(R.id.addFragmentDescriptionET)

        addButton.setOnClickListener {
            insertTodo()
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
        todoTitleET.text.clear()
        todoDescriptionET.text.clear()
    }

    private fun insertTodo() {

        val todoTitle: String = todoTitleET.text.toString()
        val todoDescription: String = todoDescriptionET.text.toString()
        val deadline: String = (datePicker.month + 1).toString() + ":" + datePicker.dayOfMonth + ":" + datePicker.year

        if (todoTitle.isEmpty()) {
            todoTitleET.error = "Please, Enter The Title"
            return
        }
        if (todoDescription.isEmpty()) {
            todoDescriptionET.error = "Please, Enter The Description"
            return
        }
        val todoId = database.push().key!!

        val todo = Todo(todoId, todoTitle, todoDescription, false, false, deadline)

        database.child(todoId).setValue(todo).addOnSuccessListener {
            Toast.makeText(view?.context, "Task added successfully", Toast.LENGTH_SHORT).show()
            clear()
        }.addOnFailureListener {
            Toast.makeText(view?.context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDatePickerDate(){
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        datePicker.updateDate(currentYear, currentMonth, currentDay)
    }
}
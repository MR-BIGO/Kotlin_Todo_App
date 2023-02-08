package com.example.finalsproject.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalsproject.R
import com.example.finalsproject.TodoAdapter
import com.example.finalsproject.models.Todo
import com.example.finalsproject.notification.MyNotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class TodoListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoList: ArrayList<Todo>
    private lateinit var database: DatabaseReference
    private lateinit var dayModeBtn: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var service: MyNotificationService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = MyNotificationService(view.context)

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        database =
            FirebaseDatabase.getInstance("https://finals-project-96657-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Todos/$uid")

        recyclerView = view.findViewById(R.id.listFragmentRV)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        todoList = arrayListOf()
        getTodos()

        dayModeBtn = view.findViewById(R.id.listFragmentDayModeBTN)

        sharedPreferences = view.context.getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val nightMode: Boolean = sharedPreferences.getBoolean("night", false)

        dayModeBtn.setOnClickListener {

            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPreferences.edit()
                editor.putBoolean("night", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPreferences.edit()
                editor.putBoolean("night", true)
            }
            editor.apply()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    private fun getTodos() {

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList.clear()
                if (snapshot.exists()) {
                    for (todoSnap in snapshot.children) {
                        val todoData = todoSnap.getValue(Todo::class.java)
                        todoList.add(todoData!!)
                        isFailedTodo(todoData)
                    }
                    val todoAdapter = TodoAdapter(todoList)

                    recyclerView.adapter = todoAdapter

                    todoAdapter.setOnItemClickListener(object : TodoAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            openUpdateDialog(todoList[position])
                        }

                        override fun onButtonClick(position: Int) {
                            if (!todoList[position].isFailed!!) {
                                if (todoList[position].isDone!!) {
                                    isDoneTodo(
                                        todoList[position].id!!,
                                        false
                                    )
                                } else {
                                    isDoneTodo(
                                        todoList[position].id!!,
                                        true
                                    )
                                    service.showNotification()
                                }
                            }
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun openUpdateDialog(todo: Todo) {
        val dialog = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.todo_update_dialog, null)

        dialog.setView(dialogView)

        val updateTitleET = dialogView.findViewById<EditText>(R.id.todoUpdateTitleET)
        val updateDescriptionET = dialogView.findViewById<EditText>(R.id.todoUpdateDescriptionET)
        val updateBTN = dialogView.findViewById<Button>(R.id.todoUpdateUpdateBTN)
        val deleteBTN = dialogView.findViewById<Button>(R.id.todoUpdateDeleteBTN)

        updateTitleET!!.setText(todo.title)
        updateDescriptionET!!.setText(todo.description)

        dialog.setTitle("Updating Task")

        val alertDialog = dialog.create()

        alertDialog.show()

        updateBTN?.setOnClickListener {

            updateTodo(
                todo.id!!,
                updateTitleET.text.toString(),
                updateDescriptionET.text.toString(),
                todo.isDone!!,
                todo.isFailed!!,
                todo.deadline!!
            )
            alertDialog.dismiss()
        }
        deleteBTN?.setOnClickListener {
            val builder = AlertDialog.Builder(view?.context)
            builder.setTitle("Are You Sure You Want To Delete This Task?")
            builder.setPositiveButton("Yes") { dialog, which ->
                deleteTodo(todo.id!!)
            }
            builder.setNegativeButton("No") { dialog, which ->
                alertDialog.dismiss()
            }
            builder.show()
            alertDialog.dismiss()
        }
    }

    private fun updateTodo(
        id: String,
        title: String,
        description: String,
        isDone: Boolean,
        isFailed: Boolean,
        deadline: String,
    ) {
        val updatedTodo = Todo(id, title, description, isDone, isFailed, deadline)
        database.child(id).setValue(updatedTodo)
        Toast.makeText(view?.context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTodo(id: String) {
        database.child(id).removeValue()
    }

    private fun isDoneTodo(id: String, isDone: Boolean) {
        database.child(id).child("done").setValue(isDone)
    }

    private fun isFailedTodo(todo: Todo) {
        var deadline = todo.deadline
        val month: Int = deadline!!.substringBefore(":").toInt()
        deadline = deadline.substringAfter(":")
        val day: Int = deadline.substringBefore(":").toInt()
        val year: Int = deadline.substringAfter(":").toInt()

        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

        if (currentYear > year) {
            database.child(todo.id!!).child("failed").setValue(true)
        } else if (currentYear == year && currentMonth > month) {
            database.child(todo.id!!).child("failed").setValue(true)
        } else if (currentYear == year && currentMonth == month && currentDay > day) {
            database.child(todo.id!!).child("failed").setValue(true)
        }

    }
}
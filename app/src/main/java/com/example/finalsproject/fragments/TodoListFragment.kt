package com.example.finalsproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalsproject.R
import com.example.finalsproject.TodoAdapter
import com.example.finalsproject.models.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TodoListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoList: ArrayList<Todo>
    private lateinit var database: DatabaseReference
    private lateinit var refreshBtn: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        database =
            FirebaseDatabase.getInstance("https://finals-project-96657-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Todos/$uid")

        recyclerView = view.findViewById(R.id.listFragmentRV)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        todoList = arrayListOf()
        getTodos()

        refreshBtn = view.findViewById(R.id.listFragmentReloadBTN)
        refreshBtn.setOnClickListener {
            getTodos()
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
                    }
                    val todoAdapter = TodoAdapter(todoList)
                    recyclerView.adapter = todoAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
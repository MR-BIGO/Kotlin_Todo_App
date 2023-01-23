package com.example.finalsproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalsproject.models.Todo

class TodoAdapter(private val todoList: ArrayList<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        var titleView: TextView = itemView.findViewById(R.id.taskTitleTV)
        var descriptionView: TextView = itemView.findViewById(R.id.taskDescriptionTV)
        var isDoneBtn: ImageButton = itemView.findViewById(R.id.taskIsDoneBTN)
//        var deadlineView: TextView = itemView.findViewById(R.id.taskDeadlineTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_task_layout, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todoList[position]
        holder.titleView.text = currentTodo.title
        holder.descriptionView.text = currentTodo.description
        if (currentTodo.isFailed == true){
            holder.isDoneBtn.setBackgroundResource(R.drawable.custom_check_button_failed)
        }else if (currentTodo.isDone == true){
            holder.isDoneBtn.setBackgroundResource(R.drawable.custom_check_button_done)
        }else{
            holder.isDoneBtn.setBackgroundResource(R.drawable.custom_check_button_idle)
        }
        holder.itemView.setOnClickListener {

        }
//        holder.itemView.setOnLongClickListener {
//
//        }
//        holder.deadlineView.text = currentTodo.deadline.toString()
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}
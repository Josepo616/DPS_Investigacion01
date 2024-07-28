package sv.edu.udb.tareas.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sv.edu.udb.tareas.Task
import sv.edu.udb.tareas.TaskAdapter

class HomeViewModel : ViewModel() {

    private var tasks = mutableListOf<Task>()
    private var isTaskListEmpty = true
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getTasksToRecycler(context: Context, recycler: RecyclerView) {
        val tasksSharedPref = context.getSharedPreferences("task", Context.MODE_PRIVATE)
        val gson = Gson()
        if (tasksSharedPref.getString("tasks", "") != "") {
            val json = tasksSharedPref.getString("tasks", "")
            tasks = gson.fromJson(json, object : TypeToken<MutableList<Task>>() {}.type)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = TaskAdapter(tasks, context)
    }

    fun isTaskListEmpty(): Boolean {
        return isTaskListEmpty
    }

    fun addTask(
        context: Context,
        recycler: RecyclerView,
        taskTitle: String,
        taskDesc: String,
        taskDueDate: Long,
        taskRepeat: Int,
        taskReminderDate: Long
    ): Boolean {
        val createdDate = System.currentTimeMillis()
        val newTask = Task(
            taskTitle,
            taskDesc,
            createdDate,
            taskDueDate,
            taskRepeat,
            taskReminderDate,
            false,
            0
        )
        val gson = Gson()
        tasks.add(newTask)
        val adapter = TaskAdapter(tasks, context)
        recycler.adapter = adapter
        val tasksSharedPref = context.getSharedPreferences("task", Context.MODE_PRIVATE)
        val editor = tasksSharedPref?.edit()
        val json = gson.toJson(tasks)
        return if (editor != null) {
            editor.putString("tasks", json)
            editor.apply()
            true
        } else {
            false
        }
    }
}







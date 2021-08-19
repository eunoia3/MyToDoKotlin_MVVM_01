package com.eun.mytodokotlin_mvvm_01.view

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eun.mytodokotlin_mvvm_01.R
import com.eun.mytodokotlin_mvvm_01.data.model.TodoModel
import com.eun.mytodokotlin_mvvm_01.viewmodel.TodoViewModel
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val TAG: String = MainActivity::class.java.name;

    private lateinit var todoViewModel: TodoViewModel  //TodoViewModel 인스턴스를 만들고, 이를 관찰

    private lateinit var todoListAdapter: TodoListAdapter
    private val todoItems: ArrayList<TodoModel> = ArrayList()

    private val recyclerview_list: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerview_list)
    }

    private val btn_add: Button by lazy {
        findViewById<Button>(R.id.btn_add)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "== onCreate ");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        todoItems.run {
//            add(
//                TodoModel(
//                    null,
//                    1,
//                    "방청소 1 ",
//                    "책상정리",
//                    Date().time
//                )
//            )
//            add(
//                TodoModel(
//                    null,
//                    2,
//                    "방청소 2 ",
//                    "옷장정리",
//                    Date().time
//                )
//            )
//        }

        initViewModel()
        initRecyclerview()
        initBtnAdd()
    }


    /*
    * ViewModel 설정
    * View 에서 ViewModel 을 관찰하여 데이터가 변경될 때 내부적으로 자동으로 알 수 있도록 한다.
    * ViewModel 은 View 와 ViewModel 의 분리로 액티비티가 destroy 되었다가 다시 create 되어도 종료되지 않고 가지고 있다.
    * */
    private fun initViewModel() {
        todoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(TodoViewModel::class.java)
        todoViewModel.getTodoListAll().observe(this, androidx.lifecycle.Observer {
            todoListAdapter.setTodoItems(it)
        })
    }


    /*
   * Recyclerview 설정
   * Recyclerview adapter 와 LinearLayoutManager 를 만들고 연결
   * */
    private fun initRecyclerview() {
        todoListAdapter =
            TodoListAdapter({ todo -> deleteDialog(todo) })  //adapter 에 click 시 해야할 일을 (todo) -> Unit 파라미터로 넘겨준다
        recyclerview_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoListAdapter
        }
    }


    /*
    * btn_add 설정
    * */
    private fun initBtnAdd() {
        btn_add.setOnClickListener {
            showAddTodoDialog()
        }
    }


    /*
    * Todo 리스트를 추가하는 Dialog 띄우기
    *  TodoModel 을 생성하여 리스트 add 해서 리스트를 갱신
    * */
    private fun showAddTodoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add, null)
        val et_add_title: EditText by lazy {
            dialogView.findViewById<EditText>(R.id.et_add_title)
        }
        val et_add_content: EditText by lazy {
            dialogView.findViewById<EditText>(R.id.et_add_content)
        }
        var builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle("Todo 아이템 추가하기")
            .setView(dialogView)
            .setPositiveButton(
                "확인"
            ) { dialogInterface, i ->
                var id: Long? = null
                val title = et_add_title.text.toString()
                val content = et_add_content.text.toString()
                val createdDate = Date().time
                val todoModel = TodoModel(
                    id,
                    todoListAdapter.getItemCount() + 1,
                    title,
                    content,
                    createdDate
                )
                todoViewModel.insert(todoModel)
            }
            .setNegativeButton("취소", null)
            .create()
        dialog.show()
    }


    /*
    * Todo 리스트를 삭제하는 Dialog 띄우기
    * */
    private fun deleteDialog(todoModel: TodoModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Todo 아이템 삭제하기").setMessage(todoModel.seq.toString()+" 번 Todo 아이템을 삭제할까요? ")
            .setNegativeButton("취소") { _, _ -> }
            .setPositiveButton("확인") { _, _ ->
                todoViewModel.delete(todoModel)
            }
            .create()
        builder.show()
    }

}
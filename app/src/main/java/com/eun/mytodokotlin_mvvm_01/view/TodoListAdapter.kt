package com.eun.mytodokotlin_mvvm_01.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eun.mytodokotlin_mvvm_01.R
import com.eun.mytodokotlin_mvvm_01.data.model.TodoModel
import java.text.SimpleDateFormat
import java.util.*


class TodoListAdapter(val deletetItemClick: (TodoModel) -> Unit) :
    RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    private var todoItems: List<TodoModel> = listOf()

    /*
    * 이 어뎁터가 아이템을 얼마나 가지고 있는지 얻는 함수
    * */
    override fun getItemCount(): Int {

        Log.d("MainActivity", "todoItem getItemCount !!: " + todoItems.size);
        return todoItems.size
    }

    /*
    * 현재 아이템이 사용할 뷰홀더를 생성하여 반환하는 함수
    * item_list 레이아웃을 사용하여 뷰를 생성하고 뷰홀더에 뷰를 전달하여 생성된 뷰홀더를 반환
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        val viewHolder = TodoViewHolder(view)
        return viewHolder
    }

    /*
    * 현재 아이템의 포지션에 대한 데이터 모델을 리스트에서 얻고
    * holder 객체를 TodoViewHolder 로 형변환한 뒤 bind 메서드에 이 모델을 전달하여 데이터를 바인딩하도록 한다
    * */
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoModel = todoItems[position]
        todoModel.seq = position + 1;
        val todoViewHolder = holder as TodoViewHolder
        todoViewHolder.bind(todoModel)
    }

    /* 데이터베이스가 변경될 때마다 호출 */
    fun setTodoItems(todoItems: List<TodoModel>) {
        this.todoItems = todoItems
        Log.d("MainActivity", "todoItem setTodoItems !!: " + todoItems.size);
        notifyDataSetChanged()
    }

    /*
    * 뷰홀더는 리스트를 스크롤하는 동안 뷰를 생성하고 다시 뷰의 구성요소를 찾는 행위를 반복하면서 생기는
    * 성능저하를 방지하기 위해 미리 저장 해 놓고 빠르게 접근하기 위하여 사용하는 객체
    * */
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_seq = itemView.findViewById<TextView>(R.id.tv_seq)
        private val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        private val tv_content = itemView.findViewById<TextView>(R.id.tv_content)
        private val tv_date = itemView.findViewById<TextView>(R.id.tv_date)
        private val iv_delete = itemView.findViewById<ImageView>(R.id.iv_delete)

        fun bind(todoModel: TodoModel) {

            tv_seq.text = todoModel.seq.toString()
            tv_title.text = todoModel.title
            tv_content.text = todoModel.content
            tv_date.text = todoModel.createDate.convertDateToString("yyyy.MM.dd HH:mm")

            iv_delete.setOnClickListener {
                deletetItemClick(todoModel)
            }

        }

    }

}


/* createDate 을 Date to String  */
fun Long.convertDateToString(format: String): String {
    val simpleDateFormat = SimpleDateFormat(format)
    return simpleDateFormat.format(Date(this))
}

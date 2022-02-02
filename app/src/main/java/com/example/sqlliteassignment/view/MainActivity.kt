package com.example.sqlliteassignment.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlliteassignment.*
import com.example.sqlliteassignment.adapter.RecyclerStudentListAdapter
import com.example.sqlliteassignment.database.StudentDatabase
import com.example.sqlliteassignment.model.StudentModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerStudentListAdapter
    private val students: ArrayList<StudentModel> = ArrayList()
    private  var db= StudentDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setListener()
    }

   private fun init(){
       val studentData=db.getStudentData()
       students.clear()
       if (studentData?.count == 0) {
           Toast.makeText(this, getString(R.string.no_data_message), Toast.LENGTH_SHORT).show()
       }
       while (studentData!!.moveToNext()) {
           students.add(StudentModel(studentData.getInt(0),studentData.getString(1),studentData.getString(2),studentData.getLong(3),studentData.getString(4),studentData.getString(5),studentData.getString(6),studentData.getString(7),studentData.getString(8),studentData.getFloat(9),studentData.getString(10)))
       }
       adapter = RecyclerStudentListAdapter(this,students)
       rvStudentList.adapter = adapter
       rvStudentList.layoutManager = LinearLayoutManager(this)
   }

    private fun setListener() {
        fabRegisterStudent.setOnClickListener {
            val intentRegister=Intent(this, StudentRegisterFromActivity::class.java)
            intentRegister.putExtra(Constant.ADD_OR_UPDATE,getString(R.string.add))
            startActivity(intentRegister)
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}
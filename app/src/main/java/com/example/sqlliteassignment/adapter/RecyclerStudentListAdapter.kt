package com.example.sqlliteassignment.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlliteassignment.R
import com.example.sqlliteassignment.database.StudentDatabase
import com.example.sqlliteassignment.model.StudentModel
import com.example.sqlliteassignment.view.StudentRegisterFromActivity

class RecyclerStudentListAdapter(private var ctx:Context, private var studentList:ArrayList<StudentModel>): RecyclerView.Adapter<RecyclerStudentListAdapter.StudentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListViewHolder {
        val recyclerInflater = LayoutInflater.from(ctx)
        val recyclerView = recyclerInflater.inflate(R.layout.item_student_list, parent, false)
        return  StudentListViewHolder(recyclerView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StudentListViewHolder, position: Int) {
        val student=studentList[position]
        holder.tvStudentName.text=student.studentName
        holder.tvCourse.text = ctx.getString(R.string.course_name)+student.course
        holder.tvContactNumber.text= student.contactNumber.toString()
        holder.tvGender.text=ctx.getString(R.string.gender)+student.gender
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    inner class StudentListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        var tvCourse:TextView = itemView.findViewById(R.id.tvCourseName)
        var tvContactNumber: TextView = itemView.findViewById(R.id.tvContactNumber)
        var tvGender: TextView = itemView.findViewById(R.id.tvGender)
        private var btnUpdate:Button=itemView.findViewById(R.id.btnUpdate)
        private var btnDelete:Button=itemView.findViewById(R.id.btnDelete)
        init {
            btnUpdate.setOnClickListener {
                val position=adapterPosition
                val studentDetails=studentList[position]
                val intentUpdate=Intent(ctx, StudentRegisterFromActivity::class.java)
                intentUpdate.putExtra("StudentId",studentDetails.studentId)
                intentUpdate.putExtra("AddOrUpdate","Update")
                ctx.startActivity(intentUpdate)
            }
            btnDelete.setOnClickListener {
                val position = adapterPosition
                val student = studentList[position]
                val id =student.studentId
                val builder =
                    AlertDialog.Builder(ctx, android.R.style.Theme_Material_Light_Dialog_Alert)
                builder.setTitle(ctx.getString(R.string.delete))
                builder.setMessage("Do you Want  Delete Student Details?")
                builder.setPositiveButton("yes") { _: DialogInterface?, _: Int ->
                    val deleteStudent = StudentDatabase(ctx)
                    val delete = deleteStudent.deleteStudent(id)
                    if (delete) {
                        studentList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show()

            }
        }
    }
}
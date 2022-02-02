package com.example.sqlliteassignment.database


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlliteassignment.Constant
import com.example.sqlliteassignment.model.StudentModel

class StudentDatabase(context: Context?) : SQLiteOpenHelper(context,"StudentRegistrationCollage.db",null,3) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create Table ${Constant.TABLE_NAME}(studentId INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,email TEXT,contactNumber NUMBER,DOB DATE,gender TEXT,Address TEXT,course TEXT,HSCPassingYear YEAR,HSCPercentage NUMBER,hobbies TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(studentData:StudentModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.STUDENT_NAME,studentData.studentName)
        contentValues.put(Constant.STUDENT_EMAIL,studentData.email)
        contentValues.put(Constant.CONTACT_NUMBER,studentData.contactNumber)
        contentValues.put(Constant.STUDENT_DOB,studentData.dob)
        contentValues.put(Constant.STUDENT_GENDER,studentData.gender)
        contentValues.put(Constant.STUDENT_ADDRESS,studentData.address)
        contentValues.put(Constant.STUDENT_COURSE,studentData.course)
        contentValues.put(Constant.STUDENT_PASSING_YEAR,studentData.HSCPassingYear)
        contentValues.put(Constant.STUDENT_PERCENTAGE,studentData.HSCPercentage)
        contentValues.put(Constant.STUDENT_HOBBIES,studentData.hobbies)
        val result = db.insert(Constant.TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun updateStudentData(studentId: Int,studentData:StudentModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.STUDENT_NAME,studentData.studentName)
        contentValues.put(Constant.STUDENT_EMAIL,studentData.email)
        contentValues.put(Constant.CONTACT_NUMBER,studentData.contactNumber)
        contentValues.put(Constant.STUDENT_DOB,studentData.dob)
        contentValues.put(Constant.STUDENT_GENDER,studentData.gender)
        contentValues.put(Constant.STUDENT_ADDRESS,studentData.address)
        contentValues.put(Constant.STUDENT_COURSE,studentData.course)
        contentValues.put(Constant.STUDENT_PASSING_YEAR,studentData.HSCPassingYear)
        contentValues.put(Constant.STUDENT_PERCENTAGE,studentData.HSCPercentage)
        contentValues.put(Constant.STUDENT_HOBBIES,studentData.hobbies)
        val result = db.update(Constant.TABLE_NAME,contentValues,Constant.STUDENT_ID +"=?", arrayOf(studentId.toString()))
        return result==1
    }


    fun getStudentData(): Cursor? {
        val db = this.writableDatabase
        return  db.rawQuery("Select * from Student",null)
    }
    fun getStudentDataById(studentId:Int):Cursor{
        val db = this.writableDatabase
        return  db.rawQuery("Select * from Student Where studentID=?", arrayOf(studentId.toString()))
    }

    fun deleteStudent(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(Constant.TABLE_NAME, Constant.STUDENT_ID+"=?", arrayOf(id.toString())) > 0
    }
}
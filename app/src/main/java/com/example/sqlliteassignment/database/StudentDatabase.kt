package com.example.sqlliteassignment.database


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabase(context: Context?) : SQLiteOpenHelper(context,"StudentRegistrationNewOne.db",null,2) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create Table Student(studentId INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,email TEXT,contactNumber NUMBER,DOB DATE,gender TEXT,Address TEXT,course TEXT,HSCPassingYear YEAR,HSCPercentage NUMBER,hobbies TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(studentName: String?,email:String?,contactNumber:Long,dob:String?,gender:String?,address:String?,course:String,HSCPassingYear:String,HSCPercentage:Float?,hobbies:String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name",studentName)
        contentValues.put("email", email)
        contentValues.put("contactNumber",contactNumber)
        contentValues.put("DOB",dob)
        contentValues.put("gender",gender)
        contentValues.put("Address",address)
        contentValues.put("course",course)
        contentValues.put("HSCPassingYear",HSCPassingYear)
        contentValues.put("HSCPercentage",HSCPercentage)
        contentValues.put("hobbies",hobbies)
        val result = db.insert("Student", null, contentValues)
        return result != -1L
    }

    fun updateStudentData(studentId: Int,studentName: String?,email:String?,contactNumber:Long,dob:String?,gender:String?,address:String?,course:String,HSCPassingYear:String,HSCPercentage:Float?,hobbies: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name",studentName)
        contentValues.put("email", email)
        contentValues.put("contactNumber",contactNumber)
        contentValues.put("DOB",dob)
        contentValues.put("gender",gender)
        contentValues.put("Address",address)
        contentValues.put("course",course)
        contentValues.put("HSCPassingYear",HSCPassingYear)
        contentValues.put("HSCPercentage",HSCPercentage)
        contentValues.put("hobbies",hobbies)
        val result = db.update("Student",contentValues,"studentId=?", arrayOf(studentId.toString()))
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
        return db.delete("Student", "studentId" + "=?", arrayOf(id.toString())) > 0
    }
}
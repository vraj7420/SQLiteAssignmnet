package com.example.sqlliteassignment.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlliteassignment.Constant
import com.example.sqlliteassignment.R
import com.example.sqlliteassignment.database.StudentDatabase
import com.example.sqlliteassignment.model.StudentModel
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.activity_student_details_from.*
import java.text.SimpleDateFormat
import java.util.*


class StudentRegisterFromActivity : AppCompatActivity() {
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var userGender: String = "male"
    private var hobbies: String = ""
    private var calendar = Calendar.getInstance()
    private var birthDate: OnDateSetListener? = null
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.UK)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details_from)
        init()
    }

    private fun init() {
        val courseSelectionAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.courses)
        )

        birthDate = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tetDateOfBirth.setText(sdf.format(calendar.time))
        }
        tetDateOfBirth.setOnClickListener {
            DatePickerDialog(
                this,
                birthDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        tetPassingYear.setOnClickListener {
            val year = MonthPickerDialog.Builder(this, { _, selectedYear ->
                tetPassingYear.setText(selectedYear.toString())
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
            year.setActivatedYear(calendar.get(Calendar.YEAR))
                .setMinYear(2000)
                .setMaxYear(calendar.get(Calendar.YEAR))
                .showYearOnly()
                .setTitle("Select Year")
                .build().show()
        }

        spinnerCourseSelection.adapter = courseSelectionAdapter
        val addOrUpdate = intent.getStringExtra(Constant.ADD_OR_UPDATE)
        if (addOrUpdate.equals(getString(R.string.update))) {
            btnAddOrUpdate.text = addOrUpdate
            setListenerForUpdate()
        }
        if (addOrUpdate.equals(getString(R.string.add))) {
            btnAddOrUpdate.text = addOrUpdate
            setListenerForAdd()
        }
    }

    private fun setListenerForAdd() {
        btnAddOrUpdate.setOnClickListener {
            if (validateStudentData()) {
                val studentDatabase = StudentDatabase(this)
                val insertData = studentDatabase.insertData(getData())
                if (insertData) {
                    Toast.makeText(this, getString(R.string.inset_data), Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }
        }
    }

    private fun setListenerForUpdate() {
        setData()
        btnAddOrUpdate.setOnClickListener {
            if (validateStudentData()) {
                val studentDatabase = StudentDatabase(this)
                val updateData = studentDatabase.updateStudentData(
                    intent.getIntExtra(Constant.STUDENT_ID, 0), getData()
                )
                if (updateData) {
                    Toast.makeText(this, getString(R.string.update_data), Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

        }
    }

    private fun getData(): StudentModel {
        return StudentModel(
            1,
            tetStudentName.text.toString().trim(),
            tetEmailAddress.text.toString().trim(),
            tetMobileNumber.text.toString().trim().toLong(),
            tetDateOfBirth.text.toString(),
            userGender,
            tetAddress.text.toString().trim(),
            spinnerCourseSelection.selectedItem.toString(),
            tetPassingYear.text.toString().trim(),
            tetHscPercentage.text.toString().trim().toFloat(),
            hobbies
        )
    }

    private fun validateStudentData(): Boolean {
        when {
            (tetStudentName.text.toString().trim().isEmpty()) -> {
                tetStudentName.error = getString(R.string.error_full_name)
                tetStudentName.requestFocus()
                return false
            }
            (tetEmailAddress.text.toString().trim().isEmpty()) -> {
                tetEmailAddress.error = getString(R.string.error_email_is_empty)
                tetEmailAddress.requestFocus()
                return false
            }
            (!tetEmailAddress.text.toString().trim().matches(emailPattern.toRegex())) -> {
                tetEmailAddress.error = getString(R.string.error_email_valid)
                tetEmailAddress.requestFocus()
                return false
            }
            (tetMobileNumber.text.toString().trim().isEmpty()) -> {
                tetMobileNumber.error = getString(R.string.error_phone_number_is_empty)
                tetMobileNumber.requestFocus()
                return false
            }
            (tetMobileNumber.text.toString().length != 10) -> {
                tetMobileNumber.error = getString(R.string.error_phone_number_valid)
                tetMobileNumber.requestFocus()
                return false
            }
            (tetDateOfBirth.text.toString().trim().isEmpty()) -> {
                tetDateOfBirth.error = getString(R.string.error_birth_is_empty)
                tetDateOfBirth.requestFocus()
                return false
            }
            (tetAddress.text.toString().trim().isEmpty()) -> {
                tetAddress.error = getString(R.string.error_address)
                tetAddress.requestFocus()
                return false
            }
            (tetPassingYear.text.toString().isEmpty()) -> {
                tetPassingYear.error = getString(R.string.error_passing_year)
                tetPassingYear.requestFocus()
                return false
            }
            (tetHscPercentage.text.toString().trim().isEmpty()) -> {
                tetHscPercentage.error = getString(R.string.error_percentage)
                tetHscPercentage.requestFocus()
                return false
            }
            (tetHscPercentage.text.toString().toFloat() > 100.00) -> {
                tetHscPercentage.error = getString(R.string.error_not_valid_percentage)
                tetHscPercentage.requestFocus()
                return false
            }
            else -> {
                userGender =
                    findViewById<RadioButton>(rgGender.checkedRadioButtonId).text.toString().trim()
                if (cbSport.isChecked) {
                    hobbies += cbSport.text.toString()
                }
                if (cbTraveling.isChecked) {
                    hobbies += cbTraveling.text.toString()
                }
                if (cbCulturalActivities.isChecked) {
                    hobbies += cbCulturalActivities.text.toString()
                }
            }
        }
        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun setData() {
        val db = StudentDatabase(this)
        val studentInformation = db.getStudentDataById(intent.getIntExtra(Constant.STUDENT_ID, 0))
        if (studentInformation.count == 0) {
            Toast.makeText(this, getString(R.string.no_data_message), Toast.LENGTH_SHORT).show()
        }
        while (studentInformation.moveToNext()) {
            val studentDataForUpdate = StudentModel(
                studentInformation.getInt(0),
                studentInformation.getString(1),
                studentInformation.getString(2),
                studentInformation.getLong(3),
                studentInformation.getString(4),
                studentInformation.getString(5),
                studentInformation.getString(6),
                studentInformation.getString(7),
                studentInformation.getString(8),
                studentInformation.getFloat(9),
                studentInformation.getString(10)
            )

            tetStudentName.setText(studentDataForUpdate.studentName)
            tetEmailAddress.setText(studentDataForUpdate.email)
            tetMobileNumber.setText(studentDataForUpdate.contactNumber.toString())
            tetDateOfBirth.setText(studentDataForUpdate.dob)
            tetAddress.setText(studentDataForUpdate.address)
            tetHscPercentage.setText(studentDataForUpdate.HSCPercentage.toString())
            tetPassingYear.setText(studentDataForUpdate.HSCPassingYear)
            when (studentDataForUpdate.gender) {
                (getString(R.string.male)) -> {
                    rbMale.isChecked = true
                }
                (getString(R.string.female)) -> {
                    rbFemale.isChecked = true

                }
                (getString(R.string.other)) -> {
                    rbOther.isChecked = true

                }
            }
            val indexOfCourse: Int =
                resources.getStringArray(R.array.courses).indexOf(studentDataForUpdate.course)
            spinnerCourseSelection.setSelection(indexOfCourse)
            if (studentDataForUpdate.hobbies.contains(getString(R.string.sport))) {
                cbSport.isChecked = true
            }
            if (studentDataForUpdate.hobbies.contains(getString(R.string.traveling))) {
                cbTraveling.isChecked = true
            }
            if (studentDataForUpdate.hobbies.contains(getString(R.string.cultural_activities))) {
                cbCulturalActivities.isChecked = true
            }
            val updateBirthDate:Date= sdf.parse(studentDataForUpdate.dob)!!
            calendar.time=updateBirthDate
            birthDate = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                tetDateOfBirth.setText(sdf.format(calendar.time))
            }
            tetDateOfBirth.setOnClickListener {
                DatePickerDialog(
                    this,
                    birthDate,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            tetPassingYear.setOnClickListener {
                val yearFormat=SimpleDateFormat("yyyy")
                val yearUpdate= yearFormat.parse(studentDataForUpdate.HSCPassingYear)!!
                calendar.time=yearUpdate
                val year = MonthPickerDialog.Builder(this, { _, selectedYear ->
                    tetPassingYear.setText(selectedYear.toString())
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
                year.setActivatedYear(calendar.get(Calendar.YEAR))
                    .setMinYear(2000)
                    .setMaxYear(2022)
                    .showYearOnly()
                    .setTitle("Select Year")
                    .build().show()
            }

        }

    }

}
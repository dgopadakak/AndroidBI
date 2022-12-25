package com.example.androidbi

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.time.LocalTime
import java.util.*

class EditTaskActivity : AppCompatActivity()
{
    private lateinit var editName: EditText
    private lateinit var editHint: EditText
    private lateinit var editNumber: EditText
    private lateinit var editNumOfParticipants: EditText
    private lateinit var editTimeForSolve: EditText
    private lateinit var editMaxScore: EditText
    private lateinit var editIsComplicated: EditText
    private lateinit var editCondition: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        editName = findViewById(R.id.editTextExamName)
        editHint = findViewById(R.id.editTextTeacherName)
        editNumber = findViewById(R.id.editTextAuditory)
        editNumOfParticipants = findViewById(R.id.editTextDate)
        editTimeForSolve = findViewById(R.id.editTextTime)
        editMaxScore = findViewById(R.id.editTextPeople)
        editIsComplicated = findViewById(R.id.editTextAbstract)
        editCondition = findViewById(R.id.editTextComment)

        val action = intent.getSerializableExtra("action") as Int

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == 2)
        {
            editName.setText(intent.getSerializableExtra("nameOfTask") as String)
            editHint.setText(intent.getSerializableExtra("hint") as String)
            editNumber.setText(intent.getSerializableExtra("number") as String)
            editNumOfParticipants.setText(intent.getSerializableExtra("numOfParticipants") as String)
            editTimeForSolve.setText(intent.getSerializableExtra("timeForSolve") as String)
            editMaxScore.setText(intent.getSerializableExtra("maxScore") as String)
            if (intent.getSerializableExtra("isComplicated") as String == "1")
            {
                editIsComplicated.setText("да")
            }
            else
            {
                editIsComplicated.setText("нет")
            }
            editCondition.setText(intent.getSerializableExtra("condition") as String)
        }
    }

    private fun confirmChanges(action: Int)
    {
        if (editName.text.toString() != "" && editHint.text.toString() != ""
            && editNumber.text.toString() != "" && editNumOfParticipants.text.toString() != ""
            && editTimeForSolve.text.toString() != "" && editMaxScore.text.toString() != ""
            && editIsComplicated.text.toString() != "")
        {
            if (editIsComplicated.text.toString().trim().lowercase(Locale.ROOT) == "да"
                || editIsComplicated.text.toString().trim().lowercase(Locale.ROOT) == "нет")
            {
                if (isTimeValid(editTimeForSolve.text.toString().trim()))
                {
                    val intent = Intent(this@EditTaskActivity,
                        MainActivity::class.java)
                    intent.putExtra("action",    action)
                    intent.putExtra("nameOfTask",      editName.text.toString().trim())
                    intent.putExtra("hint",   editHint.text.toString().trim())
                    intent.putExtra("number",    editNumber.text.toString().trim().toInt())
                    intent.putExtra("numOfParticipants",  editNumOfParticipants.text.toString().trim())
                    intent.putExtra("timeForSolve", editTimeForSolve.text.toString().trim())
                    intent.putExtra("maxScore",   editMaxScore.text.toString().trim().toInt())
                    if (editIsComplicated.text.toString().trim().lowercase(Locale.ROOT) == "да")
                    {
                        intent.putExtra("isComplicated", 1)
                    }
                    else
                    {
                        intent.putExtra("isComplicated", 0)
                    }
                    intent.putExtra("condition", editCondition.text.toString().trim())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else
                {
                    Snackbar.make(findViewById(R.id.button_confirm),
                        "Проверьте время!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show()
                }
            }
            else
            {
                Snackbar.make(findViewById(R.id.button_confirm),
                    "Поле \"Усложненное:\" поддерживает только " +
                            "значения \"да\" или \"нет\"!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.button_confirm),
                "Заполните обязательные поля!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .show()
        }
    }

    private fun isTimeValid(date: String?): Boolean
    {
        return try
        {
            LocalTime.parse(date)
            true
        }
        catch (e: java.lang.Exception)
        {
            false
        }
    }
}
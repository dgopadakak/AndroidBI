package com.example.androidbi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TaskDetailsDialogFragment: android.app.DialogFragment()
{
    private val exceptionTag = "PharmacyDetailsDialogFragment"

    interface OnInputListenerSortId
    {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewNameTitle: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewHintTitle: TextView
    private lateinit var textViewHint: TextView
    private lateinit var textViewNumTitle: TextView
    private lateinit var textViewNum: TextView
    private lateinit var textViewNumOfParticipantsTitle: TextView
    private lateinit var textViewNumOfParticipants: TextView
    private lateinit var textViewTimeForSolveTitle: TextView
    private lateinit var textViewTimeForSolve: TextView
    private lateinit var textViewMaxScoreTitle: TextView
    private lateinit var textViewMaxScore: TextView
    private lateinit var textViewIsComplicatedTitle: TextView
    private lateinit var textViewIsComplicated: TextView
    private lateinit var textViewConditionTitle: TextView
    private lateinit var textViewCondition: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button
    private lateinit var textViewCurrSort: TextView

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view: View = inflater!!.inflate(R.layout.task_details, container, false)
        textViewNameTitle = view.findViewById(R.id.textViewExamNameTitle)
        textViewName = view.findViewById(R.id.textViewExamName)
        textViewHintTitle = view.findViewById(R.id.textViewTeacherNameTitle)
        textViewHint = view.findViewById(R.id.textViewTeacherName)
        textViewNumTitle = view.findViewById(R.id.textViewAuditoryTitle)
        textViewNum = view.findViewById(R.id.textViewAuditory)
        textViewNumOfParticipantsTitle = view.findViewById(R.id.textViewDateTitle)
        textViewNumOfParticipants = view.findViewById(R.id.textViewDate)
        textViewTimeForSolveTitle = view.findViewById(R.id.textViewTimeTitle)
        textViewTimeForSolve = view.findViewById(R.id.textViewTime)
        textViewMaxScoreTitle = view.findViewById(R.id.textViewPeopleTitle)
        textViewMaxScore = view.findViewById(R.id.textViewPeople)
        textViewIsComplicatedTitle = view.findViewById(R.id.textViewAbstractTitle)
        textViewIsComplicated = view.findViewById(R.id.textViewAbstract)
        textViewConditionTitle = view.findViewById(R.id.textViewCommentTitle)
        textViewCondition = view.findViewById(R.id.textViewComment)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonOk = view.findViewById(R.id.button_details_ok)
        textViewCurrSort = view.findViewById(R.id.textViewCurrentSort)

        textViewNameTitle.setOnLongClickListener { setSortId(0) }
        textViewName.setOnLongClickListener { setSortId(0) }
        textViewHintTitle.setOnLongClickListener { setSortId(1) }
        textViewHint.setOnLongClickListener { setSortId(1) }
        textViewNumTitle.setOnLongClickListener { setSortId(2) }
        textViewNum.setOnLongClickListener { setSortId(2) }
        textViewNumOfParticipantsTitle.setOnLongClickListener { setSortId(3) }
        textViewNumOfParticipants.setOnLongClickListener { setSortId(3) }
        textViewTimeForSolveTitle.setOnLongClickListener { setSortId(4) }
        textViewTimeForSolve.setOnLongClickListener { setSortId(4) }
        textViewMaxScoreTitle.setOnLongClickListener { setSortId(5) }
        textViewMaxScore.setOnLongClickListener { setSortId(5) }
        textViewIsComplicatedTitle.setOnLongClickListener { setSortId(6) }
        textViewIsComplicated.setOnLongClickListener { setSortId(6) }
        textViewConditionTitle.setOnLongClickListener { setSortId(7) }
        textViewCondition.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle = getArguments()
        textViewName.text = arguments.getString("nameOfTask")
        textViewHint.text = arguments.getString("hint")
        textViewNum.text = arguments.getString("number")
        textViewNumOfParticipants.text = arguments.getString("numOfParticipants")
        textViewTimeForSolve.text = arguments.getString("timeForSolve")
        textViewMaxScore.text = arguments.getString("maxScore")
        if (arguments.getString("isComplicated") == "1")
        {
            textViewIsComplicated.text = "да"
        }
        else
        {
            textViewIsComplicated.text = "нет"
        }
        textViewCondition.text = arguments.getString("condition")
        if (arguments.getString("connection") != "1")
        {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

        return view
    }

    override fun onAttach(activity: Activity?)
    {
        super.onAttach(activity)
        try {
            onInputListenerSortId = getActivity() as OnInputListenerSortId
        }
        catch (e: ClassCastException)
        {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean
    {
        currentIdForSort = id
        if (currentIdForSort == 0)
        {
            textViewCurrSort.text = "Сортировка по названию"
        }
        else if (currentIdForSort == 1)
        {
            textViewCurrSort.text = "Сортировка по подсказке"
        }
        else if (currentIdForSort == 2)
        {
            textViewCurrSort.text = "Сортировка по номеру"
        }
        else if (currentIdForSort == 3)
        {
            textViewCurrSort.text = "Сортировка по количеству участников"
        }
        else if (currentIdForSort == 4)
        {
            textViewCurrSort.text = "Сортировка по времени не решение"
        }
        else if (currentIdForSort == 5)
        {
            textViewCurrSort.text = "Сортировка по максимальным баллам"
        }
        else if (currentIdForSort == 6)
        {
            textViewCurrSort.text = "Сортировка по усложненности"
        }
        else if (currentIdForSort == 7)
        {
            textViewCurrSort.text = "Сортировка по условию"
        }
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200
            , VibrationEffect.DEFAULT_AMPLITUDE))
        return true
    }

    private fun returnIdForSort()
    {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }

    private fun returnDel()
    {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit()
    {
        currentIdForSort = 9
        returnIdForSort()
    }
}
package com.example.androidbi.olympiad

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androidbi.olympiad.dbWithRoom.RoundOperatorConverter
import java.util.*
import kotlin.collections.ArrayList

@Entity
class RoundOperator()
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(RoundOperatorConverter::class)
    private var rounds: ArrayList<Round> = ArrayList()

    fun getRounds(): ArrayList<Round>
    {
        return rounds
    }

    fun setRounds(newRounds: ArrayList<Round>)
    {
        rounds = newRounds
    }

    fun setId(id: Int)
    {
        this.id = id
    }

    fun getId(): Int
    {
        return id
    }

    fun getTasksNames(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in rounds[indexGroup].listOfTasks)
        {
            arrayListForReturn.add(i.nameOfTask)
        }
        return arrayListForReturn
    }

    fun getTaskNumbers(indexGroup: Int): ArrayList<Int>
    {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in rounds[indexGroup].listOfTasks)
        {
            arrayListForReturn.add(i.num)
        }
        return arrayListForReturn
    }

    fun getTaskMaxScore(indexGroup: Int): ArrayList<Int>
    {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in rounds[indexGroup].listOfTasks)
        {
            arrayListForReturn.add(i.maxScore)
        }
        return arrayListForReturn
    }

    fun getTask(indexGroup: Int, indexExam: Int): Task
    {
        return rounds[indexGroup].listOfTasks[indexExam]
    }

    fun sortTasks(indexGroup: Int, sortIndex: Int)
    {
        if (sortIndex == 0)
        {
            val tempArrayListOfTasksNames: ArrayList<String> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksNames.add(i.nameOfTask.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksNames.sort()
            for (i in tempArrayListOfTasksNames)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.nameOfTask.lowercase(Locale.ROOT)
                        && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 1)
        {
            val tempArrayListOfTasksConditions: ArrayList<String> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksConditions.add(i.hint.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksConditions.sort()
            for (i in tempArrayListOfTasksConditions)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.hint.lowercase(Locale.ROOT)
                        && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 2)
        {
            val tempArrayListOfTasksNumbers: ArrayList<Int> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksNumbers.add(i.num)
            }
            tempArrayListOfTasksNumbers.sort()
            for (i in tempArrayListOfTasksNumbers)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.num && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 3)
        {
            val tempArrayListOfTasksNumOfParticipants: ArrayList<String> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksNumOfParticipants.add(i.numOfParticipants.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksNumOfParticipants.sort()
            for (i in tempArrayListOfTasksNumOfParticipants)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.numOfParticipants.lowercase(Locale.ROOT)
                        && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 4)
        {
            val tempArrayListOfTasksTimeForSolve: ArrayList<String> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksTimeForSolve.add(i.timeForSolve.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksTimeForSolve.sort()
            for (i in tempArrayListOfTasksTimeForSolve)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.timeForSolve.lowercase(Locale.ROOT)
                        && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 5)
        {
            val tempArrayListOfTasksMaxScore: ArrayList<Int> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksMaxScore.add(i.maxScore)
            }
            tempArrayListOfTasksMaxScore.sort()
            for (i in tempArrayListOfTasksMaxScore)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.maxScore && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 6)
        {
            val tempArrayListOfTasksIsComplicated: ArrayList<Int> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksIsComplicated.add(i.isComplicated)
            }
            tempArrayListOfTasksIsComplicated.sort()
            for (i in tempArrayListOfTasksIsComplicated)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.isComplicated && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }

        if (sortIndex == 7)
        {
            val tempArrayListOfTasksHints: ArrayList<String> = ArrayList()
            val tempArrayListOfTasks: ArrayList<Task> = ArrayList()
            for (i in rounds[indexGroup].listOfTasks)
            {
                tempArrayListOfTasksHints.add(i.condition.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksHints.sort()
            for (i in tempArrayListOfTasksHints)
            {
                for (j in rounds[indexGroup].listOfTasks)
                {
                    if (i == j.condition.lowercase(Locale.ROOT)
                        && !tempArrayListOfTasks.contains(j))
                    {
                        tempArrayListOfTasks.add(j)
                        break
                    }
                }
            }
            rounds[indexGroup].listOfTasks = tempArrayListOfTasks
        }
    }
}
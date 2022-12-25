package com.example.androidbi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbi.databinding.ActivityMainBinding
import com.example.androidbi.olympiad.Task
import com.example.androidbi.olympiad.Round
import com.example.androidbi.olympiad.RoundOperator
import com.example.androidbi.olympiad.dbWithRoom.App
import com.example.androidbi.olympiad.dbWithRoom.AppDatabase
import com.example.androidbi.olympiad.dbWithRoom.RoundOperatorOperatorDao
import com.example.androidbi.forRecyclerView.CustomRecyclerAdapterForExams
import com.example.androidbi.forRecyclerView.RecyclerItemClickListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TaskDetailsDialogFragment.OnInputListenerSortId
{
    private val gsonBuilder = GsonBuilder()
    private val gson: Gson = gsonBuilder.create()
    private val serverIP = "192.168.1.69"
    private val serverPort = 9999
    private lateinit var connection: Connection
    private var connectionStage: Int = 0
    private var startTime: Long = 0

    private lateinit var db: AppDatabase
    private lateinit var roDao: RoundOperatorOperatorDao

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewTasks: RecyclerView

    private var ro: RoundOperator = RoundOperator()
    private var currentRoundID: Int = -1
    private var currentTaskID: Int = -1
    private var waitingForUpdate: Boolean = false
    private lateinit var roundTitle: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        nv = binding.navView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        progressBar = findViewById(R.id.progressBar)
        recyclerViewTasks = findViewById(R.id.recyclerViewExams)
        recyclerViewTasks.visibility = View.INVISIBLE
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        recyclerViewTasks.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerViewTasks,
                object : RecyclerItemClickListener.OnItemClickListener
                {
                    override fun onItemClick(view: View, position: Int)
                    {
                        currentTaskID = position
                        Snackbar.make(findViewById(R.id.app_bar_main),
                            "Количество участников: ${ro.getTask(currentRoundID, currentTaskID)
                                .numOfParticipants}",
                            Snackbar.LENGTH_LONG)
                            .show()
                    }
                    override fun onItemLongClick(view: View, position: Int)
                    {
                        currentTaskID = position
                        val examDetails = TaskDetailsDialogFragment()
                        val tempExam = ro.getTask(currentRoundID, currentTaskID)
                        val bundle = Bundle()
                        bundle.putString("nameOfTask", tempExam.nameOfTask)
                        bundle.putString("hint", tempExam.hint)
                        bundle.putString("number", tempExam.num.toString())
                        bundle.putString("numOfParticipants", tempExam.numOfParticipants)
                        bundle.putString("timeForSolve", tempExam.timeForSolve)
                        bundle.putString("maxScore", tempExam.maxScore.toString())
                        bundle.putString("isComplicated", tempExam.isComplicated.toString())
                        bundle.putString("condition", tempExam.condition)
                        bundle.putString("connection", connectionStage.toString())
                        examDetails.arguments = bundle
                        examDetails.show(fragmentManager, "MyCustomDialog")
                    }
                }
            )
        )

        db = App.instance?.database!!
        roDao = db.groupOperatorDao()
        startTime = System.currentTimeMillis()
        connection = Connection(serverIP, serverPort, "REFRESH", this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean
    {
        if (currentRoundID != -1 && connectionStage == 1)
        {
            menu.getItem(0).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId
        if (id == R.id.action_add)
        {
            val intent = Intent()
            intent.setClass(this, EditTaskActivity::class.java)
            intent.putExtra("action", 1)
            startActivityForResult(intent, 1)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run()
            {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {
                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                        {
                            activity.runOnUiThread { processingInputStream(message) }
                        } else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable
        {
            override fun run()
            {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable
        {
            override fun run() {
                while (true)
                {
                    if (System.currentTimeMillis() - startTime > 5000L && connectionStage == 0)
                    {
                        activity.runOnUiThread { Snackbar.make(findViewById(R.id.app_bar_main),
                            "Подключиться не удалось!\n" +
                                    "Будет использоваться локальная база данных.",
                            Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.YELLOW)
                            .show() }
                        connectionStage = -1
                        activity.runOnUiThread { progressBar.visibility = View.INVISIBLE }
                        ro = roDao.getById(1)
                        for (i in 0 until ro.getRounds().size)
                        {
                            activity.runOnUiThread { nv.menu.add(0, i, 0,
                                ro.getRounds()[i].name as CharSequence) }
                        }
                    }
                }
            }
        }

        fun sendDataToServer(text: String)
        {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String)
        {
            roDao.delete(RoundOperator())
            val tempGo: RoundOperator = gson.fromJson(text, RoundOperator::class.java)
            roDao.insert(tempGo)

            if (connectionStage != 1)
            {
                Snackbar.make(findViewById(R.id.app_bar_main),
                    "Успешно подключено!\n" +
                            "Будут использоваться серверные данные.",
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.GREEN)
                    .show()
            }

            progressBar.visibility = View.INVISIBLE
            for (i in 0 until ro.getRounds().size)
            {
                nv.menu.removeItem(i)
            }
            val tempArrayListRounds: ArrayList<Round> = /*dbh.getAllData()*/tempGo.getRounds()
            ro.setRounds(tempArrayListRounds)
            for (i in 0 until tempArrayListRounds.size)
            {
                nv.menu.add(
                    0, i, 0,
                    tempArrayListRounds[i].name as CharSequence
                )
            }
            if (waitingForUpdate || connectionStage == -1)
            {
                waitingForUpdate = false
                if (currentRoundID != -1)
                {
                    recyclerViewTasks.adapter = CustomRecyclerAdapterForExams(
                        ro.getTasksNames(currentRoundID),
                        ro.getTaskNumbers(currentRoundID),
                        ro.getTaskMaxScore(currentRoundID)
                    )
                }
            }
            connectionStage = 1
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Snackbar.make(findViewById(R.id.app_bar_main),
            "Выбран: ${item.title}.",
            Snackbar.LENGTH_LONG)
            .show()
        drawerLayout.closeDrawer(GravityCompat.START)
        roundTitle = "${item.title}"
        toolbar.title = roundTitle
        invalidateOptionsMenu()
        currentRoundID = item.itemId
        recyclerViewTasks.adapter = CustomRecyclerAdapterForExams(
            ro.getTasksNames(currentRoundID),
            ro.getTaskNumbers(currentRoundID),
            ro.getTaskMaxScore(currentRoundID))
        recyclerViewTasks.visibility = View.VISIBLE
        return true
    }

    fun delTask()
    {
        connection.sendDataToServer("d$currentRoundID,$currentTaskID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int)
    {
        if (sortId > -1 && sortId < 8)      // Сортировка
        {
            ro.sortTasks(currentRoundID, sortId)
            if (connectionStage == 1)
            {
                connection.sendDataToServer("u" + gson.toJson(ro))
            }
            toolbar.title = when (sortId)
            {
                0 -> "$roundTitle сорт. Назввние"
                1 -> "$roundTitle сорт. Подсказка"
                2 -> "$roundTitle сорт. №"
                3 -> "$roundTitle сорт. Участники."
                4 -> "$roundTitle сорт. Время"
                5 -> "$roundTitle сорт. Балл"
                6 -> "$roundTitle сорт. Усложненный"
                7 -> "$roundTitle сорт. Условие"
                else -> roundTitle
            }
        }
        if (sortId == 8)        // Удаление
        {
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelTask = MyDialogFragmentDelTask()
            val bundle = Bundle()
            bundle.putString("name", ro.getTask(currentRoundID, currentTaskID).nameOfTask)
            myDialogFragmentDelTask.arguments = bundle
            myDialogFragmentDelTask.show(manager, "myDialog")
        }
        if (sortId == 9)        // Изменение
        {
            val tempTask = ro.getTask(currentRoundID, currentTaskID)
            val intent = Intent()
            intent.setClass(this, EditTaskActivity::class.java)
            intent.putExtra("action", 2)
            intent.putExtra("nameOfTask", tempTask.nameOfTask)
            intent.putExtra("hint", tempTask.hint)
            intent.putExtra("number", tempTask.num.toString())
            intent.putExtra("numOfParticipants", tempTask.numOfParticipants)
            intent.putExtra("timeForSolve", tempTask.timeForSolve)
            intent.putExtra("maxScore", tempTask.maxScore.toString())
            intent.putExtra("isComplicated", tempTask.isComplicated.toString())
            intent.putExtra("condition", tempTask.condition)
            startActivityForResult(intent, 1)
        }
        recyclerViewTasks.adapter = CustomRecyclerAdapterForExams(
            ro.getTasksNames(currentRoundID),
            ro.getTaskNumbers(currentRoundID),
            ro.getTaskMaxScore(currentRoundID))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            val action = data?.getSerializableExtra("action") as Int
            val examName = data.getSerializableExtra("nameOfTask") as String
            val teacherName = data.getSerializableExtra("hint") as String
            val auditory = data.getSerializableExtra("number") as Int
            val date = data.getSerializableExtra("numOfParticipants") as String
            val time = data.getSerializableExtra("timeForSolve") as String
            val people = data.getSerializableExtra("maxScore") as Int
            val abstract = data.getSerializableExtra("isComplicated") as Int
            val comment = data.getSerializableExtra("condition") as String
            val tempTask = Task(examName, teacherName, auditory, date, time, people
                , abstract, comment)
            val tempTaskJSON: String = gson.toJson(tempTask)

            if (action == 1)
            {
                val tempStringToSend = "a${ro.getRounds()[currentRoundID].name}##$tempTaskJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
            if (action == 2)
            {
                val tempStringToSend = "e$currentRoundID,$currentTaskID##$tempTaskJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
        }
    }
}
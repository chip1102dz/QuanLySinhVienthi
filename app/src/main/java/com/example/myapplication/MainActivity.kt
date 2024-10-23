package com.example.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var userDatabase: UserDatabase
    var list = mutableListOf<User>()
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        intent = result.data
        if(result.resultCode == Activity.RESULT_OK){

        }
    } )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        recyclerView = activityMainBinding.rcv
        userDatabase = UserDatabase(this)
        userAdapter = UserAdapter(userDatabase, object : ItemOnClickListener{
            override fun OnClickItem(user: User) {
                intent = Intent(this@MainActivity, InforActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key", user)
                intent.putExtras(bundle)
                resultLauncher.launch(intent)
            }

            override fun OnClickItem1(user: User) {
                activityMainBinding.edtDate.setText(user.date)
                activityMainBinding.edtName.setText(user.name)
                activityMainBinding.btnDeleteUser.setOnClickListener {
                    deleteUser(user.id)
                }
                activityMainBinding.btnUpdateUser.setOnClickListener {
                    updateUser(user)
                }
            }
        })
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        activityMainBinding.edtDate.setOnClickListener {
            setDate()
        }

        //Xu ly button
        activityMainBinding.btnAddUser.setOnClickListener {
            addUser()
        }
        getAllUser()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setDate() {
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date = "" + dayOfMonth + "/" + (month+1) + "/" + year
            activityMainBinding.edtDate.setText(date)
        }, 1990, 0, 1)
        datePickerDialog.show()
    }

    private fun addUser() {
        val name = activityMainBinding.edtName.text.toString()
        val date = activityMainBinding.edtDate.text.toString()
        val image = R.drawable.ic_launcher_background

        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(date)){
            Toast.makeText(this, "HAY NHAP DU THONG TIN", Toast.LENGTH_SHORT).show()
            return
        }
        if(!kiemtra(name)){
            Toast.makeText(this, "HAY TEN VIET HOA", Toast.LENGTH_SHORT).show()
            return
        }
        if(userDatabase.checkUser(name)){
            Toast.makeText(this, "TRUNG DU LIEU", Toast.LENGTH_SHORT).show()
            return
        }
        val user = User(name = name, date = date, image = image)
        userDatabase.insertUser(user)
        activityMainBinding.edtName.setText("")
        activityMainBinding.edtDate.setText("")
        getAllUser()
    }

    private fun getAllUser() {
        list = userDatabase.getAllData()
        userAdapter.setData(list)
    }

    private fun kiemtra(str: String): Boolean{
        return str == str.uppercase()
    }
    private fun deleteUser(id: Int){
        AlertDialog.Builder(this@MainActivity)
            .setTitle("XOA USER")
            .setMessage("BAN CO MUON XOA USER NAY ?")
            .setPositiveButton("CO", DialogInterface.OnClickListener { dialog, which ->
                userDatabase.delete(id)
                getAllUser()
            }).setNegativeButton("KHONG", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).show()
    }
    private fun updateUser(user: User){
        val name = activityMainBinding.edtName.text.toString()
        val date = activityMainBinding.edtDate.text.toString()

        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(date)){
            Toast.makeText(this, "HAY NHAP DU THONG TIN", Toast.LENGTH_SHORT).show()
            return
        }
        if(!kiemtra(name)){
            Toast.makeText(this, "HAY TEN VIET HOA", Toast.LENGTH_SHORT).show()
            return
        }
        user.name = name
        user.date = date

        AlertDialog.Builder(this@MainActivity)
            .setTitle("XOA USER")
            .setMessage("BAN CO MUON SUA USER NAY ?")
            .setPositiveButton("CO", DialogInterface.OnClickListener { dialog, which ->
                userDatabase.update(user)
                getAllUser()
            }).setNegativeButton("KHONG", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).show()
    }
}
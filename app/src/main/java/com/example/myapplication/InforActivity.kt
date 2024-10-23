package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityInforBinding

class InforActivity : AppCompatActivity() {
    lateinit var inforBinding: ActivityInforBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        inforBinding = ActivityInforBinding.inflate(layoutInflater)
        setContentView(inforBinding.root)

        var user = User()
        user = intent.getSerializableExtra("key") as User
        inforBinding.tvName.setText(user.name)
        inforBinding.tvDate.setText(user.date)
        inforBinding.imgView.setImageResource(user.image)

        inforBinding.btnBack.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
package com.panjikrisnayasa.bigproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener, ValueEventListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_home_logout.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        mUserDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

        val cUser = mAuth.currentUser
        if (cUser != null) {
            mUserDatabaseReference.child(cUser.uid).addValueEventListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_home_logout -> {
                mAuth.signOut()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }
    }

    override fun onDataChange(p0: DataSnapshot) {
        val user = p0.getValue(User::class.java)
        if (user != null) {
            val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            if (currentTime >= 12) {
                iv_home_greetings_background.setImageResource(R.drawable.image_afternoon)
                tv_home_welcome.text = getString(R.string.home_tv_welcome, "Afternoon", user.fullName)
            } else if (currentTime >= 19) {
                iv_home_greetings_background.setImageResource(R.drawable.image_night)
                tv_home_welcome.text = getString(R.string.home_tv_welcome, "Night", user.fullName)
            } else if (currentTime >= 0) {
                iv_home_greetings_background.setImageResource(R.drawable.image_morning)
                tv_home_welcome.text = getString(R.string.home_tv_welcome, "Morning", user.fullName)
            }
        }
    }

    override fun onCancelled(p0: DatabaseError) {
        Toast.makeText(this, "Error: ${p0.code}", Toast.LENGTH_SHORT).show()
    }
}

package mykotlin.roshanpaturkar.com.keysstand.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import mykotlin.roshanpaturkar.com.keysstand.R

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var userId: String? = null
    var mAuthListner: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->

            user = firebaseAuth.currentUser

            if (user != null){
                checkPinStatus()
            }else{
                Toast.makeText(this, "User Not Signed In...!!!", Toast.LENGTH_LONG).show()
            }
        }

        gotoLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        gotoRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }

    }

    private fun checkPinStatus() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        var pin: String? = null
        userId = user!!.uid
        var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseRef: DatabaseReference = mDatabase.getReference("KeysStand").child("Users").child(userId)

        mDatabaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                pin = dataSnapshot!!.child("pin").value.toString()
                if (pin.equals("null")){
                    gotoCreatePinActivity()
                }else{
                    gotoVerifyPinActivity(pin!!)
                }
            }
            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    private fun gotoVerifyPinActivity(pin: String) {
        var verify = Intent(this, VerifyPinActivity::class.java)
        verify.putExtra("pin", pin)
                        startActivity(verify)
                        finish()
    }

    private fun gotoCreatePinActivity() {
        startActivity(Intent(this, CreatePinActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListner!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListner != null){
            mAuth!!.removeAuthStateListener(mAuthListner!!)
        }
    }
}

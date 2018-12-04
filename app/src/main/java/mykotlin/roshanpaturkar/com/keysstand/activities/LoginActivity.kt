package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.ColorUtils
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import mykotlin.roshanpaturkar.com.keysstand.R

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var userId: String? = null
    var mCurrentUser: FirebaseUser? = null
    var progressDailog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white
//        loginCardView.setCardBackgroundColor(Color.argb(150, 155, 155, 155)) //semi transparent
//        loginCardView.setCardBackgroundColor(Color.TRANSPARENT) // transparent
        supportActionBar!!.title = "Login!"

        mAuth = FirebaseAuth.getInstance()
        progressDailog = ProgressDialog(this)

        loginButtonId.setOnClickListener {

            var email: String = loginEmailE.text.toString().trim()
            var password: String = loginPasswordEt.text.toString().trim()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                loginUser(email, password)
            }else{
                if (TextUtils.isEmpty(email)){
                    loginEmailE.setError("Email required !")
                }
                if (TextUtils.isEmpty(password)){
                    loginPasswordEt.setError("Password required !")
                }
                //Toast.makeText(this, "Please fill all the detail's...!!!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun loginUser(email: String, password: String) {

        progressDailog!!.setMessage("Signing In...")
        progressDailog!!.show()

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    task: Task<AuthResult> ->

                    if (task.isSuccessful){
                        progressDailog!!.dismiss()
//                        var userName = email.split("@")[0] //split = roshan@gmail.com --> [roshan],[gmail.com]
//                        //Toast.makeText(this, "$userName Successfully SignedIn!", Toast.LENGTH_LONG).show()
//                        var dashboardIntent = Intent(this, DashboardActivity::class.java)
//                        dashboardIntent.putExtra("name", userName)
//                        startActivity(dashboardIntent)
//                        finish()
                        checkPinStatus()
                    }else{
                        progressDailog!!.dismiss()
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }

                }

    }

    private fun checkPinStatus() {

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        var pin: String? = null
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
    }
}

package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import mykotlin.roshanpaturkar.com.keysstand.R

class RegisterActivity : AppCompatActivity() {

    var userId: String? = null
    var mCurrentUser: FirebaseUser? = null

    var mAuth: FirebaseAuth? = null
    var mDatabaseRef: DatabaseReference? = null

    var progressDailog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white
        supportActionBar!!.title = "Register!"

        mAuth = FirebaseAuth.getInstance()
        progressDailog = ProgressDialog(this)

        accountCreateActBtn.setOnClickListener {

            var email: String = accountEmailEt.text.toString().trim()
            var password: String = accountPasswordEt.text.toString().trim()
            var displayName: String = accountDisplayNameEt.text.toString().trim()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(displayName)){
                createAccount(email, password, displayName)
            }else{
                Toast.makeText(this, "Please fill all the detail's...!!!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun createAccount(email: String, password: String, displayName: String) {

        progressDailog!!.setMessage("Signing Up...")
        progressDailog!!.show()

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->

                    if (task.isSuccessful){
                        var currentUser = mAuth!!.currentUser
                        var userID = currentUser!!.uid

                        mDatabaseRef = FirebaseDatabase.getInstance().reference
                                .child("KeysStand")
                                .child("Users").child(userID)

                        var userObject = HashMap<String, String>()
                        userObject.put("display_name", displayName)
                        userObject.put("email", email)
                        userObject.put("password", password)

                        mDatabaseRef!!.setValue(userObject).addOnCompleteListener {

                            task: Task<Void> ->

                            if (task.isSuccessful){
                                progressDailog!!.dismiss()
                                Toast.makeText(this, "$displayName Successfully Sign Up!", Toast.LENGTH_LONG).show()

//                                var dashboardIntent = Intent(this, DashboardActivity::class.java)
//                                dashboardIntent.putExtra("name", displayName)
//                                startActivity(dashboardIntent)
//                                finish()

                                checkPinStatus()
                            }else {
                                progressDailog!!.dismiss()
                                Toast.makeText(this, "$displayName Failed to Sign Up!", Toast.LENGTH_LONG).show()
                            }
                        }

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

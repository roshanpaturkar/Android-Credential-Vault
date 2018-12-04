package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_verify_pin.*
import mykotlin.roshanpaturkar.com.keysstand.R

class VerifyPinActivity : AppCompatActivity() {

    var pin: String? = null

    var mDatabase: DatabaseReference? = null
    var userId: String? = null
    var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_pin)
        supportActionBar!!.title = "Verify PIN!"
        verifyPinCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white

        mDatabase = FirebaseDatabase.getInstance().reference
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        if (intent != null){
            pin = intent.extras.get("pin").toString()
        }

        verifyPinButton.setOnClickListener {
            var vPin = verifyPinEnterPin.text.toString().trim()

            if (!TextUtils.isEmpty(vPin)){
                if (pin.equals(vPin)){
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }else{
                    verifyPinEnterPin.setError("Incorrect PIN!")
                }
            }else{
                verifyPinEnterPin.setError("Enter PIN!")
            }
        }

        resetPin.setOnClickListener {
            resetPIN()
        }
    }

    private fun resetPIN() {

        var data = HashMap<String, Any>()
        data.put("user_name", pin.toString())

        var deletedData: DatabaseReference = FirebaseDatabase.getInstance().reference
        deletedData!!                   //backing up deleted data
                .child("KeysStand")
                .child("DeletedPIN")
                .child(userId)
                .push()
                .setValue(data)

        mDatabase!!                     //deleting data
                .child("KeysStand")
                .child("Users")
                .child(userId)
                .child("pin")
                .setValue(null)

        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}

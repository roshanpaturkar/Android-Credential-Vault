package mykotlin.roshanpaturkar.com.keysstand.activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_change_pin.*
import mykotlin.roshanpaturkar.com.keysstand.R

class ChangePinActivity : AppCompatActivity() {

    var nPin: String? = null
    var vPin: String? = null

    var mDatabaseRef: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        changePinCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white
        supportActionBar!!.title = "Change PIN!!"

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        mDatabaseRef = FirebaseDatabase.getInstance().reference
                .child("KeysStand")
                .child("Users")
                .child(userId)

        changePinButton.setOnClickListener {

            var pin = changePinOldPin.text.toString().trim()
            nPin = changePinNewPin.text.toString().trim()
            vPin = changePinVerifyPin.text.toString().trim()


            if (!TextUtils.isEmpty(pin) && !TextUtils.isEmpty(nPin) && !TextUtils.isEmpty(vPin)){
                checkPIN(pin)
            }else{
                changePinOldPin.setError("Enter PIN!")
            }
        }
    }

    private fun checkPIN(pin: String) {
        var pinn: String? = null
        var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseRef: DatabaseReference = mDatabase.getReference("KeysStand").child("Users").child(userId)

        mDatabaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                pinn = dataSnapshot!!.child("pin").value.toString()
                if (pin.equals(pinn)){
                    changePin()
                }else{
                    changePinOldPin.setError("Incorrect PIN!")
                }
            }
            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    private fun changePin() {
        if (nPin.equals(vPin)){
            var data = HashMap<String, Any>()
            data.put("pin", nPin!!)

            var pinn: DatabaseReference = mDatabaseRef!!
            pinn!!.updateChildren(data).addOnCompleteListener {
                startActivity(Intent(this, VerifyPinActivity::class.java))
                finish()
            }
        }else{
            changePinVerifyPin.setError("PIN not match!")
        }
    }
}

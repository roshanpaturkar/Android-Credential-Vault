package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_pin.*
import mykotlin.roshanpaturkar.com.keysstand.R

class CreatePinActivity : AppCompatActivity() {

    var pin: String? = null
    var vPin: String? = null

    var mDatabaseRef: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)
        supportActionBar!!.title = "Create PIN!"
        createPinCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        mDatabaseRef = FirebaseDatabase.getInstance().reference
                .child("KeysStand")
                .child("Users")
                .child(userId)

        alert()

        createPinButton.setOnClickListener {

            pin = createPinEnterPin.text.toString().trim()
            vPin = createPinVerifyPin.text.toString().trim()

            if (!TextUtils.isEmpty(pin) && !TextUtils.isEmpty(vPin)){
                if (pin.equals(vPin)){
                    createPin(pin!!)
                }else{
                    createPinVerifyPin.setError("PIN not match!")
                }
            }else{
                Toast.makeText(this, "Please fill all the details!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun alert() {
        var data = AlertDialog.Builder(this)
        with(data){
            setTitle("Warning!")
            setMessage("These feature needs to login and verify PIN multiple time for security reasons while creating PIN!")
            setPositiveButton("OK"){
                dialog, which ->
                dialog.dismiss()
            }
        }
        data.show()
    }

    private fun createPin(pin: String) {
        var data = HashMap<String, Any>()
        data.put("pin", pin)

        var pinn: DatabaseReference = mDatabaseRef!!
        pinn!!.updateChildren(data)

        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

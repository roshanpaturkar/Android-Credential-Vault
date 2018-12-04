package mykotlin.roshanpaturkar.com.keysstand.activities

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
import kotlinx.android.synthetic.main.activity_add.*
import mykotlin.roshanpaturkar.com.keysstand.R

class AddActivity : AppCompatActivity(){

    var mDatabaseRef: DatabaseReference? = null

    var emailID: String? = null
    var password: String? = null

    var mCurrentUser: FirebaseUser? = null
    var name: String? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar!!.title = "Add Record!"
        addRecordCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        mDatabaseRef = FirebaseDatabase.getInstance().reference
                .child("KeysStand")
                .child("Records")
                .child(userId)

        saveButtonAddRecord.setOnClickListener {
            saveRecords()
        }
    }

    private fun saveRecords() {
        emailID = userNameAddRecord.text.toString().trim()
        password = passwordAddRecord.text.toString().trim()

        if(!TextUtils.isEmpty(emailID) && !TextUtils.isEmpty(password)){
            var data = HashMap<String, Any>()
            data.put("user_name", emailID.toString())
            data.put("password", password.toString())

            var record: DatabaseReference = mDatabaseRef!!.push()

            if (!(record!!.setValue(data).isSuccessful)) {
                Toast.makeText(this, "Data Saved successfully!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
//            }else{
//                Toast.makeText(this, "Offline Data Saved successfully!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }

//            record!!.setValue(data).addOnCompleteListener {
//                Toast.makeText(this, "Data Saved successfully!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }
//
//            record!!.setValue(data).addOnFailureListener {
//                Toast.makeText(this, "Offline Data Saved successfully!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }

        }else{
            Toast.makeText(this, "Please fill all the details!", Toast.LENGTH_LONG).show()
        }
    }
}

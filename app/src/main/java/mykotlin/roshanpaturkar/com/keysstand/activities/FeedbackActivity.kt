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
import kotlinx.android.synthetic.main.activity_feedback.*
import mykotlin.roshanpaturkar.com.keysstand.R

class FeedbackActivity : AppCompatActivity() {

    var fullName: String? = null
    var issue: String? = null
    var description: String? = null

    var mDatabaseRef: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        feedbackCardView.setCardBackgroundColor(Color.argb(200,255,255,255)) // semi white
        supportActionBar!!.title = "Feedback!"

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        mDatabaseRef = FirebaseDatabase.getInstance().reference
                .child("KeysStand")
                .child("Feedback")

        subbmitButtonFeedback.setOnClickListener {
            submitFeedback()
        }
    }

    private fun submitFeedback() {
        fullName = fullNameFeedback.text.toString().trim()
        issue = issueFeedback.text.toString().trim()
        description = describtionFeedback.text.toString()


        if(!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(issue) && !TextUtils.isEmpty(description)){
            var data = HashMap<String, Any>()
            data.put("name", fullName.toString())
            data.put("user_id", userId.toString())
            data.put("issue", issue.toString())
            data.put("description", description.toString())

            var record: DatabaseReference = mDatabaseRef!!.push()

            if (!(record!!.setValue(data).isSuccessful)) {
                Toast.makeText(this, "Feedback Successfully Submitted!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
//            }else{
//                Toast.makeText(this, "Offline Feedback Successfully Submitted!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }

//            record!!.setValue(data).addOnCompleteListener {
//                Toast.makeText(this, "Feedback Successfully Submitted!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }
//
//            record!!.setValue(data).addOnFailureListener {
//                Toast.makeText(this, "Offline Feedback Successfully Submitted!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }

        }else{
            Toast.makeText(this, "Please fill all the details!", Toast.LENGTH_LONG).show()
        }
    }
}

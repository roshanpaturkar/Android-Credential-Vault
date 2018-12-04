package mykotlin.roshanpaturkar.com.keysstand.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_view_feedback.*
import mykotlin.roshanpaturkar.com.keysstand.R
import mykotlin.roshanpaturkar.com.keysstand.adapters.FeedbackAdapter

class ViewFeedbackActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_feedback)
        supportActionBar!!.title = "Public Feedback's!"
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)    // no need of these, it will take instance from dashboard activity

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        mDatabase = FirebaseDatabase.getInstance().reference
                .child("KeysStand")
                .child("Feedback")
        mDatabase!!.keepSynced(true)

        viewFeedbackRecyclerView.setHasFixedSize(true)

        viewFeedbackRecyclerView.layoutManager = linearLayoutManager
        viewFeedbackRecyclerView.adapter = FeedbackAdapter(mDatabase!!, this)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}

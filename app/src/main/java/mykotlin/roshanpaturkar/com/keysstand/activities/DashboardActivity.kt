package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import mykotlin.roshanpaturkar.com.keysstand.R
import mykotlin.roshanpaturkar.com.keysstand.adapters.RecordAdapter
import kotlin.system.exitProcess

class DashboardActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var userId: String? = null
    var mCurrentUser: FirebaseUser? = null

    var version = "1.3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Home!"
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        updateTest()

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        var linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, true)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        mDatabase = FirebaseDatabase.getInstance().reference.child("KeysStand")
                .child("Records")
                .child(userId)
        mDatabase!!.keepSynced(true)

        dashboardRecordsRecyclerView.setHasFixedSize(true)

        dashboardRecordsRecyclerView.layoutManager = linearLayoutManager
        dashboardRecordsRecyclerView.adapter = RecordAdapter(mDatabase!!, this)

        addCredintialsButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

    }

    private fun updateTest() {
        var ver: String? = null
        var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseRef: DatabaseReference = mDatabase.getReference("KeysStand").child("Updates")

        mDatabaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                ver = dataSnapshot!!.child("version").value.toString()
                if (ver.equals(version)){
                }else{
                    downloadUpdate("YES")
                }
            }
            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null){
            when(item.itemId){

                R.id.feedback -> {
                    startActivity(Intent(this, FeedbackActivity::class.java))
                }

                R.id.viewFeedback -> {
                    startActivity(Intent(this, ViewFeedbackActivity::class.java))
                }

                R.id.update -> {
                    checkUpdates()
                }

                R.id.changePin -> {
                    startActivity(Intent(this, ChangePinActivity::class.java))
                }

                R.id.about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }

                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
        return true
    }

    private fun checkUpdates() {

        var ver: String? = null

        var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseRef: DatabaseReference = mDatabase.getReference("KeysStand").child("Updates")

        mDatabaseRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                ver = dataSnapshot!!.child("version").value.toString()
                if (ver.equals(version)){
                    downloadUpdate("NO")
                }else{
                    downloadUpdate("YES")
                }
            }

            override fun onCancelled(error: DatabaseError?) {}

        })
    }

    private fun downloadUpdate(status: String?) {
        if (status.equals("YES")){

            var data = AlertDialog.Builder(this)
            with(data){
                setTitle("Updates!")
                setMessage("Application update is available!\nDo you want to download?")
                setPositiveButton("Yes"){
                    dialog, which ->
                    dialog.dismiss().also { download() }
                }
                setNegativeButton("No"){
                    dialog, which ->
                    dialog.dismiss()
                }
            }
            data.show()
        }else{
            var data = AlertDialog.Builder(this)
            with(data){
                setTitle("Updates!")
                setMessage("Your application is up to date!")
                setPositiveButton("OK"){
                    dialog, which ->
                    dialog.dismiss()
                }
            }

            data.show()
        }
    }

    private fun download() {
        var appLink: String? = null
        var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseRef: DatabaseReference = mDatabase.getReference("KeysStand").child("Updates")

        mDatabaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                appLink = dataSnapshot!!.child("link").value.toString()
                val uri = Uri.parse(appLink)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError?) {}
        })
    }

    private fun checkPinStatus() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
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

    override fun onBackPressed() {
        super.onBackPressed()
        //getSystemService(exitProcess(0))
        moveTaskToBack(true)
    }

    override fun onRestart() {
        super.onRestart()
        checkPinStatus()
    }
}

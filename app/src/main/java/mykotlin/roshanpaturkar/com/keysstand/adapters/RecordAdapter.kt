package mykotlin.roshanpaturkar.com.keysstand.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mykotlin.roshanpaturkar.com.keysstand.R
import mykotlin.roshanpaturkar.com.keysstand.models.Records

class RecordAdapter(dataBaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Records, RecordAdapter.ViewHolder>(
        Records::class.java,
        R.layout.record_row,
        RecordAdapter.ViewHolder::class.java,
        dataBaseQuery
){

    var mDatabase: DatabaseReference? = null
    var userId: String? = null
    var mCurrentUser: FirebaseUser? = null

    override fun populateViewHolder(viewHolder: ViewHolder?, records: Records?, position: Int) {
        val itemKey = getRef(position).key
        viewHolder!!.bindView(records!!, context)

        mDatabase = FirebaseDatabase.getInstance().reference
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        userId = mCurrentUser!!.uid

        viewHolder.itemView.setOnClickListener {
            var id = viewHolder.user_name
            var passwd = viewHolder.password


           showMenu(id, passwd, itemKey)
        }
    }

    private fun showMenu(id: String?, passwd: String?, key: String?) {

        var options = arrayOf("View", "Delete")
        var builder = android.support.v7.app.AlertDialog.Builder(context)
        builder.setTitle("Selecte Options")
        builder.setItems(options, DialogInterface.OnClickListener { dialogInterface, i ->

            if (i == 0) {
                //show details
                showDetails(id, passwd)

            } else {
                //make backup for deleted data and delete the data

                var warn = AlertDialog.Builder(context)  // warning dialog
                with(warn){
                    setTitle("Warning !")
                    setMessage("Do you really want delete ?")
                    setPositiveButton("Yes"){
                        dialog, which ->
                        deleteRecord(id, passwd, key)   // call for delete
                    }
                    setNegativeButton("No"){
                        dialog, which ->
                        dialog.dismiss()                // for negative response
                    }
                }

                warn.show()
            }
        })

        builder.show()
    }

    private fun deleteRecord(id: String?, passwd: String?, key: String?){

        var data = HashMap<String, Any>()
        data.put("user_name", id.toString())
        data.put("password", passwd.toString())

        var deletedData: DatabaseReference = FirebaseDatabase.getInstance().reference

        deletedData!!                   //backing up deleted data
                .child("KeysStand")
                .child("DeletedRecord")
                .child(userId)
                .push()
                .setValue(data)

        mDatabase!!                     //deleting data
                .child("KeysStand")
                .child("Records")
                .child(userId)
                .child(key)
                .setValue(null)

        Toast.makeText(context, "Record Deleted Successfully!", Toast.LENGTH_LONG).show()
    }

    private fun showDetails(id: String?, passwd: String?) {
        var data = AlertDialog.Builder(context)
        with(data){
            setTitle("Detail's : ")
            setMessage("Email : " + id + "\n" + "Password : " + passwd)
            setPositiveButton("OK"){
                dialog, which ->
                dialog.dismiss()
            }
        }

        data.show()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var user_name: String? = null
        var password: String? = null

        fun bindView(records: Records?, context: Context) {

            var userName = itemView.findViewById<TextView>(R.id.emailTextViewRow)
            var pass = itemView.findViewById<TextView>(R.id.passwordTextViewRow)

            user_name = records!!.user_name
            password = records!!.password

            userName.text = records.user_name
            pass.text = ""
            //pass.text = records.password

        }

    }
}
package mykotlin.roshanpaturkar.com.keysstand.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*
import mykotlin.roshanpaturkar.com.keysstand.R

class ForgetPassword : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    var progressDailog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        forgetPasswordCardView.setCardBackgroundColor(Color.argb(200,255,255,255))
        supportActionBar!!.title = "Forget Password!"

        mAuth = FirebaseAuth.getInstance()
        progressDailog = ProgressDialog(this)

        forgetPasswordButton.setOnClickListener {

            var emailID = forgetPasswordEditText.text.toString()

            if (!TextUtils.isEmpty(emailID)){

                progressDailog!!.setMessage("Sending request to server...")
                progressDailog!!.show()

                mAuth!!.sendPasswordResetEmail(emailID)
                        .addOnCompleteListener{

                            task ->

                            if (task.isSuccessful){
                                progressDailog!!.dismiss()

                                var data = AlertDialog.Builder(this)
                                with(data){
                                    setTitle("Request Send!")
                                    setMessage("Please check your mailbox and follow the instructions! \n\t\t\t\t\t       Thank You!")
                                    setPositiveButton("OK"){
                                        dialog, which ->
                                        dialog.dismiss().also {
                                            startActivity(Intent(context, MainActivity::class.java))
                                            finish()
                                        }
                                    }
                                }

                                data.show()

                            }else{
                                progressDailog!!.dismiss()
                                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                            }
                        }
                //startActivity(Intent(this, MainActivity::class.java))
            }else{
                forgetPasswordEditText.setError("Enter email!")
            }
        }
    }
}

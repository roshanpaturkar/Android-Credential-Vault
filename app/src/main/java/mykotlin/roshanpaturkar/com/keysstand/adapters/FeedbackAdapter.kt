package mykotlin.roshanpaturkar.com.keysstand.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import mykotlin.roshanpaturkar.com.keysstand.R
import mykotlin.roshanpaturkar.com.keysstand.models.Feedbacks

class FeedbackAdapter(dataBaseQuery: DatabaseReference, var context: Context)
    : FirebaseRecyclerAdapter<Feedbacks, FeedbackAdapter.ViewHolder>(
        Feedbacks::class.java,
        R.layout.feedback_row,
        FeedbackAdapter.ViewHolder::class.java,
        dataBaseQuery
) {

    override fun populateViewHolder(viewHolder: ViewHolder?, feedbacks: Feedbacks?, position: Int) {
        viewHolder!!.bindView(feedbacks!!, context)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var description: String? = null
        var issue: String? = null
        var name: String? = null
        var user_id: String? = null

        fun bindView(feedbacks: Feedbacks?, context: Context) {

            var nameView = itemView.findViewById<TextView>(R.id.nameViewFeedback)
            var idView = itemView.findViewById<TextView>(R.id.idViewFeedback)
            var issueView = itemView.findViewById<TextView>(R.id.issueViewFeedback)
            var descriptionView = itemView.findViewById<TextView>(R.id.describtionViewFeedback)

            name = feedbacks!!.name
            user_id = feedbacks!!.user_id
            issue = feedbacks!!.issue
            description = feedbacks!!.description

            nameView.text = feedbacks.name
            idView.text = feedbacks.user_id
            issueView.text = feedbacks.issue
            descriptionView.text = feedbacks.description

        }

    }
}
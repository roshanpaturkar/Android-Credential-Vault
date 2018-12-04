package mykotlin.roshanpaturkar.com.keysstand.models

class Feedbacks() {
    var description: String? = null
    var issue: String? = null
    var name: String? = null
    var user_id: String? = null

    constructor(description: String, issue: String, name: String, user_id: String): this(){
        this.name = name
        this.user_id = user_id
        this.issue = issue
        this.description = description
    }
}
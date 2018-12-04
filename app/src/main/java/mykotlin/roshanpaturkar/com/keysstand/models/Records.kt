package mykotlin.roshanpaturkar.com.keysstand.models

class Records() {

    var user_name: String? = null
    var password: String? = null

    constructor(user_name: String, password: String): this(){
        this.user_name = user_name
        this.password = password
    }
}
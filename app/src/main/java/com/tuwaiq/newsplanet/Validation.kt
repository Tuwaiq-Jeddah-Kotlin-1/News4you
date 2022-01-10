package com.tuwaiq.newsplanet

    fun notValid(
        username : String,
        email : String,
        password : String,
        phoneNumber : String
    ) : Boolean {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()){
            return true
        }
        return false
    }

    fun validateEmail(email : String) : Boolean{
    if(email.isEmpty()){
        return true
    }
    return false
    }

    fun validatePass(password: String) : Boolean{
    if(password.isEmpty()){
        return true
    }
    return false
    }



package com.tuwaiq.newsplanet.util


/* this is a generic class is to defines successful response and failure response and errors response ..
also will handle the loading state fot the responses ..
and it's sealed to defined what class can inherit from it ..*/


// this class will wrap any network response in my app ..
sealed class Resource<T>(
// this is the body of our response ..
    val data: T? = null,


// this is the message of the response ..
    val message: String? = null
) {

    // these are the classes that will inherit from the Resource class and handel the different situations ..
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
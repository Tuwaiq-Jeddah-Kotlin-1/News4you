package com.tuwaiq.newsplanet

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

     private  var username1 ="hani"
     private  var email1 ="gggg@gmail.com"
     private  var phone1 ="0505509439"
     private  var password1 ="12345678"

    private  var username2 ="badr"
    private  var email2 ="gggg@gmail.com"
    private  var phone2 =""
    private  var password2 =""

    private var email = ""
    private var pass = ""

    @Test
    fun `all field filled`(){
        assertFalse(notValid(username1 ,email1 ,  password1 , phone1))
    }
    @Test
    fun `some filed not filled`(){
        assertTrue(notValid(username2 ,email2 ,  password2 , phone2))
    }

    @Test
    fun `email is empty`(){
        assertTrue(validateEmail(email))
    }

    @Test
    fun `pass is empty`(){
        assertTrue(validateEmail(pass))
    }
}
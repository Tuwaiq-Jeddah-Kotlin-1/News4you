package com.tuwaiq.newsplanet.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import kotlinx.android.synthetic.main.bottom_sheet_update.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileFragment() : Fragment( R.layout.profile_fragment) {

    lateinit var updateBBtn : Button
    lateinit var signOutButton : Button
    private lateinit var pref : SharedPreferences
    lateinit var viewModel: NewsViewModel
    lateinit var usernameBET : TextInputEditText
    lateinit var phoneNumberBET : TextInputEditText

    val userCollectionRef = Firebase.firestore.collection("users")
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to access the activity's ViewModel ..
        viewModel = (activity as NewsActivity).viewModel



        signOutButton = view.findViewById(R.id.btn_logout)


        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = db.collection("users").document("$userUID")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<User>()
            usernameTV.text = user!!.username
            emailTV.text = user.email
            phoneNumberTV.text = user.phoneNumber
        }



        btn_update.setOnClickListener {
            bottomSheetToUpdate()
        }

        signOutButton.setOnClickListener {
            pref =
                this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = pref.edit()
            editor.clear()
            editor.apply()
            // Logout from app
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
        }
    }

    // this block of code acts like liveData .. This will work perfectly with our events RV
    fun subscribeToRealtimeUpdate() {
        userCollectionRef.addSnapshotListener { querySnapshot, FirebaseFirestoreException ->
            FirebaseFirestoreException?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val strBuilder = StringBuilder()
                for (document in it.documents) {
                    val user = document.toObject(User::class.java)
                    strBuilder.append("$user\n")
                }
            }
        }
    }

    fun retriveUser() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = userCollectionRef.get().await()
            val strBuilder = StringBuilder()
            for (document in querySnapshot.documents) {
                val user = document.toObject(User::class.java)
                strBuilder.append("$user\n")
                usernameTV.text = user!!.username
                emailTV.text = user.email
                phoneNumberTV.text = user.phoneNumber
            }
            withContext(Dispatchers.Main) {

            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateInFirestore(newUsername: String, newPhoneNumber: String) {
        val UID = FirebaseAuth.getInstance().currentUser?.uid
        val collectionRef = Firebase.firestore.collection("users")
        collectionRef.document(UID.toString()).update("username", newUsername, "phoneNumber", newPhoneNumber)
    }



    @SuppressLint("InflateParams")
    private fun bottomSheetToUpdate() {

        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_update, null)

        val builder = BottomSheetDialog(requireView().context as Context)


        updateBBtn = view.findViewById(R.id.updateBBtn)
        usernameBET = view.findViewById(R.id.usernameBET)
        phoneNumberBET = view.findViewById(R.id.phoneNumberBET)


        usernameBET.setText(usernameTV.text.toString())
        phoneNumberBET.setText(phoneNumberTV.text.toString())

        updateBBtn.setOnClickListener {
            if (usernameBET.text.toString().isNotEmpty() &&
                phoneNumberBET.text.toString().isNotEmpty() &&
                phoneNumberBET.text.toString().length == 10
            ) {
                usernameTV.setText(usernameBET.text.toString())
                updateInFirestore("${usernameBET.text.toString()}", "${phoneNumberBET.text.toString()}")
                builder.dismiss()

            } else {
                Toast.makeText(context , "There is a problem" , Toast.LENGTH_LONG).show()
            }
        }
        builder.setContentView(view)
        builder.show()
    }
}

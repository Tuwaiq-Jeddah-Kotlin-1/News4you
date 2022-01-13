package com.tuwaiq.newsplanet.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
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
import com.tuwaiq.newsplanet.ui.SplashScreenActivity
import com.tuwaiq.newsplanet.util.LangSetting
import kotlinx.android.synthetic.main.bottom_sheet_update.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class ProfileFragment() : Fragment(R.layout.profile_fragment) {

    lateinit var updateBBtn: Button
    lateinit var signOutButton: Button
    private lateinit var pref: SharedPreferences
    lateinit var viewModel: NewsViewModel
    lateinit var usernameBET: TextInputEditText
    lateinit var phoneNumberBET: TextInputEditText
    lateinit var profileSharedPreferance: SharedPreferences
    lateinit var langSetting : LangSetting

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        langSetting = LangSetting(requireContext())
        profileSharedPreferance =
            this.requireActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to access the activity's ViewModel ..
        viewModel = (activity as NewsActivity).viewModel

        val isDarkMode = profileSharedPreferance.getBoolean("DARKMODE", false)
        val profileSharedEditor: SharedPreferences.Editor = profileSharedPreferance.edit()

        signOutButton = view.findViewById(R.id.btn_logout)

        usernameTV.text = profileSharedPreferance.getString("USERNAME", "")!!
        emailTV.text = profileSharedPreferance.getString("EMAIL", "")!!
        phoneNumberTV.text = profileSharedPreferance.getString("PHONENUMBER", "")!!

        var language: String = "en"

        switch2.isChecked = profileSharedPreferance.getBoolean("DARKMODE", false)

        switch2.setOnClickListener {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                profileSharedEditor.putBoolean("DARKMODE", false)
                profileSharedEditor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                profileSharedEditor.putBoolean("DARKMODE", true)
                profileSharedEditor.apply()
            }
        }

        englishBtn.setOnClickListener {
            language = "en"
            val editor: SharedPreferences.Editor = profileSharedPreferance.edit()
            editor.putString("LANGUAGE", "en")
            editor.apply()
            setLocales(language)
            recreate(context as Activity)
        }

        arabicBtn.setOnClickListener {
            language = "ar"
            val editor: SharedPreferences.Editor = profileSharedPreferance.edit()
            editor.putString("LANGUAGE", "ar")
            editor.apply()
            setLocales(language)
            recreate(context as Activity)
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

    private fun updateInFirestore(newUsername: String, newPhoneNumber: String) {
        val UID = FirebaseAuth.getInstance().currentUser?.uid
        val collectionRef = Firebase.firestore.collection("users")
        collectionRef.document(UID.toString())
            .update("username", newUsername, "phoneNumber", newPhoneNumber)
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
                updateInFirestore(
                    "${usernameBET.text.toString()}",
                    "${phoneNumberBET.text.toString()}"
                )
                builder.dismiss()

            } else {
                Toast.makeText(context, "There is a problem", Toast.LENGTH_LONG).show()
            }
        }
        builder.setContentView(view)
        builder.show()
    }


    private fun setLocales(language: String) {
        langSetting.setLocals(language)
    }
}

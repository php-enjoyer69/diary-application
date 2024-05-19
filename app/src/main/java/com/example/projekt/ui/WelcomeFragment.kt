package com.example.projekt.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projekt.R
import dagger.hilt.android.AndroidEntryPoint

//fragment that welcomes the user
@AndroidEntryPoint
class WelcomeFragment: Fragment(R.layout.fragment_welcome) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAuthenticate = view.findViewById<Button>(R.id.btn_authenticate)

        //enter the application
        btnAuthenticate.setOnClickListener {
            val action = WelcomeFragmentDirections.actionAuthenticationFragmentToNoteFragment()
            findNavController().navigate(action)
        }


    }


}
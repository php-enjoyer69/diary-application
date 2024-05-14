package com.example.projekt.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projekt.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment: Fragment(R.layout.fragment_authentication) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAuthenticate = view.findViewById<Button>(R.id.btn_authenticate)

                btnAuthenticate.setOnClickListener {
                    val action = AuthenticationFragmentDirections.actionAuthenticationFragmentToNoteFragment()
                    findNavController().navigate(action)
                }


    }


}
package com.example.easychat.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.easychat.R
import com.example.easychat.databinding.FragmentProfileBinding
import com.example.easychat.model.UserModel
import com.example.easychat.splash.MainActivity
import com.example.easychat.utils.FirebaseUtil
import com.google.firebase.messaging.FirebaseMessaging

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser : UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        getAndSetInfo()
        initListeners()
    }

    private fun initListeners() {
        binding.btnUpdate.setOnClickListener {

            val newUsername = binding.etUsername.text.toString().trim()

            if (newUsername.isEmpty() || newUsername.length<=4 ){
                binding.etUsername.error = getString(R.string.error_name)
                return@setOnClickListener
            }
            setInProgress(true)
            currentUser.username = newUsername
            updateToFirestore()
        }

        binding.tvLogout.setOnClickListener {

            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
                if (it.isSuccessful){
                    FirebaseUtil().logout()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

    }

    private fun updateToFirestore() {
        FirebaseUtil().currentUserDetails().set(currentUser)
            .addOnCompleteListener {
                setInProgress(false)
                if (it.isSuccessful){
                    Toast.makeText(requireContext(), getString(R.string.ready), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getAndSetInfo() {
        FirebaseUtil().currentUserDetails().get().addOnCompleteListener {

            setInProgress(false)
            if (it.isSuccessful){

                currentUser = it.result.toObject(UserModel::class.java)!!

                binding.etUsername.setText(currentUser.username)
                binding.etPhoneNumber.setText(currentUser.phone)
            }
        }
    }

    private fun setInProgress(it : Boolean){
        binding.pbUpdate.isVisible = it
        binding.btnUpdate.isEnabled = !it
    }

}
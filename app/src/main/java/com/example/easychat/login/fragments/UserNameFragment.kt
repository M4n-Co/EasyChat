package com.example.easychat.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.example.easychat.databinding.FragmentUserNameBinding
import java.util.regex.Pattern

class UserNameFragment : Fragment() {

    private var _binding : FragmentUserNameBinding? = null
    private val binding get() = _binding!!

    private val args : UserNameFragmentArgs by navArgs()
    private val mPhone get() = args.phone

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

    }

    private fun initUI() {
        initListeners()
        initTextChanges()
    }

    private fun initTextChanges() {
        binding.etUsername.doAfterTextChanged {
            binding.etUsername.error = null
        }
    }

    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
        val username = binding.etUsername.text.toString().toString()
            if (username.length>=5){

            }else{
                binding.etUsername.error = "Formato incorrecto"
            }
        }
    }

    private fun setInProgress(it : Boolean){
        binding.pbUserName.isVisible = it
        binding.btnEnter.isEnabled = !it
    }
}
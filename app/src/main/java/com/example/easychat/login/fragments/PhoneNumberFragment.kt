package com.example.easychat.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.easychat.R
import com.example.easychat.databinding.FragmentPhoneNumberBinding

class PhoneNumberFragment : Fragment() {

    private var _binding : FragmentPhoneNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInProgress(false)
        initUI()

    }

    private fun initUI() {
        initListeners()
        initTextChangedListener()
    }

    private fun initTextChangedListener() {
        binding.etPhoneNumber.doAfterTextChanged {
            binding.etPhoneNumber.error = null
        }
    }

    private fun initListeners() {

        binding.ccpCodePhone.setOnCountryChangeListener{
            binding.ccpCodePhone.registerCarrierNumberEditText(binding.etPhoneNumber)
        }

        binding.ccpCodePhone.registerCarrierNumberEditText(binding.etPhoneNumber)

        binding.btnSendOTP.setOnClickListener {

            if (!binding.ccpCodePhone.isValidFullNumber){
                binding.etPhoneNumber.error = getString(R.string.error_name)
                binding.etPhoneNumber.requestFocus()
            }else{
                setInProgress(true)
                val phone = binding.ccpCodePhone.fullNumberWithPlus
                findNavController().navigate(
                    PhoneNumberFragmentDirections.actionPhoneNumberFragmentToOtpFragment(
                        phone.toString()
                    )
                )
                setInProgress(false)
            }

        }
    }

    private fun setInProgress(it : Boolean){
        binding.pbPhoneNumber.isVisible = it
        binding.btnSendOTP.isEnabled = !it
    }

}
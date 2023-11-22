package com.example.easychat.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.easychat.databinding.FragmentOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OtpFragment : Fragment() {

    private var _binding : FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private val args : OtpFragmentArgs by navArgs()
    private val mPhone : String get() = args.phone

    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var mVerificationCode : String
    private lateinit var mForceResendingToken: ForceResendingToken

    private val callbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            binding.pbOTP.isVisible = false
            signIn(phoneAuthCredential)
        }
        override fun onVerificationFailed(firebaseException: FirebaseException) {
            binding.pbOTP.isVisible = false
            Toast.makeText(requireContext(), "Error de verificacion", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationCode: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(verificationCode, forceResendingToken)
            mVerificationCode = verificationCode
            mForceResendingToken = forceResendingToken

            binding.pbOTP.isVisible = false

            Toast.makeText(requireContext(), "Codigo enviado", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        requestOTP(mPhone, false)
        initListeners()
    }

    private fun requestOTP(phone : String, isResend : Boolean) {

        startResendOTP()

        binding.pbOTP.isVisible = true

        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(mForceResendingToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }

    }

    private fun startResendOTP() {
        binding.tvResend.isEnabled = false

        val timer = Timer()

        timer.scheduleAtFixedRate(
            TimerTask().run{

            },0,1000)
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val enterOtp = binding.etOTP.text.toString().trim()

            val credential = PhoneAuthProvider.getCredential(mVerificationCode, enterOtp)
            signIn(credential)
            binding.pbOTP.isVisible = true
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        binding.pbOTP.isVisible = true

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener{result ->
            if (result.isSuccessful){
                binding.pbOTP.isVisible = false
                findNavController().navigate(
                    OtpFragmentDirections.actionOtpFragmentToUserNameFragment(mPhone)
                )
            }else{
                Toast.makeText(requireContext(), "Error de autentificacion", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


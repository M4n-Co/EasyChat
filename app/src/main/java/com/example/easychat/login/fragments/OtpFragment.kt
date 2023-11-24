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
import com.example.easychat.R
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

    private var mTimeoutSeconds = 60L

    private lateinit var mVerificationCode : String
    private lateinit var mForceResendingToken: ForceResendingToken

    private lateinit var mTimer : Timer

    private val callbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            setInProgress(false)
            signIn(phoneAuthCredential)
        }
        override fun onVerificationFailed(firebaseException: FirebaseException) {
            Toast.makeText(requireContext(), getString(R.string.verification_error), Toast.LENGTH_SHORT).show()
            setInProgress(false)
        }

        override fun onCodeSent(verificationCode: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(verificationCode, forceResendingToken)
            mVerificationCode = verificationCode
            mForceResendingToken = forceResendingToken

            setInProgress(false)
            Toast.makeText(requireContext(), getString(R.string.code_sent), Toast.LENGTH_SHORT).show()
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

        setInProgress(true)

        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phone)
            .setTimeout(mTimeoutSeconds, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(mForceResendingToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }

    }

    private fun startResendOTP() {
        setInProgress(false)

        mTimer = Timer()
        mTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                mTimeoutSeconds--
                val resendOtp = "${getString(R.string.resend_otp)} $mTimeoutSeconds ${getString(R.string.sec)}"
                binding.tvResend.text = resendOtp
                if (mTimeoutSeconds <= 0) {
                    mTimeoutSeconds = 60L
                    mTimer.cancel()
                    binding.tvResend.isEnabled = true
                }
            }
        }, 0, 1000)

    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val enterOtp = binding.etOTP.text.toString().trim()

            val credential = PhoneAuthProvider.getCredential(mVerificationCode, enterOtp)
            signIn(credential)
            setInProgress(true)
        }

        binding.tvResend.setOnClickListener {
            requestOTP(mPhone, true)
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        setInProgress(true)

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener{result ->
            if (result.isSuccessful){
                setInProgress(false)
                mTimer.cancel()
                findNavController().navigate(
                    OtpFragmentDirections.actionOtpFragmentToUserNameFragment(mPhone)
                )
            }else{
                Toast.makeText(requireContext(), getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setInProgress(it : Boolean){
        binding.pbOTP.isVisible = it
        binding.btnNext.isEnabled = !it
    }
}


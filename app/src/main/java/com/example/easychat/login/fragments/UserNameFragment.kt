package com.example.easychat.login.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.example.easychat.databinding.FragmentUserNameBinding
import com.example.easychat.home.activity.HomeActivity
import com.example.easychat.login.model.UserModel
import com.example.easychat.utils.FirebaseUtil
import com.google.firebase.Timestamp

class UserNameFragment : Fragment() {

    private var _binding : FragmentUserNameBinding? = null
    private val binding get() = _binding!!

    private val args : UserNameFragmentArgs by navArgs()
    private val mPhone get() = args.phone

    private  var mUserModel : UserModel? = null

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
        getUsername()
        initListeners()
        initTextChanges()
    }

    private fun setUsername(){
        val username = binding.etUsername.text.toString().trim()

        if (username.isEmpty() || username.length<=5 ){
            binding.etUsername.error = "Formato incorrecto"
            return
        }

        setInProgress(true)

        if (mUserModel != null){
            mUserModel!!.username = username
        }else{
            mUserModel = UserModel(mPhone,username, Timestamp.now())
        }

        if (mUserModel != null){
            FirebaseUtil().currentUserDetails().set(mUserModel!!).addOnCompleteListener{
                setInProgress(false)
                if (it.isSuccessful){
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    private fun getUsername() {
        setInProgress(true)

        FirebaseUtil().currentUserDetails().get().addOnCompleteListener{
            setInProgress(false)
            if(it.isSuccessful){
                mUserModel = it.result.toObject(UserModel::class.java)
                if (mUserModel!=null){
                    binding.etUsername.setText(mUserModel!!.username)
                }
            }
        }
    }

    private fun initTextChanges() {
        binding.etUsername.doAfterTextChanged {
            binding.etUsername.error = null
        }
    }

    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
            setUsername()
        }
    }

    private fun setInProgress(it : Boolean){
        binding.pbUserName.isVisible = it
        binding.btnEnter.isEnabled = !it
    }
}
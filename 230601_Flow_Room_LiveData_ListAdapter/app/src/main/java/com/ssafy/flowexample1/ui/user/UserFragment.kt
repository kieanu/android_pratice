package com.ssafy.flowexample1.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.ssafy.flowexample1.database.UserDto
import com.ssafy.flowexample1.databinding.FragmentUserBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentUserBinding
    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(requireActivity())
    }

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = userListAdapter

        // 뷰모델 LiveData 갱신
        userViewModel.userList.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it.toMutableList()) // listAdapter 객체 갱신
        }
        setUi()
    }

    private fun setUi() {
        binding.addButton.setOnClickListener {
            val user = setUserDto(binding.nameEditText,binding.ageEditText,binding.introduceEditText,binding.avatarEditText)
            if(user != null) userViewModel.addUser(user)
        }
        binding.updateButton.setOnClickListener {
            val userId = userListAdapter.selectedUser?.id ?: 0
            val user = setUserDto(binding.updatedNameEditText,binding.updatedAgeEditText,binding.updatedIntroduceEditText,binding.updatedAvatarEditText,userId)
            Log.d("싸피 업데이트 유저", "setUi: $user")
            if(user != null) userViewModel.updateUser(user)
        }
        binding.deleteButton.setOnClickListener {
            val user = userListAdapter.selectedUser
            if(user != null) userViewModel.deleteUser(user)
        }
    }

    private fun setUserDto(name:EditText, age:EditText, introduce:EditText, avatar:EditText, id:Long = 0) : UserDto? {
        val avatarUri:String = binding.avatarEditText.text.toString()
        var user: UserDto? =
        if(avatarUri != "") {
            UserDto(
                id,
                name.text.toString(),
                age.text.toString().toInt(),
                introduce.text.toString(),
                avatar.text.toString()
            )
        } else {
            UserDto(
                id,
                name.text.toString(),
                age.text.toString().toInt(),
                introduce.text.toString(),
                "https://blog.jinbo.net/attach/615/200937431.jpg"
            )
        }
        return user
    }

    override fun onResume() {
        super.onResume()
//        userViewModel.getUsers() -> 버튼 누를때마다 호출됨
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
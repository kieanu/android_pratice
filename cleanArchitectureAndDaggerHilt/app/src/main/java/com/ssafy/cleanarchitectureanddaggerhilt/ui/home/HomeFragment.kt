package com.ssafy.cleanarchitectureanddaggerhilt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ssafy.cleanarchitectureanddaggerhilt.databinding.FragmentHomeBinding
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.base.BaseViewModel
import com.ssafy.cleanarchitectureanddaggerhilt.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)
    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        observe(viewModel.settings, ::onViewStateChange) // viewModel.setting.oberserve(lifecycleOwner) {onViewStateChange ~ 와 동일}
//        viewModel.getSettings()
    }
}
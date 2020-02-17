package com.test.memoapp.ui.memos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.test.memoapp.R
import com.test.memoapp.databinding.MemosFragBinding
import kotlinx.android.synthetic.main.memos_frag.*
import javax.inject.Inject

class MemosFragment : Fragment(){


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MemosViewModel> { viewModelFactory }

    private val args: MemosFragmentArgs by navArgs()

    private lateinit var listAdapter: MemosAdapter

    lateinit var binding : MemosFragBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<MemosFragBinding>(
            inflater,
            R.layout.memos_frag,
            container,
            false
        )
        binding = dataBinding
        //sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        setupListAdapter()

        fab_add_memo.setOnClickListener {
            findNavController().navigate(R.id.action_memosFragment_to_addEditMemoFragment)
        }
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = MemosAdapter(viewModel)
            binding.memosList.adapter = listAdapter
        } else {
            //Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
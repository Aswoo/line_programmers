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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.memoapp.databinding.MemosFragBinding
import com.test.memoapp.di.Injectable
import kotlinx.android.synthetic.main.memos_frag.*
import javax.inject.Inject

class MemosFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MemosViewModel> { viewModelFactory }

    private val args: MemosFragmentArgs by navArgs()

    private lateinit var adapter: MemosAdapter

    lateinit var binding: MemosFragBinding


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

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = MemosAdapter{
            memo ->
            val action = MemosFragmentDirections.actionMemosFragmentToMemoDetailFragment(memo.id)
            findNavController().navigate(action)
        }
        this.adapter = rvAdapter
        binding.memosList.adapter = rvAdapter

        fab_add_memo.setOnClickListener {

            val action = MemosFragmentDirections.actionMemosFragmentToAddEditMemoFragment(null,null)
            findNavController().navigate(action)
        }

    }

    private fun initRecyclerView() {

        binding.viewmodel = viewModel
        binding.memosList.layoutManager = LinearLayoutManager(context)
        viewModel.items.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result)
        })
    }
}
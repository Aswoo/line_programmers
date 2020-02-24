package com.test.memoapp.ui.memos

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.test.memoapp.data.Memo
import com.test.memoapp.databinding.MemosFragBinding
import com.test.memoapp.di.Injectable
import com.test.memoapp.util.RecyclerViewItemDecoration
import com.test.memoapp.util.setupRefreshLayout
import kotlinx.android.synthetic.main.memos_frag.*
import javax.inject.Inject

class MemosFragment : Fragment(), Injectable {


    private val args : MemosFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MemosViewModel> { viewModelFactory }

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
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        if(args.forceUpdate){
            viewModel.updateForce(args.forceUpdate)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("TAG","activityCreated")
        viewModel.updateForce(false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        setupRefreshLayout(binding.refreshLayout, binding.memosList)
        setupFab()
        setupForceRefresh()

        val rvAdapter = MemosAdapter { memo ->
            val action = MemosFragmentDirections.actionMemosFragmentToMemoDetailFragment(memo)
            findNavController().navigate(action)
        }
        this.adapter = rvAdapter
        binding.memosList.adapter = rvAdapter

    }

    private fun setupFab() {
        fab_add_memo.setOnClickListener {
            val action =
                MemosFragmentDirections.actionMemosFragmentToAddEditMemoFragment(Memo("","",
                    emptyList(),""))
            findNavController().navigate(action)
        }
    }
    private fun setupForceRefresh(){
        viewModel.forceUpdate.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.refresh()
            }
        })
    }

    private fun initRecyclerView() {

        binding.viewmodel = viewModel
        binding.memosList.layoutManager = LinearLayoutManager(context)
        binding.memosList.addItemDecoration(RecyclerViewItemDecoration(context))

        viewModel.items.observe(viewLifecycleOwner, Observer {
                result ->
            adapter.submitList(result)
        })


    }
}
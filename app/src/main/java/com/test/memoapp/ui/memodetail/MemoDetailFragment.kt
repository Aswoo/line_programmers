package com.test.memoapp.ui.memodetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.memoapp.R
import androidx.lifecycle.Observer
import com.test.memoapp.databinding.MemodetailFragBinding
import com.test.memoapp.di.Injectable
import com.test.memoapp.ui.addeditmemo.AddEditMemoAdapter
import javax.inject.Inject

class MemoDetailFragment : Fragment(),Injectable{

    private lateinit var binding: MemodetailFragBinding

    private val args: MemoDetailFragmentArgs by navArgs()

    private lateinit var adapter: MemoDetailAdapter


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MemoDetailViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.memodetail_frag, container, false)
        binding = MemodetailFragBinding.bind(view).apply {
            viewmodel = viewModel
        }
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.load(args.memoId)
        setupFab()
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val rvAdapter = MemoDetailAdapter()
        this.adapter = rvAdapter
        binding.imageList.adapter = rvAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                //viewModel.deleteTask()
                true
            }
            else -> false
        }
    }
    private fun setupFab() {
        activity?.findViewById<View>(R.id.fab_add_memo)?.setOnClickListener {

            val action = MemoDetailFragmentDirections
                .actionMemoDetailFragmentToAddEditMemoFragment(
                    null,
                    memoId = args.memoId)
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memodetail_fragment_menu, menu)
    }

    private fun initRecyclerView() {

        binding.viewmodel = viewModel
        binding.imageList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL ,false)

        viewModel.imagePathList.observe(viewLifecycleOwner, Observer { result ->

            adapter.submitList(result)
        })


    }

}
package com.test.memoapp.ui.memodetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.memoapp.EventObserver
import com.test.memoapp.R
import com.test.memoapp.databinding.MemodetailFragBinding
import com.test.memoapp.di.Injectable
import com.test.memoapp.util.HorizentalRecyclerViewItemDecoration
import javax.inject.Inject

class MemoDetailFragment : Fragment(), Injectable {

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
        viewModel.load(args.memo.id)
        setupFab()
        setHasOptionsMenu(true)
        setupNavigation()
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
                viewModel.deleteMemo()
                true
            }
            else -> false
        }
    }

    private fun setupFab() {
        binding.fabEditMemo.setOnClickListener {
            viewModel.editMemo()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memodetail_fragment_menu, menu)
    }

    private fun initRecyclerView() {

        binding.viewmodel = viewModel
        binding.imageList.addItemDecoration(HorizentalRecyclerViewItemDecoration(context))
        binding.imageList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )

        viewModel.imagePathList.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result)
        })
    }
    private fun setupNavigation() {
        viewModel.deleteMemoCommand.observe(viewLifecycleOwner, EventObserver {
            val action = MemoDetailFragmentDirections
                .actionMemoDetailFragmentToMemosFragment(true)
            findNavController().navigate(action)
        })
        viewModel.editMemoCommand.observe(viewLifecycleOwner, EventObserver {
            val action = MemoDetailFragmentDirections
                .actionMemoDetailFragmentToAddEditMemoFragment(viewModel.memo.value!!)
            findNavController().navigate(action)
        })
    }

}
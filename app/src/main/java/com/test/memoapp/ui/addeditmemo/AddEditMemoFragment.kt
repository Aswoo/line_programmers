package com.test.memoapp.ui.addeditmemo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.memoapp.R
import com.test.memoapp.data.Memo
import com.test.memoapp.databinding.AddmemoFragBinding
import com.test.memoapp.di.Injectable
import com.test.memoapp.util.setupSnackbar
import kotlinx.android.synthetic.main.addmemo_frag.*
import java.util.logging.Logger
import javax.inject.Inject

class AddEditMemoFragment : Fragment(),Injectable{

    private lateinit var binding: AddmemoFragBinding

    private val args: AddEditMemoFragmentArgs by navArgs()

    private lateinit var adapter: AddEditMemoAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddEditMemoViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.addmemo_frag, container, false)
        binding = AddmemoFragBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val rvAdapter = AddEditMemoAdapter{
            viewModel.removeImagePath(it)
        }
        this.adapter = rvAdapter

        binding.memoImageList.adapter = rvAdapter

        args?.run {

            if (images != null) {
                viewModel.addImagePath(images)
            }

        }
        fab_save_memo.setOnClickListener {

            val title = add_memo_title.text.toString()
            val description = add_memo_description.text.toString()
            viewModel.saveMemo(Memo(title,description))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        Log.e("MEMOID",args.memoId.toString())
        viewModel.start(args.memoId)

    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {
                //viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_attach -> {
                val action = AddEditMemoFragmentDirections.actionAddEditMemoFragmentToActionBottomSheetFragment(args.memoId)
                findNavController().navigate(action)
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addeditmemo_fragment_menu, menu)
    }

    companion object {
        private const val REQUEST_PICK_PHOTO = 1
    }
    private fun initRecyclerView() {

        binding.viewmodel = viewModel
        binding.memoImageList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL ,false)

        viewModel.imagePathList.observe(viewLifecycleOwner, Observer { result ->

            adapter.submitList(result)
        })
    }


}
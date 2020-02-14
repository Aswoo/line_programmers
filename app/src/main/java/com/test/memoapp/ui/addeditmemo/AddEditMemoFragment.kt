package com.test.memoapp.ui.addeditmemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.test.memoapp.R
import com.test.memoapp.databinding.AddmemoFragBinding

class AddEditMemoFragment : Fragment(){



    private lateinit var viewDataBinding: AddmemoFragBinding
    /*
    private val args: AddEditTaskFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddEditTaskViewModel> { viewModelFactory }


     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.addmemo_frag, container, false)
        viewDataBinding = AddmemoFragBinding.bind(root).apply {
            //this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupNavigation()
        /*
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        viewModel.start(args.taskId)

         */
    }

    private fun setupSnackbar() {
        //view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        /*
        viewModel.taskUpdatedEvent.observe(this, EventObserver {
            val action = AddEditTaskFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })

         */
    }

}
package com.test.memoapp.ui.memos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.test.memoapp.R
import com.test.memoapp.databinding.MemosFragBinding
import kotlinx.android.synthetic.main.memos_frag.*

class MemosFragment : Fragment(){


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

        fab_add_memo.setOnClickListener {
            //findNavController().navigate
        }
    }
}
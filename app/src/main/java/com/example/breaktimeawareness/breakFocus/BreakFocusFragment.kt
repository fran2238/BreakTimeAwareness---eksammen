package com.example.breaktimeawareness.breakFocus

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.breaktimeawareness.R
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.database.BreakTimeDatabaseDao
import com.example.breaktimeawareness.databinding.BreakFocusFragmentBinding

class BreakFocusFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : BreakFocusFragmentBinding =DataBindingUtil.inflate(
            inflater, R.layout.break_focus_fragment, container, false
        )
        val application = requireNotNull(this.activity).application

        val arguments = BreakFocusFragmentArgs.fromBundle(requireArguments())


        val dataSource = BreakTimeDatabase.getInstance(application).breakTimeDatabaseDao

        val viewModelFactory = BreakFocusViewModelFactory(arguments.breakTimeKey, dataSource)

        val breakFocusViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(BreakFocusViewModel::class.java)

        binding.breakTimeFocusViewModel = breakFocusViewModel


        breakFocusViewModel.navigateToOverview.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(
                BreakFocusFragmentDirections.actionBreakFocusFragmentToBreakOverviewFragment())

                breakFocusViewModel.doneNavigating()
            }
        }

        )
        return binding.root
    }



}
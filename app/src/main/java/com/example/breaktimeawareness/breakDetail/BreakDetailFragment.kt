package com.example.breaktimeawareness.breakDetail

import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.breaktimeawareness.R
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.databinding.BreakDetailFragmentBinding

class BreakDetailFragment : Fragment() {





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  binding : BreakDetailFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.break_detail_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = BreakDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = BreakTimeDatabase.getInstance(application).breakTimeDatabaseDao
        val viewModelFactory = BreakDetailViewModelFactory(arguments.breakTimeKey, dataSource)

        val breakDetailViewModel =
                ViewModelProviders.of(
this, viewModelFactory).get(BreakDetailViewModel::class.java)

        binding.breakDetailViewModel = breakDetailViewModel

        binding.setLifecycleOwner(this)


breakDetailViewModel.navigateToOverView.observe(this.viewLifecycleOwner, Observer {

    if (it == true){
        this.findNavController().navigate(
            BreakDetailFragmentDirections.actionBreakDetailFragmentToBreakOverviewFragment())
            breakDetailViewModel.doneNavigating()
    }
})
        return binding.root
    }




}
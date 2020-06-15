package com.example.breaktimeawareness.breakOverview

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breaktimeawareness.R
import com.example.breaktimeawareness.database.BreakTimeDatabase
import com.example.breaktimeawareness.databinding.BreakOverviewFragmentBinding
import com.google.android.material.snackbar.Snackbar

class BreakOverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BreakOverviewFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.break_overview_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BreakTimeDatabase.getInstance(application).breakTimeDatabaseDao

        val viewModelFactory = BreakTimeViewModelFactory(dataSource, application)

        val breakTimeViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(BreakOverviewViewModel::class.java)
        binding.breakTImeViewModel = breakTimeViewModel

        binding.setLifecycleOwner(this)



        // SnackBar Observer for message on delete
        breakTimeViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                breakTimeViewModel.doneShowingSnackbar()
            }
        })

        breakTimeViewModel.navigateToBreakDetail.observe(this.viewLifecycleOwner, Observer { breakTime ->
            breakTime?.let {
                findNavController().navigate(
                    BreakOverviewFragmentDirections.actionBreakOverviewFragmentToBreakDetailFragment(breakTime))
                breakTimeViewModel.onBreakDetailNavigated()

            }
        })
        breakTimeViewModel.navigateToFocusQuality.observe(this.viewLifecycleOwner, Observer { breakTime ->
            breakTime?.let {
                findNavController().navigate(
                    BreakOverviewFragmentDirections.actionBreakOverviewFragmentToBreakFocusFragment(
                        breakTime.breakTimeId
                    )
                )
                breakTimeViewModel.doneNavigating()
            }
        })
        breakTimeViewModel.buzzEvent.observe(this.viewLifecycleOwner, Observer {buzzed ->
            if (buzzed != BreakOverviewViewModel.BuzzType.NO_BUZZ){
            buzz(buzzed.pattern)
                if (buzzed == BreakOverviewViewModel.BuzzType.FIRST){
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.first_warning),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                }
                if (buzzed == BreakOverviewViewModel.BuzzType.SECOND){
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.second_warning),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                }
                if (buzzed == BreakOverviewViewModel.BuzzType.FINAL){
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.final_warning),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                }
                breakTimeViewModel.onBuzzComplete()
            }

        })

        // recyclerView

        val manager = GridLayoutManager(activity, 1)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int) = when(position) {
                0 -> 1
                else -> 1
            }

        }
        binding.breakList.layoutManager = manager

        val adapter = BreakTimeAdapter(BreakTimeListener {breakTimeId ->
            breakTimeViewModel.onBreakDetailClicked(breakTimeId)
        })
        binding.breakList.adapter =adapter

        breakTimeViewModel.breaks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }


}
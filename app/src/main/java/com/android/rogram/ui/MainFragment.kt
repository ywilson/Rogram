package com.android.rogram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.rogram.R
import com.android.rogram.data.NetworkState
import com.android.rogram.data.RoData
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.android.synthetic.main.recycler_view.*

/**
 * Entry Point
 */
class MainFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: RoViewModel

    private val adapter by lazy {
        RoAdapter(roDataListener)
    }

    private val roDataListener = RoAdapter.OnClickListener { data, card, _ ->
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300.toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300.toLong()
        }
        val imageCardTransition = getString(R.string.image_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(card to imageCardTransition)
        val directions =
            MainFragmentDirections.actionFirstFragmentToSecondFragment(
                data.id,
                data
            )
        findNavController().navigate(directions, extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300.toLong()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.recycler_view, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(RoViewModel::class.java)
        progressBar = rootView.findViewById(R.id.spinner)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        observe()

    }

    private fun observe() {
        viewModel.roData.observe(viewLifecycleOwner, Observer<NetworkState<List<RoData>>> { state ->
            when (state) {
                is NetworkState.Success -> {
                    state.data?.let { adapter.addData(it) }
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
                is NetworkState.Loading -> {}

                is NetworkState.Error -> showError(state)
            }


        })

        viewModel.spinner.observe(viewLifecycleOwner, Observer<Boolean> { res ->
            progressBar.visibility = if (res) View.VISIBLE else View.GONE
        })
    }

    private fun showError(resource: NetworkState<List<RoData>>) {
        error.visibility =
            if (resource.data == null) View.VISIBLE else View.GONE

    }
}
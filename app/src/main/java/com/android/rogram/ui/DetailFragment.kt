package com.android.rogram.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.rogram.R
import com.android.rogram.data.RoData
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Picasso

/**
 * Album Details
 */
class DetailFragment : Fragment() {

    private lateinit var roData: RoData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        roData = DetailFragmentArgs.fromBundle(args).roData

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 300.toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.detailTitle)
        val img = view.findViewById<ImageView>(R.id.detailImg)
        title.text = roData.title
        Picasso.get().load(roData.url).into(img)
    }
}
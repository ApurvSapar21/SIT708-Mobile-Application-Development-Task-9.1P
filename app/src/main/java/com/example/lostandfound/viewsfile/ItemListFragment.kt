package com.example.lostandfound.viewsfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfound.MainActivity
import com.example.lostandfound.R
import com.example.lostandfound.adapter.AdapterItems

class ItemListFragment : Fragment() {

    companion object {
        fun newInstance() = ItemListFragment()
    }

    var adapterItems : AdapterItems? = null
    var itemsRecycler : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_item_list, container, false)
        val a = this.requireActivity() as MainActivity

        /**
         * Init adapter and recyclerView
         * Set data to UI
         */
        itemsRecycler = rootView.findViewById(R.id.itemRecyclerView)

        val layoutManager = LinearLayoutManager(this@ItemListFragment.context)
        itemsRecycler!!.layoutManager = layoutManager

        adapterItems = AdapterItems(a.readData(), this@ItemListFragment.requireContext())
        itemsRecycler!!.adapter = adapterItems

        // Check if data is empty
        val noDataTv = rootView.findViewById<TextView>(R.id.noData)
        if (a.readData().isEmpty()) {
            noDataTv.visibility = View.VISIBLE
            itemsRecycler!!.visibility = View.GONE
        } else {
            itemsRecycler!!.visibility = View.VISIBLE
            noDataTv.visibility = View.GONE
        }

        return rootView
    }

    // BackPress
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Let's handle onClick back btn
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.container, StartFragment.newInstance()).commitNow()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
package com.example.lostandfound.viewsfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.lostandfound.MainActivity
import com.example.lostandfound.R

class RemoveFragment : Fragment() {

    companion object {
        fun newInstance() = RemoveFragment()
    }

    private var removeID: String? = ""
    private var txtDesc: String? = ""
    private var txtDate: String? = ""
    private var txtPhone: String? = ""
    private var txtName: String? = ""
    private var txtLocation: String? = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_remove, container, false)

        removeID = arguments?.getString("itemId")
        txtName = arguments?.getString("itemName")
        txtDesc = arguments?.getString("itemDesc")
        txtLocation = arguments?.getString("itemLocation")
        txtDate = arguments?.getString("itemDate")
        txtPhone = arguments?.getString("itemPhone")

        val description = rootView.findViewById<TextView>(R.id.description)
        val dateFoundLost = rootView.findViewById<TextView>(R.id.dateFoundLost)
        val dialNumber = rootView.findViewById<TextView>(R.id.dialNumber)
        val itemOwnerName = rootView.findViewById<TextView>(R.id.itemOwnerName)
        val locationFound = rootView.findViewById<TextView>(R.id.locationFound)
        val removeItemBtn = rootView.findViewById<AppCompatButton>(R.id.removeItemBtn)

        description.text = txtDesc
        dateFoundLost.text = "Date Posted: $txtDate"
        dialNumber.text = "Kindly call: $txtPhone for more details."
        itemOwnerName.text = "Posted By: $txtName"
        locationFound.text = "Location: $txtLocation"

        removeItemBtn.setOnClickListener {
            val a = (this.activity as MainActivity)
            a.removeItem(removeID!!.toLong())
        }

        return rootView
    }
}
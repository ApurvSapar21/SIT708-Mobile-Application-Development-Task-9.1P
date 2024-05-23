package com.example.lostandfound.viewsfile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lostandfound.MainActivity
import com.example.lostandfound.R
import java.util.*


class CreateFragment : Fragment() {

    companion object {
        fun newInstance() = CreateFragment()
    }

    private var m: String? = null
    private var d: String? = null
    private val myCalender: Calendar = Calendar.getInstance()

    private var checkedText : String? = null

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private var placeName: String? = ""
    private var latLng: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)

        placeName = arguments?.getString("placeName")
        latLng = arguments?.getString("latLng")

        val a = this.requireActivity() as MainActivity

        val radioButtonLost = rootView.findViewById<RadioButton>(R.id.radioButtonLost)
        val radioButtonFound = rootView.findViewById<RadioButton>(R.id.radioButtonFound)

        val eName = rootView.findViewById<EditText>(R.id.name)
        val ePhone = rootView.findViewById<EditText>(R.id.phoneNumber)
        val eDescription = rootView.findViewById<EditText>(R.id.description)
        val tDate = rootView.findViewById<TextView>(R.id.datePick)
        val eLocation = rootView.findViewById<TextView>(R.id.myLocation)

        eLocation.text = placeName

        eLocation.setOnClickListener { checkLocationPermission() }

        tDate.setOnClickListener {
            val mYear = myCalender[Calendar.YEAR]
            val mMonth = myCalender[Calendar.MONTH]
            val mDay = myCalender[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this.requireContext(),
                { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    myCalender[year, month] = dayOfMonth
                    m = if (month < 10) {
                        "0" + (month + 1)
                    } else {
                        "" + (month + 1)
                    }
                    d = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        "" + dayOfMonth
                    }
                    val fnD = "$year-$m-$d"
                    tDate.text = fnD
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        val saveBtn = rootView.findViewById<AppCompatButton>(R.id.saveItems)
        saveBtn.setOnClickListener {
            // Is the button now checked?
            if (radioButtonLost.isChecked) {
                checkedText = "Lost"
            } else if (radioButtonFound.isChecked) {
                checkedText = "Found"
            }

            // Other UI
            when {
                TextUtils.isEmpty(eName.text.toString().trim()) -> {
                    eName.error = "Name required!"
                }
                TextUtils.isEmpty(ePhone.text.toString().trim()) -> {
                    ePhone.error = "Phone required!"
                }
                TextUtils.isEmpty(eDescription.text.toString().trim()) -> {
                    eDescription.error = "Description required!"
                }
                TextUtils.isEmpty(eLocation.text.toString().trim()) -> {
                    eLocation.error = "Location required!"
                }
                TextUtils.isEmpty(tDate.text.toString().trim()) -> {
                    tDate.error = "Date required!"
                }
                else -> {
                    val name = eName.text.toString().trim()
                    val phone = ePhone.text.toString().trim()
                    val desc = checkedText + " " + eDescription.text.toString().trim()
                    val myLocation = eLocation.text.toString().trim()
                    val myDate = tDate.text.toString().trim()
                    Log.d("res", "$name : $phone : $desc : $myDate : $myLocation")
                    a.recordToDb(name, phone, desc, myDate, myLocation, latLng)
                }
            }
        }

        return rootView
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this.requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // We can request the permission.
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            // Permission previously granted
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, LocationPickerFragment.newInstance())
                .commitNow()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            // permission was granted, do location-related task you need to do.
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapsFragment.newInstance())
                    .commitNow()
            }
        } else {
            // permission denied! Disable the functionality that depends on this permission.
            Toast.makeText(this.requireContext(), "Location Permission Not Granted.", Toast.LENGTH_SHORT).show()
        }
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
package com.example.lostandfound.viewsfile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lostandfound.R
import kotlin.system.exitProcess


class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)

        val createNewAdvert = rootView.findViewById<View>(R.id.createNewAdvert)
        createNewAdvert.setOnClickListener {
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateFragment.newInstance())
                .commitNow()
        }

        val showLostAndFound = rootView.findViewById<View>(R.id.showLostAndFound)
        showLostAndFound.setOnClickListener {
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, ItemListFragment.newInstance())
                .commitNow()
        }

        val showLostOnMap = rootView.findViewById<View>(R.id.showLostOnMap)
        showLostOnMap.setOnClickListener { checkLocationPermission() }

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
                .replace(R.id.container, MapsFragment.newInstance())
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
                    exitProcess(0)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
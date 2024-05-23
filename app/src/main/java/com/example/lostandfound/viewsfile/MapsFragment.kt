package com.example.lostandfound.viewsfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.lostandfound.MainActivity
import com.example.lostandfound.R


class MapsFragment : Fragment() {

    companion object {
        fun newInstance() = MapsFragment()
    }

    var coordinatesList = ArrayList<String>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        val a = this.requireActivity() as MainActivity

        coordinatesList = a.getMapCoordinates() as ArrayList<String>

        if (coordinatesList.size < 1) {
            Toast.makeText(this.requireContext(), "No Data", Toast.LENGTH_SHORT).show()
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateFragment.newInstance())
                .commitNow()
        } else {
            for (items in coordinatesList) {
                // println(items)
                // Nairobi#lat/lng: (-1.2920659,36.8219462)
                val parts: List<String> = items.split("#")

                val title = parts[0]

                val tempStr = parts[1].split(":")
                val c =  tempStr[1].split(",")

                val latDouble = c[0].replace(" (", "").toDouble()
                val lonDouble = c[1].replace(")", "").toDouble()

                val coordinatesStr = LatLng(latDouble, lonDouble)
                googleMap.addMarker(MarkerOptions().position(coordinatesStr).title(title))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinatesStr))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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
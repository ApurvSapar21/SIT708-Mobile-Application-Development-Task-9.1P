package com.example.lostandfound

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.lostandfound.dbhelper.DbHelper
import com.example.lostandfound.modelfiles.ItemsModel
import com.example.lostandfound.schemefiles.LostFoundContract
import com.example.lostandfound.viewsfile.ItemListFragment
import com.example.lostandfound.viewsfile.RemoveFragment
import com.example.lostandfound.viewsfile.StartFragment

class MainActivity : AppCompatActivity() {

    private val dbHelper = DbHelper(this)
    var dataItemsModel : ArrayList<ItemsModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // start Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StartFragment.newInstance())
                .commitNow()
        }
    }

    fun recordToDb(name:String, phone:String, description:String, date:String, location:String, latLng:String?) {
        /**
         * Insert data into the database by passing a ContentValues object to the insert() method
         * Gets the data repository in write mode
         */
        val db = dbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_NAME, name)
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_PHONE, phone)
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_DESCRIPTION, description)
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_DATE, date)
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_LOCATION, location)
            put(LostFoundContract.LostFoundEntry.COLUMN_NAME_COORDINATES, latLng)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(LostFoundContract.LostFoundEntry.TABLE_NAME, null, values)
        Log.d("insertedRow", newRowId.toString())

        // show items now
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ItemListFragment.newInstance())
            .commitNow()
    }

    fun readData(): ArrayList<ItemsModel> {
        val db = dbHelper.readableDatabase

        /**
         * Define a projection that specifies which columns from the database you will actually
         * use after this query.
         */
        val projection = arrayOf(BaseColumns._ID,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_NAME,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_PHONE,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_DESCRIPTION,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_DATE,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_LOCATION
        )

        // Filter results WHERE "name" = 'example'
        val selection = "${LostFoundContract.LostFoundEntry.COLUMN_NAME_NAME} = ?"
        val selectionArgs = arrayOf("Jones")

        // Sort results in the resulting Cursor
//        val sortOrder = "${LostFoundContract.LostFoundEntry.COLUMN_NAME_DATE} DESC"
        val sortOrder = "${BaseColumns._ID} DESC"

        val cursor = db.query(
            LostFoundContract.LostFoundEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,           // don't group the rows
            null,            // don't filter by row groups
            sortOrder               // The sort order
        )

        /**
         *  For each row, you can read a column's value by calling one of the Cursor get methods,
         *  such as getString() or getLong()
         *  For each of the get methods, you must pass the index position of the column you desire,
         *  which you can get by calling getColumnIndex() or getColumnIndexOrThrow().
         *  When finished iterating through results, call close() on the cursor to release its resources.
         */
//        val itemIds = mutableListOf<Long>()
//        val itemStr = mutableListOf<String>()
        dataItemsModel = ArrayList()
        with(cursor) {
            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                val itemDesc = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_DESCRIPTION))
//                itemIds.add(itemId)
//                itemStr.add(itemDesc)
                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val itemName = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_NAME))
                val itemDesc = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_DESCRIPTION))
                val itemDate = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_DATE))
                val itemLocation = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_LOCATION))
                val itemPhone = getString(getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_PHONE))
                dataItemsModel.add(ItemsModel(itemId, itemName, itemDesc, itemDate, itemLocation, itemPhone))
            }
        }
        cursor.close()
        Log.d("resDataLog1", dataItemsModel.toString())
        return dataItemsModel
    }

    fun intentToDetails(itemId : Long, itemName : String, itemDesc : String, itemLocation : String,
                   itemDate : String, itemPhone : String) {
        val bundle = Bundle()
        bundle.putString("itemId", itemId.toString())
        bundle.putString("itemName", itemName)
        bundle.putString("itemDesc", itemDesc)
        bundle.putString("itemLocation", itemLocation)
        bundle.putString("itemDate", itemDate)
        bundle.putString("itemPhone", itemPhone)
        val transaction = supportFragmentManager.beginTransaction()
        val fragmentTwo = RemoveFragment.newInstance()
        fragmentTwo.arguments = bundle
        transaction.replace(R.id.container, fragmentTwo)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    fun removeItem(removeID: Long) {
        val db = dbHelper.readableDatabase
        // Define 'where' part of query.
        val selection = "${BaseColumns._ID} = $removeID"
        // Issue SQL statement.
        db.delete(LostFoundContract.LostFoundEntry.TABLE_NAME, selection, null)

        // Go back to list
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ItemListFragment.newInstance())
            .commitNow()

    }

    fun getMapCoordinates(): MutableList<String> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_LOCATION,
            LostFoundContract.LostFoundEntry.COLUMN_NAME_COORDINATES
        )

        val sortOrder = "${BaseColumns._ID} DESC"

        val cursor = db.query(
            LostFoundContract.LostFoundEntry.TABLE_NAME,
            projection,             // The array of columns to return (pass null to get all)
            null,
            null,
            null,
            null,
            sortOrder               // The sort order
        )

        val itemCoordinatesStr = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val itemLocationLatLng =
                    getString(
                        getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_LOCATION)
                    ) +"#"+ getString(
                        getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME_COORDINATES)
                    )
                itemCoordinatesStr.add(itemLocationLatLng)
            }
        }

        cursor.close()
        Log.d("resDataCoordinates", itemCoordinatesStr.toString())
        return itemCoordinatesStr
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
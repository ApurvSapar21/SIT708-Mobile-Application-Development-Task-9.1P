package com.example.lostandfound.modelfiles

class ItemsModel {
    var itemId : Long? = null
    var itemName : String? = null
    var itemDescription : String? = null
    var itemDate : String? = null
    var itemLocation : String? = null
    var itemPhone : String? = null

    /**
     * Secondary constructor
     */
    constructor(
        itemId: Long?,
        itemName: String?,
        itemDescription: String?,
        itemDate: String?,
        itemLocation: String?,
        itemPhone: String?
    ) {
        this.itemId = itemId
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.itemDate = itemDate
        this.itemLocation = itemLocation
        this.itemPhone = itemPhone
    }
}
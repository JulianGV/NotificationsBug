package com.globant.notificarionbug.contact

data class ContactMessage(
        var uniqueId : Int,
        var ownerId : String,
        var name : String,
        var messages: ArrayList<String>)
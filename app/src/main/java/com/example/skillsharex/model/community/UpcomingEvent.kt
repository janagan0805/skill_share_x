package com.example.skillsharex.model.community

import com.google.gson.annotations.SerializedName

data class UpcomingEvent(

    @SerializedName("event_id")
    val eventId: String,

    @SerializedName("event_title")
    val eventTitle: String,
    // example: "Android Session"

    @SerializedName("mentor_name")
    val mentorName: String,

    @SerializedName("event_date")
    val eventDate: String,
    // ISO-8601 string

    @SerializedName("platform")
    val platform: String
    // always "Live in-app" (for now)
)

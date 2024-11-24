package com.ob.rfi.api

import com.google.gson.annotations.SerializedName


data class ErrorModel(
    @SerializedName("error")
    var error: Error? = null,
) {

    class Error(
        @SerializedName("error_code")
        var error_code: String? = null,

        @SerializedName("error_message")
        var error_message: String = "",
    )
}

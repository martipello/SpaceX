package com.sealstudios.spacex.objects.queries

class SuccessLaunchQuery {
    companion object {
        fun successLaunchQuery(success: Boolean): Pair<String, Any> {
            return "success" to success
        }
    }
}
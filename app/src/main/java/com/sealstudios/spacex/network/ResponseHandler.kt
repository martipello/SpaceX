package com.sealstudios.spacex.network

import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        print("EXCEPTION $e")
        return when (e) {
            is HttpException -> Resource.error(getErrorMessage(e.code()), null, e.code())
            is NoConnectivityException -> Resource.error(
                e.localizedMessage ?: "No Connection", null,
                ErrorCodes.NO_CONNECTION.code
            )
            is SocketTimeoutException -> Resource.error(
                getErrorMessage(ErrorCodes.SOCKET_TIME_OUT.code), null,
                ErrorCodes.SOCKET_TIME_OUT.code
            )
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null, Int.MAX_VALUE)
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.BAD_REQUEST.code -> "Timeout"
            ErrorCodes.FORBIDDEN.code -> "Unauthorised"
            ErrorCodes.INTERNAL_SERVER.code -> "Not found"
            ErrorCodes.NOT_FOUND.code -> "Not found"
            ErrorCodes.UNAUTHORIZED.code -> "Not found"
            ErrorCodes.SOCKET_TIME_OUT.code -> "Not found"
            else -> "Something went wrong"
        }
    }
}
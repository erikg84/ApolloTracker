package com.example.apollotracker.remote

import retrofit2.Response

sealed class Resource<T>(open val data: T? = null) {
    class Success<T>(override val data: T) : Resource<T>(data)
    class Loading<T>(override val data: T? = null) : Resource<T>(data)
    class Failure<T>(override val data: T? = null, val error: Throwable) : Resource<T>(data)

    companion object {
        private fun <T> Response<T>.toResource(): Resource<T> {
            return try {
                if (this.isSuccessful) {
                    this.body()?.let {
                        Success(it)
                    } ?: Failure(null, Throwable("Empty response body"))
                } else {
                    Failure(null, Throwable(errorBody()?.string() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Failure(null, Throwable(e.message ?: "Error handling response"))
            }
        }

        suspend fun <T> fetchCatching(apiCall: suspend () -> Response<T>): Resource<T> {
            return try {
                apiCall().toResource()
            } catch (e: Exception) {
                Failure(null, e)
            }
        }
    }
}
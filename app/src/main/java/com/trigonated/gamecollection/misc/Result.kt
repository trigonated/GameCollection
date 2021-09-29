package com.trigonated.gamecollection.misc

/**
 * Generic class that represents an operation's status and result. Useful when combined with
 * [kotlinx.coroutines.flow.Flow].
 *
 * This class contains several helpful static functions to create results.
 * @param T The type of data of this operation's result.
 */
data class Result<T>(
    /** The status of the operation. */
    val status: Status,
    /** The data from the result. Typically non-null on successful operations and null otherwise. */
    val data: T?,
    /** The error that occurred. Typically only non-null on failed operations. */
    val error: Error?,
    /** A message associated with the [error] */
    val message: String?
) {

    companion object {
        /**
         * A result from a successful operation.
         * @param data The data from the result.
         */
        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null, null)
        }

        /**
         * A result from a failed operation.
         * @param message A message associated with the error.
         * @param error An error.
         */
        fun <T> error(message: String?, error: Error?): Result<T> {
            return Result(Status.ERROR, null, error, message)
        }

        /**
         * A result from an operation that is still loading.
         */
        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
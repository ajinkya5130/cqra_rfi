package com.ob.rfi.viewmodels

sealed class LoginSealedClass {
    /*data class Success<T>(val signInResponseModel: T): LoginSealedClass<T>()
    data class Failure<T>(val signInResponseModel: ErrorModel): LoginSealedClass<T>()
    data class Message<T>(val signInResponseModel: String,val throwable: Throwable? =null): LoginSealedClass<T>()
    data class Loading<T>(val isLoading: Boolean, val message: String = ""): LoginSealedClass<T>()
    data object EmptyState: LoginSealedClass<Unit>()*/
    data class Success<T>(val successModel: T): LoginSealedClass()
    data class Failure<T>(val failureModel: T): LoginSealedClass()
    data class Message(val message: String,val throwable: Throwable? =null): LoginSealedClass()
    data class Loading(val isLoading: Boolean, val message: String = ""): LoginSealedClass()
    data object EmptyState: LoginSealedClass()
}
package com.example.shopeasy.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val user: State<FirebaseUser?> = _user

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                    _error.value = null
                    onResult(true)
                } else {
                    _error.value = task.exception?.message
                    onResult(false)
                }
            }
    }

    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                    _error.value = null
                    onResult(true)
                } else {
                    _error.value = task.exception?.message
                    onResult(false)
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}
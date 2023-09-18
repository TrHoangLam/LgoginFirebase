package com.example.lgoginfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lgoginfirebase.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class SignInActivity : BaseActivity() {
    private var binding:ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

       val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.default_web_client_id))
           .requestEmail()
           .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding?.tvRegister?.setOnClickListener {
            startActivities(arrayOf(Intent(this,SignUpActivity::class.java)))
            finish()
        }

        binding?.tvForgotPassword?.setOnClickListener {
            startActivities(arrayOf(Intent(this,ForgetPasswordActivity::class.java)))

        }
        binding?.btnSignIn?.setOnClickListener {
            sinInUser()
        }
        binding?.btnSignInWithGoogle?.setOnClickListener {signInWithGoogle()}

        binding?.btncn?.setOnClickListener {
            startActivities(arrayOf(Intent(this,EndActivity::class.java)))
        }
    }

    private fun sinInUser()
    {
        val email = binding?.etSinInEmail?.text.toString()
        val password = binding?.etSinInPassword?.text.toString()
        if (validateForm(email,password))
        {
            showProgressBar()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if (task.isSuccessful)
                    {
                        startActivities(arrayOf(Intent(this,MainActivity::class.java)))
                        finish()
                        hidePRogressBar()
                    }
                    else
                    {
                        showToast(this,"Can't login currently. Try after sometime")
                        hidePRogressBar()
                    }
                }
        }
    }

    private fun signInWithGoogle()
    {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
           handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful)
        {
            val account:GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }
        else
        {
            showToast(this,"SignIn Failed, Try again later")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
            showProgressBar()
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful)
            {
                startActivities(arrayOf(Intent(this,MainActivity::class.java)))
                finish()
                hidePRogressBar()
            }
            else
            {
                showToast(this,"Can't login currently. Try after sometime")
                hidePRogressBar()
            }
        }
        }


    private fun validateForm(email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.tilEmail?.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password) ->{
                binding?.tilPassword?.error = "Enter password"
                false
            }
            else -> {true}
        }
    }
}
package ows.kotlinstudy.githubrepo

import RetrofitUtil
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import kotlinx.coroutines.*
import ows.kotlinstudy.githubrepo.databinding.ActivitySigninBinding
import ows.kotlinstudy.githubrepo.utility.AuthTokenProvider
import kotlin.coroutines.CoroutineContext

class SignInActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySigninBinding

    private val authTokenProvider by lazy { AuthTokenProvider(this) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkAuthCodeExist()) {
            Log.d("msg", "checkAuthCodeExist")
            launchMainActivity()
        } else {
            Log.d("msg", "checkAuthCode not Exist")
            initViews()
        }
    }

    private fun checkAuthCodeExist(): Boolean = authTokenProvider.token.isNullOrBlank().not()

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            loginGithub()
        }
    }

    private fun launchMainActivity(){
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    // https://github.com/login/oauth/authorize?client_id=~~~
    private fun loginGithub() {
        val loginUri = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(this, loginUri)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.getQueryParameter("code")?.let {
            // getAccessToken
            launch(coroutineContext) {
                showProgress()
                getAccessToken(it)
                dismissProgress()
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext) {
        with(binding) {
            loginButton.isGone = true
            progressBar.isGone = false
            progressTextView.isGone = false
        }
    }

    private suspend fun dismissProgress() = withContext(coroutineContext) {
        with(binding) {
            loginButton.isGone = false
            progressBar.isGone = true
            progressTextView.isGone = true
        }
    }

    private suspend fun getAccessToken(code: String) = withContext(Dispatchers.IO) {
        Log.d("msg","getAccessToekn ${code}")
        val response = RetrofitUtil.authApiService.getAccessToken(
            clientId = BuildConfig.GITHUB_CLIENT_ID,
            clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
            code = code
        )

        //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
        if (response.isSuccessful) {
            val accessToken = response.body()?.accessToken ?: ""
            if (accessToken.isNotEmpty()) {
                authTokenProvider.updateToken(accessToken)
            } else {
                Toast.makeText(this@SignInActivity, "accessToken이 존재하지 않습니다", Toast.LENGTH_SHORT)
                    .show()
            }
        }else{
            Log.d("msg","getAcessToken error")
        }
    }
}
package com.b00sti.travelbucketlist.ui.auth.splash

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.b00sti.travelbucketlist.BR
import com.b00sti.travelbucketlist.R
import com.b00sti.travelbucketlist.base.BaseActivity
import com.b00sti.travelbucketlist.databinding.ActivitySplashBinding
import com.b00sti.travelbucketlist.utils.ScreenRouter
import com.google.firebase.auth.FirebaseAuth
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by b00sti on 15.02.2018
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    override fun getViewModels(): SplashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    override fun getBindingVariable(): Int = BR.vm
    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goNext()
        printKey()
    }

    fun printKey() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.b00sti.travelbucketlist",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }

    private fun goNext() {
        viewModel.setNavigator(this)
        when {
            isAuthorized() -> openMainActivity()
            else           -> openAuthActivity()
        }
    }

    private fun isAuthorized(): Boolean = FirebaseAuth.getInstance().currentUser != null

    override fun openMainActivity() {
        ScreenRouter.goToMainActivity(this)
        finish()
    }

    override fun openAuthActivity() {
        ScreenRouter.goToAuthActivity(this)
        finish()
    }
}
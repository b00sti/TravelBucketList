package com.b00sti.travelbucketlist.ui.auth.register

import android.arch.lifecycle.ViewModelProviders
import com.b00sti.travelbucketlist.BR
import com.b00sti.travelbucketlist.R
import com.b00sti.travelbucketlist.base.BaseFragment
import com.b00sti.travelbucketlist.databinding.FragmentRegisterBinding
import com.b00sti.travelbucketlist.utils.ScreenRouter
import com.b00sti.travelbucketlist.utils.finish

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(), RegisterNavigator {

    companion object {
        fun getInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun getViewModels(): RegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    override fun getBindingVariable(): Int = BR.vm
    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun onResume() {
        super.onResume()
        viewModel.setNavigator(this)
    }


    override fun registerFacebook() {}//(getBaseActivity() as AuthActivity).onFacebookClick()
    override fun openLoginFragment() = getBase()?.onBackPressed() ?: Unit

    override fun openMainActivity() {
        ScreenRouter.goToMainActivity(getBase())
        finish()
    }


}
package com.b00sti.travelbucketlist.ui.auth.login

import com.b00sti.travelbucketlist.base.BaseNav

/**
 * Created by b00sti on 15.02.2018
 */
interface LoginNavigator : BaseNav {

    fun loginFacebook()
    fun openRegisterFragment()
    fun openMainActivity()
    fun openForgotFragment()

}
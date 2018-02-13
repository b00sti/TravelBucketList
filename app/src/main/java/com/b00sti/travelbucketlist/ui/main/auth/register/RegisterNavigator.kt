package com.b00sti.travelbucketlist.ui.main.auth.register

import com.b00sti.travelbucketlist.base.BaseNavigator

interface RegisterNavigator : BaseNavigator {
    fun registerFacebook()
    fun openLoginFragment()
    fun openMainActivity()
}
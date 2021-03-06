package com.b00sti.travelbucketlist.ui.main

import com.b00sti.travelbucketlist.base.BaseVM

/**
 * Created by b00sti on 08.02.2018
 */
class MainViewModel : BaseVM<MainNavigator>() {

    fun onBackArrowClick() {
        getNavigator().onBackArrowClick()
    }

    fun onSettingsClicked() {
        getNavigator().onBackArrowClick()
    }
}
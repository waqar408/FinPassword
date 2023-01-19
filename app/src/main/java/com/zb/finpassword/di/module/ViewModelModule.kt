package com.zb.finpassword.di.module

import com.zb.finpassword.ui.add_account.ViewModel.AddPrivacyViewModel
import com.zb.finpassword.ui.login.ViewModel.LoginViewModel
import com.zb.finpassword.ui.privacy.ViewModel.HomeViewModel
import com.zb.finpassword.ui.sign_up.ViewModel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

   /* viewModel {
        SplashViewModel(get())
    }*/

    viewModel{
        LoginViewModel(get(), get())
    }
    viewModel{
        HomeViewModel(get(), get())
    }
    viewModel{
        SignUpViewModel(get(), get())
    }
    viewModel{
        AddPrivacyViewModel(get(), get())
    }

}
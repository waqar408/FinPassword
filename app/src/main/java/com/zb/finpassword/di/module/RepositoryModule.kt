package com.zb.smartsaving.di.module

import com.zb.finpassword.data.repository.*
import org.koin.dsl.module

val repoModule= module {
   /* single {
        SplashRepository(get())
    }*/

    single {
        LoginRepository(get(), get())
    }
    single {
        HomeRepository(get(), get())
    }
    single {
        ListingRepository(get(), get())
    }
    single {
        SignUpRepository(get(), get())
    }
    single {
        AddPrivacyRepository(get(), get())
    }
}
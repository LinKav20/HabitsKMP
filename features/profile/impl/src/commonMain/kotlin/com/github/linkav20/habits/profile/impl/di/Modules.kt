package com.github.linkav20.habits.profile.impl.di

import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.profile.api.ProfileNavContent
import com.github.linkav20.habits.profile.api.profile.ProfileComponent
import com.github.linkav20.habits.profile.api.profile.ProfileContent
import com.github.linkav20.habits.profile.impl.ui.DefaultProfileNavComponent
import com.github.linkav20.habits.profile.impl.ui.ProfileNavContentImpl
import com.github.linkav20.habits.profile.impl.ui.profile.DefaultProfileComponent
import com.github.linkav20.habits.profile.impl.ui.profile.ProfileContentImpl
import org.koin.dsl.module

fun profileFeatureModule() = module {
    factory<ProfileNavComponent.Factory> {
        DefaultProfileNavComponent.Factory(
            get(),
            get(),
            get(),
        )
    }
    factory<ProfileNavContent> { ProfileNavContentImpl() }
    factory<ProfileComponent.Factory> { DefaultProfileComponent.Factory() }
    factory<ProfileContent> { ProfileContentImpl() }
}

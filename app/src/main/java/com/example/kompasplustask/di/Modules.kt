package com.example.kompasplustask.di

import com.example.kompasplustask.data.FaqRepositoryImpl
import com.example.kompasplustask.domain.repository.FaqRepository
import com.example.kompasplustask.presentation.detail_screen.DetailViewModel
import com.example.kompasplustask.presentation.main_screen.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<FaqRepository> { FaqRepositoryImpl() }


}
val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
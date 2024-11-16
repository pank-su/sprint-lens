package su.pank.sprintlens.ui.di

import org.koin.dsl.module
import su.pank.sprintlens.ui.screen.main.MainScreenModel
import su.pank.sprintlens.ui.screen.select_dataset.SelectDatasetScreenModel

val uiModule = module {
    factory {
        SelectDatasetScreenModel(get())
    }
    factory {
        MainScreenModel(it[0], get())
    }
}
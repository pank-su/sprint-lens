package su.pank.sprintlens.ui.di

import org.koin.dsl.module
import su.pank.sprintlens.ui.screen.select_dataset.SelectDatasetScreenModel

val uiModule = module {
    factory {
        SelectDatasetScreenModel()
    }
}
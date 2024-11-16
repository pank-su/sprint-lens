package su.pank.sprintlens.data.di

import io.getenv
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import su.pank.sprintlens.data.DatasetsRepostiory
import su.pank.sprintlens.data.RemoteDatasetsRepository
import su.pank.sprintlens.data.TestDatasetsRepository

val dataModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(Logging){
                level = LogLevel.BODY
                
            }

            defaultRequest {
                val defaultUrl =  "192.168.0.108:8080"
                url(if (defaultUrl.startsWith("http")) defaultUrl else "http://$defaultUrl/")
            }

        }
    }

    single<DatasetsRepostiory> {
        RemoteDatasetsRepository(get())
        //TestDatasetsRepository()
    }

}
package su.pank.sprintlens.data.models

import io.github.vinceglb.filekit.core.PlatformFile

data class LoadingFiles(val sprintFile: PlatformFile, val ticketFile: PlatformFile, val historyOfTickets: PlatformFile)


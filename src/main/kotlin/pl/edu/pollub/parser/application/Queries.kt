package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.ComputerId
import java.io.File

data class ExportFileQuery(val fileHint: File)

data class GetComputerQuery(val id: ComputerId)

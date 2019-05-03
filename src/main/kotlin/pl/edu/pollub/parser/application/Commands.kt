package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.ComputerId
import java.io.File

data class ImportFileCommand(val fileToImport: File)

data class RemoveComputerCommand(val computerToRemoveId: ComputerId)

data class EditComputerCommand(val id: ComputerId, val computerToEditData: ComputerTableRow) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EditComputerCommand

        if (id != other.id) return false
        if (!computerToEditData.contentEquals(other.computerToEditData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + computerToEditData.contentHashCode()
        return result
    }
}

data class AddComputerCommand(val computerToAddData: ComputerTableRow) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddComputerCommand

        if (!computerToAddData.contentEquals(other.computerToAddData)) return false

        return true
    }

    override fun hashCode(): Int {
        return computerToAddData.contentHashCode()
    }
}
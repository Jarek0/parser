package pl.edu.pollub.parser.utils

class FileReader {

    static File readFile(String fileName) {
        def c = FileReader.class.getClassLoader()
        def resource = c.getResource(fileName)
        return new File(resource.getFile())
    }

}

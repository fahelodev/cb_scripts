/** reading files **/

String filePath = "data1.txt" //Absolute Path fix too
File myFile = new File(filePath)

println myFile.text

//collect data into a List
def lista = myFile.collect {it}
println "lista: $lista"

// store file content in an array
def array = myFile as String[]  // asi se define un array
println "array: $array"

// read file into a list of String
def lines = myFile.readLines()
println "lines: $lines"

// read file line by line
def lineNoRange = 2..4
def lineList =[]
myFile.eachLine {line, lineNumber ->
    if(lineNoRange.contains(lineNumber)){
        lineList.add(line)
    }
    println "$lineNumber: $line"
}
println "lineList: $lineList"

// read with Reader
def line
myFile.withReader {reader ->
    while ((line = reader.readLine()) != null){
        println "line reader: $line"
    }
}

//reading with new reader
def outputFile = "data2.txt"
def reader = myFile.newReader()
new File(outputFile).append(reader)
reader.close()

//work with binary
byte[] contents = myFile.bytes
println contents

//size in bytes
println contents.length

//check if is a File or Directory
println myFile.isFile()
println myFile.isDirectory()

//get list of files from a dir
new File( "C:/aws").eachFile {files ->
    println files.getAbsolutePath()
}

//get list recursively display all files in a directory
new File( "C:/aws").eachFileRecurse {files ->
    println files.getAbsolutePath().replace("\\","/")
}

//copy files data to another file
def newFile = new File("data3.txt")
newFile<<myFile.text

//borrar archivo
newFile.delete()












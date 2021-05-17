File myFile = new File("dataWrites.txt")

//escribir
myFile.write('''Hola este es un texto
con varios saltos de linea''')
myFile <<"\ntambien puedo agregar data asi"
myFile.text = "y asi tambien puedo ingresar data, pero borro la que existia"

myFile.renameTo(new File("nuevonombre.txt"))

myFile.delete()

//check if is a File or Directory
println myFile.isFile()
println myFile.isDirectory()
println myFile.isHidden()


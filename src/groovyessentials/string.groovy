// single - double
def name = "inputOutput"
String nombre = "inputOutput"
println "mi nombre es: ".concat(nombre)
println "mi nombre es: ${name}"
println "mi nombre es: $name"
println 'mi nombre es: $name'

// triple single - triple double MULTILINEA
def s1 = """ This is a groovy class 
and we are learning strings"""
def sx = '''This is a groovy class 
and we are learning strings'''
println(s1)
println(sx)


def s2 = "pruebaDeCaracteres"
println s2.length()
println s2[2]
println s2[-2]
println s2.indexOf('D')
println s2[2..4]
println s2.substring(5)
println s2.subSequence(1,4)
println s2.split("e")
println s2-("Caracteres")
println s2.toLowerCase()
println s2.toUpperCase()
println s2*3

// slashy
// dollar slashy
def s3 = /un string escrito ${s2}/
def s4 = $/otro string escrito por aca/$

println s3
println s4

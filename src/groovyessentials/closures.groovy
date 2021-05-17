//Closure basico
def MyClosure = { println "Hola Closure"}
MyClosure.call()

//closure con 1 parametro : name
def hola = "hola" // esta referencia no se puede hacer en metodos, pero en Closures si
def ClosureUnParam = { name -> println "$hola $name"}
ClosureUnParam("Edu")

//llamar un clos en un metodo
def myMethod(clos){
    clos.call("Eloisa")
}
myMethod(ClosureUnParam)


//closure multiParametro
def ClosureMultiParam = {
    a,b,c ->
    return (a+b+c)
}
println ClosureMultiParam(10,20,30)


//Listas y Clousures
def listasNombres = ["Edu", "Pame", "Elo","Santi", "C3PO", "1234", "J1N3T"]

println listasNombres.find{it =="Edu"}

def resultado = listasNombres.findAll {it ==~ /[A-Z][a-z]+/}
    .collect {String name -> name.toUpperCase()}
    .sort {String a , String b -> a.length() <=> b.length() ?: a<=>b }

resultado.eachWithIndex{ String val, int idx ->
    println "$idx => $val"
}







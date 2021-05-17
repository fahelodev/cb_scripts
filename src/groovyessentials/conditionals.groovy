//conditional statement
//if else
def num = 10
if (num == 10) {
    println 'mi numero es diez'
}
else {
    println("mi numero es ${num}")
}

//swtich case
def x = 100
def result = ""

switch (x){
    case {x==0}:
        result = "x es Cero"
        break
    case {x==1}:
        result ="x es Uno"
        break
    case {x==10}:
        result = "x es Diez"
        break
    default:
        result = "Invalido"
}
println(result)

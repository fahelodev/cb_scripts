//for - classic
for (def i = 1; i<=5; i++){
    println(i)
}

// for in groovy
for (i in 1..5) {
    println(i)
}
//other for
1.upto(5){println("$it")}
5.times {println("$it")}
1.step(10,2){println("$it")}

def map = [name:"inputOutput",sub:"automation"]
for (e in map) {
    print e.key+":"
    println e.value
}


//while loop

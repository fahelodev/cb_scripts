//Collection Data Type
//Maps : key value pair  synthax : ['':'']

//Simple Maps
def MyMap = ['name': 'inputOutput',
             'last': 'araya',
             'age': 40]

println MyMap
println MyMap.name
println MyMap['name']
println MyMap.get('last')

MyMap.put('city','Santiago')
println MyMap

println MyMap.containsKey('city')
println MyMap.containsValue('inputOutput')

println MyMap. getClass()

def MyMap2 = MyMap.clone()
println MyMap2

MyMap2.each {key,value ->
    println "$key : $value"
}



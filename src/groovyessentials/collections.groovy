//Collection Data types
//List
/**
 * syntax:  [obj1,obj2,obj3]
 *          [1,2,3]
 *          ["text1","text2","text3"]
 *          [obj1,obj2,[obj31,obj32]]
 *          [] : any
 */

def fruits = ["apple","orange","watermelon","grapes"]
println fruits[0]
println fruits.get(1)

def fruits2 = ["apple","orange",["watermelon","grapes"]]
println fruits2[2][0]
println fruits2.get(2).get(0)
println fruits2[2..0]
println fruits2[0..2]
println fruits2[2].contains("grapes")
println fruits2[2].size()
fruits2<<"uvas"<<"platanos"
println fruits2
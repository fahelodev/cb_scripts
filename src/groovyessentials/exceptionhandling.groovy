try{
    int i = 1/0
}catch(Exception exp){
    print "en el bloque de excepcion : causa "
    print exp.getCause()+'\n'
}finally{
    println "imprime el bloque final"
}
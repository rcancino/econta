package com.luxsoft.sw4.econta

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='codigo')
class CuentaSat {

	String codigo
	String nombre
	String tipo
	

    static constraints = {
    	codigo nullable:false,unique:true,maxSize:20
    	tipo maxSize:100
    	
    }
    

    String toString(){
    	return "$codigo $nombre"
    }
}

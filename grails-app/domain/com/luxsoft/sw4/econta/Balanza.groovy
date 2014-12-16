package com.luxsoft.sw4.econta

import com.luxsoft.sw4.Empresa

class Balanza {

	Empresa empresa
	String version='1.0'
	String rfc
	
	String mes
	Integer ejercicio
	byte[] acuse

	static hasMany = [partidas: BalanzaDet]

    static constraints = {
    	version inList:['1.0']
    	rfc matches:"[A-ZÑ&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z0-9]?[A-Z0-9]?[0-9A-Z]?"
    	mes inList:['01','02','03','04','05','06','07','08','09','10','11','12']
    	ejercicio range:2014..2099
    	acuse nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el acuse
    }
    static mapping = {
		partidas cascade: "all-delete-orphan"
	}
}

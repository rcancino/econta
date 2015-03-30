package com.luxsoft.sw4.econta

import com.luxsoft.sw4.Empresa

class Balanza {

	Empresa empresa
	String versionSat='1.1'
	String rfc
	
	String mes
	Integer ejercicio
	byte[] acuse
	byte[] xml

	Date dateCreated
	Date lastUpdated

	static hasMany = [partidas: BalanzaDet]

    static constraints = {
    	versionSat inList:['1.0']
    	//rfc matches:"[A-ZÑ&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z0-9]?[A-Z0-9]?[0-9A-Z]?"
    	rfc size:12..13
    	mes inList:['01','02','03','04','05','06','07','08','09','10','11','12']
    	ejercicio range:2014..2099
    	acuse nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el acuse
    	xml nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }
    static mapping = {
		partidas cascade: "all-delete-orphan"
	}
}

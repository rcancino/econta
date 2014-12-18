package com.luxsoft.sw4.econta

import com.luxsoft.sw4.Empresa

class Polizas {

	Empresa empresa
	String versionSat="1.0"
	String rfc
	Integer mes
	Integer ejercicio

	byte[] acuse
	byte[] xml

	Date dateCreated
	Date lastUpdated

	static hasMany = [partidas: Poliza]

    static constraints = {
    	rfc matches:"[A-ZÑ&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z0-9]?[A-Z0-9]?[0-9A-Z]?"
    	mes range:1..12
    	ejercicio range:2014..2099
    	acuse nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el acuse
    	xml nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }

    static mapping = {
		partidas cascade: "all-delete-orphan"
	}
}

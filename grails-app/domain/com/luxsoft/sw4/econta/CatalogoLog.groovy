package com.luxsoft.sw4.econta

import com.luxsoft.sw4.Empresa

class CatalogoLog {

	Empresa empresa
	Integer ejercicio
	Integer mes

	byte[] acuse
	byte[] xml

	Date dateCreated
	Date lastUpdated


    static constraints = {
    	ejercicio range:2014..2099
    	acuse nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el acuse
    	xml nullable:true,maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }
}

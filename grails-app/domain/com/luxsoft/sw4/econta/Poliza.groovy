package com.luxsoft.sw4.econta

class Poliza {

	
	Integer tipo
	String descripcion
	Date fecha
	String concepto


	Date dateCreated
	Date lastUpdated

	static belongsTo = [polizas: Polizas]

    static constraints = {
    	concepto size:1..300
    }
}

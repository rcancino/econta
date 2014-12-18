package com.luxsoft.sw4.econta

class Poliza {

	
	Integer tipo
	String num
	String descripcion
	Date fecha
	String concepto
	Long origen
	String tipoOrigen


	Date dateCreated
	Date lastUpdated

	static belongsTo = [polizas: Polizas]

	static hasMany = [partidas: PolizaDet]

    static constraints = {
    	concepto size:1..300
    	origen nullable:true
    }

    static mapping = {
		partidas cascade: "all-delete-orphan"
	}
}

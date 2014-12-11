package com.luxsoft.sw4

import grails.validation.Validateable
import groovy.transform.ToString


@ToString(includeNames=true,includePackage=false)
@Validateable(nullable=true)
class Direccion implements Serializable{
	
	String calle
	String numeroInterior
	String numeroExterior
	String colonia
	String municipio
	String codigoPostal
	String estado
	String pais='MEXICO'

	
    static constraints = {
		calle(nullable:true,size:1..200)
		numeroInterior(size:1..50,nullable:true)
		numeroExterior(size:1..50,nullable:true)
		//colonia(nullable:true)
		//municipio(nullable:true)
		//codigoPostal(nullable:true)
		//estado(nullable:true)
		pais(nullable:false,size:1..100)
    }
	
	
}
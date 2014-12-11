package com.luxsoft.sw4
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='rfc')
class Empresa {

	String nombre
	String clave
	String rfc

	Direccion direccion

	Date dateCreated
	Date lastUpdated


    static constraints = {
    	clave minSize:3,maxSize:15
    	rfc size:12..13
    }

    static embedded = ['direccion']

    String toString(){
    	return "$nombre"
    }

    def beforeInsert() {
		capitalizarNombre()
	}

	def beforeUpdate() {
		if (isDirty('nombre') || isDirty('clave')) {
			capitalizarNombre()
		}
	}

	private capitalizarNombre(){
		nombre=nombre.toUpperCase()
		clave=clave.toUpperCase()
	}
}

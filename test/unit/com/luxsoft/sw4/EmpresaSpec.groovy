package com.luxsoft.sw4

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.buildtestdata.mixin.Build

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Empresa)
@Build(Empresa)
class EmpresaSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Alta de nueva empresa"() {
    	given:'Una empresa nueva'
    	def empresa=Empresa.buildWithoutSave(nombre:'PAPEL')

    	when:'Salvamos la empresa'
    	empresa.save()

    	then:'La empresa persiste satisfactoriamente en la base de datos'
    	empresa.errors.errorCount==0
    	empresa.id
    }

    void "Test modificacion de una empresa"(){
    	given:'Una empresa existente'
    	def empresa=Empresa.build(nombre:'PAPEL')

    	when:'Modificamos el nombre'
    	def found=Empresa.get(empresa.id)
    	found.nombre="PAPEL2"
    	found.save flush:true

    	then:'La modificaion presiste en la base de datos'
    	Empresa.get(found.id).nombre=='PAPEL2'
    }
}

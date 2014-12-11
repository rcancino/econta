package com.luxsoft.sw4.econta

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CuentaSat)
class CuentaSatSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test ToString"() {
    	given:
    	def cuenta=new CuentaSat(codigo:'1',nombre:'Caja')
    	expect:
    	cuenta.toString()=='1 Caja'

    }

    void "test alta de cuenta"(){
    	given: 'Una cuenta nueva'
    	def cuenta=new CuentaSat(codigo:'1',nombre:'Caja',tipo:'ACTIVO')

    	when:'salvamos'
    	cuenta.save()

    	then:'La cuenta es persistida satisfactoriamente'
    	cuenta.errors.errorCount==0
    	cuenta.id
    }

    
}

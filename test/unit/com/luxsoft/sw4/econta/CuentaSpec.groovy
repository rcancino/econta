package com.luxsoft.sw4.econta

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.buildtestdata.mixin.Build
import com.luxsoft.sw4.Empresa

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Cuenta)
@Build([Cuenta,Empresa])
class CuentaSpec extends Specification {

	def empresa

    def setup() {
    	empresa=Empresa.build()
    }

    def cleanup() {
    }

    void "test Crear una cuenta"() {
    	given: 'Una cuenta nueva'
    	def cuenta=Cuenta.buildWithoutSave(empresa:empresa,clave:'001')
    	when:'Salvamos la cuenta'
    	cuenta.save()

    	then:'La cuenta persiste exitosamente en la base de datos'
    	cuenta.errors.errorCount==0
    	cuenta.id
    }

    void "test Crear sub cuenta"(){
    	given: 'Una cuenta existente'
    	def cuenta=Cuenta.build(empresa:empresa,clave:'001')

    	when:'Agregamos una subcuenta'
    	def subCuenta=Cuenta.build(empresa:empresa,clave:'001-001',padre:cuenta)
    	cuenta.save flush:true

    	then:'La subcuenta persiste exitosamente'
    	Cuenta.get(subCuenta.id)
    	Cuenta.get(subCuenta.id).clave=='001-001'
    }
}

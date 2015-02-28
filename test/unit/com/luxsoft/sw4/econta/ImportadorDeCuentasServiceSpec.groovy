package com.luxsoft.sw4.econta

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImportadorDeCuentasService)
class ImportadorDeCuentasServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Test importacion "() {
    	given:'Un archivo existente'
    	def file=new File('test/unit/resources/CuentasContablesPapelSA.csv')
    	
    	expect: 'El archivo existe'
    	file.exists()

    	when: 'Procesamos el archivo'
    	def cuentas=service.procesar(file)

    	then:'Obtenemos la lista de las cuentas a importar'
    	cuentas.size()==1395
    }
}

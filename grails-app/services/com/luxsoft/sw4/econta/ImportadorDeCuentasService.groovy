package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class ImportadorDeCuentasService {

	@NotTransactional
    def importar(File file,def empresa) {
    	
    	def origen=1
    	// Cargar cuentas de mayor
    	file.eachLine{line,row ->
    		if(row>1){
    			
    			def fields=line.split(";")
    			
    			def nivel=fields[4].toInteger()
    			if(nivel==1){
    				def cuenta=new Cuenta(empresa:empresa)
    				cuenta.cuentaSat=CuentaSat.findByCodigo(fields[0])
    				cuenta.clave=fields[1]
    				cuenta.descripcion=fields[2]
    				cuenta.naturaleza=fields[5]=='D'?'DEUDORA':'ACREEDORA'
    				cuenta.origen=origen
    				cuenta.tipo='ACTIVO'
    				origen++
    				cuenta.save flush:true,failOnError:true
    				log.info 'Cuenta importada: '+cuenta
    			}
    		}
    	}

    	file.eachLine{line,row ->
    		if(row>1){
    			def fields=line.split(";")
    			def nivel=fields[4].toInteger()
    			if(nivel==2){
    				def cuenta=new Cuenta(empresa:empresa)
    				cuenta.padre=Cuenta.findByClave(fields[1])
    				cuenta.cuentaSat=CuentaSat.findByCodigo(fields[0])
    				cuenta.clave=fields[3]
    				cuenta.descripcion=fields[2]
    				cuenta.naturaleza=fields[5]=='D'?'DEUDORA':'ACREEDORA'
    				cuenta.origen=origen
    				cuenta.tipo='ACTIVO'
    				origen++
    				cuenta.save flush:true,failOnError:true
    				log.info 'Cuenta importada: '+cuenta
    			}
    		}
    	}
    }
}

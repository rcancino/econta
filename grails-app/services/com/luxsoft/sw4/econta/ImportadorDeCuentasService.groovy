package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class ImportadorDeCuentasService {

    def buscarCuentaSat(def codigo){
        def found=CuentaSat.findByCodigo(codigo)
        assert found,'No existe el codigo agrupador: '+codigo
    }

	@NotTransactional
    def importar(def file,def empresa,Integer ejercicio,Integer mes) {

        // new ByteArrayInputStream( arr ).eachLine('UTF-8') { line ->
        //     println line
        // }
    	
    	def origen=1
    	// Cargar cuentas de mayor

    	file.getInputStream().eachLine('ISO-8859-1'){line,row ->
    		if(row>1){
    			
    			def fields=line.split(";")

               def found =Cuenta.findByEmpresaAndClave(empresa,fields[3]);
    			
                if(!found){
                    def nivel=fields[4].toInteger()
                    if(nivel==1){
                        def cuenta=new Cuenta(empresa:empresa)
                        println "Importando cuenta de nivel 1 :"+ fields[3]
                        cuenta.cuentaSat=CuentaSat.findByCodigo(fields[0])
                        //cuenta.cuentaSat=buscarCuentaSat(fields[0])
                        cuenta.clave=fields[1]+'-0000'
                        cuenta.descripcion=fields[2]
                        cuenta.naturaleza=fields[5]=='D'?'DEUDORA':'ACREEDORA'
                        cuenta.origen=origen
                        cuenta.tipo='ACTIVO'
                        cuenta.ejercicio=ejercicio
                        cuenta.mes=mes
                        origen++
                        cuenta.save flush:true,failOnError:true
                        log.info 'Cuenta importada: '+cuenta
                    }
                }

    			
    		}
    	}
        file.getInputStream().eachLine('ISO-8859-1'){line,row ->
    	//file.eachLine{line,row ->
    		if(row>1){
    			def fields=line.split(";")
                def nivel=fields[4].toInteger()

                def found =Cuenta.findByEmpresaAndClave(empresa,fields[3]);
                
                if(!found){
                    if(nivel==2){
                        println "Importando cuenta de nivel 2 :"+ fields[3]
                        def cuenta=new Cuenta(empresa:empresa)
                        cuenta.padre=Cuenta.findByEmpresaAndClave(empresa,fields[1])
                        cuenta.cuentaSat=CuentaSat.findByCodigo(fields[0])
                        //cuenta.cuentaSat=buscarCuentaSat(fields[0])
                        cuenta.clave=fields[3]
                        cuenta.descripcion=fields[2]
                        cuenta.naturaleza=fields[5]=='D'?'DEUDORA':'ACREEDORA'
                        cuenta.origen=origen
                        cuenta.tipo='ACTIVO'
                        cuenta.ejercicio=ejercicio
                        cuenta.mes=mes
                        origen++
                        cuenta.save flush:true,failOnError:true
                        log.info 'Cuenta importada: '+cuenta
                    }
                }			
    		}
    	}
    }
}

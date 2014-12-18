package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import groovy.sql.Sql
import org.springframework.security.access.annotation.Secured
import com.luxsoft.sw4.Empresa


@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])
@Transactional
class PolizasController {

	def polizasService

    def index(Integer max) {
        params.max = Math.min(max ?: 100, 500)
        def empresa=session.empresa
        if(!empresa){
        	flash.message="Debe seleccionar una empresa para operar"
        	return [polizasInstanceCount: Polizas.count()]
        }
        respond Polizas.list(params), model:[polizasInstanceCount: Polizas.count()]
    }

    def show(Polizas polizasInstance){
    	
    }

    
    def importar(){
         [importadorCommand:new ImportadorCommand(
            empresa:session.empresa
            ,ejercicio:session.ejercicio
            ,mes:session.mes)
         ]
    }

    @Transactional
    def importarPolizas(ImportadorCommand command){
        if(!command.validate()){
            render view:'importar',model:[importadorCommand:command]
            return;
        }
        def balanza=polizasService.importar(command.empresa,command.ejercicio,command.mes)
        flash.message="Polizas del $command importada"
        //render view:'show',model:[balanzaInstance:balanza]
        redirect action:'index'

    }
}

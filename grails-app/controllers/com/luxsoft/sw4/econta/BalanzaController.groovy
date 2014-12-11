package com.luxsoft.sw4.econta



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured
import groovy.sql.Sql
import org.springframework.jdbc.datasource.SingleConnectionDataSource

@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])
@Transactional(readOnly = true)
class BalanzaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Balanza.list(params), model:[balanzaInstanceCount: Balanza.count()]
    }

    def show(Balanza balanzaInstance) {
        respond balanzaInstance
    }

    def create() {
        respond new Balanza(params)
    }

    @Transactional
    def save(Balanza balanzaInstance) {
        if (balanzaInstance == null) {
            notFound()
            return
        }

        if (balanzaInstance.hasErrors()) {
            respond balanzaInstance.errors, view:'create'
            return
        }

        balanzaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'balanza.label', default: 'Balanza'), balanzaInstance.id])
                redirect balanzaInstance
            }
            '*' { respond balanzaInstance, [status: CREATED] }
        }
    }

    def edit(Balanza balanzaInstance) {
        respond balanzaInstance
    }

    @Transactional
    def update(Balanza balanzaInstance) {
        if (balanzaInstance == null) {
            notFound()
            return
        }

        if (balanzaInstance.hasErrors()) {
            respond balanzaInstance.errors, view:'edit'
            return
        }

        balanzaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Balanza.label', default: 'Balanza'), balanzaInstance.id])
                redirect balanzaInstance
            }
            '*'{ respond balanzaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Balanza balanzaInstance) {

        if (balanzaInstance == null) {
            notFound()
            return
        }

        balanzaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Balanza.label', default: 'Balanza'), balanzaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    @Transactional
    def importar(){
        /*
        SingleConnectionDataSource ds=new SingleConnectionDataSource(
            driverClassName:'com.mysql.jdbc.Driver',
            url:'',
            username:'root',
            password:'')
         Sql sql=new Sql(ds)
         */
         [importadorCommand:new ImportadorCommand(ejercicio:session.ejercicio,mes:session.mes)]
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'balanza.label', default: 'Balanza'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

import grails.validation.Validateable
import org.grails.databinding.BindingFormat

@Validateable
class ImportadorCommand{
    
    Integer mes
    Integer ejercicio
    
    static constraints={
        ejercicio inList:(2014..2018)
        mes inList:(1..12)
        
    }
}

class ReportCommand{
    String reportName
    static constraints={
        reportName nullable:false
    }
}

@Validateable 
class FechaCommand extends ReportCommand{
    
    @BindingFormat('dd/MM/yyyy')
    Date fecha=new Date()

    static constraints={
        fecha nullable:false
    }
}

@Validateable 
class PeriodoCommand extends ReportCommand{
    @BindingFormat('dd/MM/yyyy')
    Date fechaInicial=new Date()
    @BindingFormat('dd/MM/yyyy')
    Date fechaFinal=new Date()

    static constraints={
        fechaInicial nullable:false
        fechaFinal nullable:false
    }
}

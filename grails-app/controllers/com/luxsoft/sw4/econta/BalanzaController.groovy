package com.luxsoft.sw4.econta



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured
import groovy.sql.Sql
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import com.luxsoft.sw4.Empresa


@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])
@Transactional(readOnly = true)
class BalanzaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def importadorDeBalanzaService

    def balanzaService

    def balanzaBuilder

    def index(Integer max) {
        //params.max = Math.min(max ?: 100, 500)
        //respond Balanza.list(params), model:[balanzaInstanceCount: Balanza.count()]
        params.max = Math.min(max ?: 1000, 1000)
        def query=Balanza.where {
            empresa==session.empresa
        }
        respond query.list(params), model:[cuentaInstanceCount: query.count()]
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
         [importadorCommand:new ImportadorCommand(
            empresa:session.empresa
            ,ejercicio:session.ejercicio
            ,mes:session.mes)
         ]
    }

    @Transactional
    def importarBalanza(ImportadorCommand command){
        if(!command.validate()){
            render view:'importar',model:[importadorCommand:command]
            return;
        }
        def balanza=importadorDeBalanzaService.importar(command.empresa,command.ejercicio,command.mes)
        flash.message="Balanza del $command importada"
        //render view:'show',model:[balanzaInstance:balanza]
        redirect action:'index'

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

    @Transactional
    def generarXml(Balanza balanza){
        balanza=balanzaService.generarXml(balanza)
        flash.message="XML Generado exitosamente"
        redirect action:'show',params:[id:balanza.id]
    }

    def mostrarXml(Balanza balanza){
        if(balanza.xml){
            def document=balanzaBuilder.cargar(balanza);
            render(text: document.xmlText(), contentType: "text/xml", encoding: "UTF-8")
        }else{
            flash.message="No se ha generado el XML "
            redirect action:'show',params:[id:balanza.id]
        }
    }

    def descargarXml(Balanza balanza){
         log.info 'Descargando archivo xml: '+balanza.id
        response.setContentType("application/octet-stream")
        //String name="Balanza_"+"$balanza.empresa.clave"+"_$balanza.ejercicio"+"_$balanza.mes"+".xml"
        //String mes=org.apache.commons.lang.StringUtils.leftPad(balanza.mes.toString(),2,'0')

        String name="$balanza.empresa.rfc"+"$balanza.ejercicio"+balanza.mes+"B${balanza.tipo}.xml"
        response.setHeader("Content-disposition", "filename=$name")
        response.outputStream << balanza.xml
        return
    }
}

import grails.validation.Validateable
import org.grails.databinding.BindingFormat

@Validateable
class ImportadorCommand{
    
    Empresa empresa
    Integer mes
    Integer ejercicio
    
    static constraints={
        ejercicio inList:(2014..2018)
        mes inList:(1..12)
        
    }

    String toString(){
        return "Importar balanza de $empresa.clave Ejercicio:$ejercicio Mes:$mes"
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

package com.luxsoft.sw4.econta



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.validation.Validateable
import org.springframework.security.access.annotation.Secured

@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])
@Transactional
class CuentaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def importadorDeCuentasService

    def index(Integer max) {
        params.max = Math.min(max ?: 1000, 1000)
        params.sort='clave'
        params.order='asc'
        def query=Cuenta.where {
            empresa==session.empresa
        }
        respond query.list(params), model:[cuentaInstanceCount: query.count()]
    }

    def show(Cuenta cuentaInstance) {
        respond cuentaInstance
    }

    def create() {
        if(!session.empresa){
            flash.message="Debe selecionar una empresa antes de dar de alta cuentas"
            redirect action:'index'
            return
        }
        params.empresa=session.empresa
        respond new Cuenta(params)
    }

    @Transactional
    def save(Cuenta cuentaInstance) {
        if (cuentaInstance == null) {
            notFound()
            return
        }

        if (cuentaInstance.hasErrors()) {
            respond cuentaInstance.errors, view:'create'
            return
        }

        cuentaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'cuenta.label', default: 'Cuenta'), cuentaInstance.id])
                redirect cuentaInstance
            }
            '*' { respond cuentaInstance, [status: CREATED] }
        }
    }

    def edit(Cuenta cuentaInstance) {
        respond cuentaInstance
    }

    @Transactional
    def update(Cuenta cuentaInstance) {
        if (cuentaInstance == null) {
            notFound()
            return
        }

        if (cuentaInstance.hasErrors()) {
            respond cuentaInstance.errors, view:'edit'
            return
        }
        
        cuentaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Cuenta.label', default: 'Cuenta'), cuentaInstance.id])
                redirect cuentaInstance
            }
            '*'{ respond cuentaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Cuenta cuentaInstance) {

        if (cuentaInstance == null) {
            notFound()
            return
        }

        cuentaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Cuenta.label', default: 'Cuenta'), cuentaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cuenta.label', default: 'Cuenta'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def getCuentasSatJSON() {

        def term='%'+params.term.trim()+'%'
        def query=CuentaSat.where{
            nombre=~term || codigo=~term
         }
        def list=query.list(max:30, sort:"nombre")
        
        list=list.collect{ c->
            def nombre=c.nombre
            def desc=c.toString()
            [id:c.id,
            label:c.toString(),
            value:nombre,
            nombre:nombre
            ]
        }
        def res=list as JSON
        
        render res
    }

    @Transactional
    def agregarSubCuenta(Cuenta cta,SubCuentaCommand command){
        if(!command.validate()){
            flash.mesage="Sub cuenta invalida: "+command.errors
            redirect action:'edit',params:[id:cta.id]    
        }
        def sub=new Cuenta()
        sub.empresa=cta.empresa
        sub.clave=cta.clave+command.clave
        sub.claveSat=command.claveSat
        sub.descripcion=command.descripcion
        sub.tipo=cta.tipo
        sub.subTipo=cta.subTipo
        sub.nivel=cta.nive+1
        sub.detalle=cta-detalle
        sub.deResultado=cta.deResumen
        sub.naturaleza=cta.naturaleza
        sub.presentacionContable=cta.presentacionContable
        sub.presentacionFiscal=cta.presentacionFiscal
        sub.presentacionFinanciera=cta.presentacionFinanciera
        sub.presentacionPresupuestal=cta.presentacionPresupuestal
        
        cta.addToSubCuentas(sub)
        cta.save failOnError:true
        flash.message="Sub cuenta agregada: "+cta.clave
        redirect action:'edit',params:[id:cta.id]
    }

    @Transactional
    def importar(){
        def file=grailsApplication.mainContext.getResource("/WEB-INF/data/CuentasContablesPapelSA.csv").file
        importadorDeCuentasService.importar(file,session.empresa)
        flash.message="Cuentas importadas"
        redirect action:'index'

    }

    def uploadFile(){
        def file=request.getFile('file')
        def user=getAuthenticatedUser().username
        assert session.empresa,'Debe seleccionar una empresa'
        if (file.empty) {
            flash.message = 'Archivo  incorrecto (archivo vacío)'
            redirect action:'index'
            return
        }
        def ejercicio=session.ejercicio
        def mes=1
        importadorDeCuentasService.importar(file,session.empresa,ejercicio,mes)
        flash.message="Cuentas importadas"
        redirect action:'index'
    }


}

@Validateable
class SubCuentaCommand{
    Long parent
    String clave
    String descripcion
    CuentaSat cuentaSat

}

@Validateable
class CuentaCommand{

}

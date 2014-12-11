package com.luxsoft.sw4



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured
import grails.validation.Validateable

@Secured(["hasAnyRole('OPERADOR','ADMIN')"])
@Transactional(readOnly = true)
class EmpresaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Empresa.list(params), model:[empresaInstanceCount: Empresa.count()]
    }

    def show(Empresa empresaInstance) {
        respond empresaInstance
    }

    def create() {
        respond new Empresa(params)
    }

    @Transactional
    def save(Empresa empresaInstance) {
        if (empresaInstance == null) {
            notFound()
            return
        }

        if (empresaInstance.hasErrors()) {
            respond empresaInstance.errors, view:'create'
            return
        }

        empresaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'empresa.label', default: 'Empresa'), empresaInstance.id])
                redirect empresaInstance
            }
            '*' { respond empresaInstance, [status: CREATED] }
        }
    }

    @Secured(["hasAnyRole('ADMINISTRACION')"])
    def edit(Empresa empresaInstance) {
        respond empresaInstance
    }

    @Transactional
    @Secured(["hasAnyRole('ADMINISTRACION')"])
    def update(Empresa empresaInstance) {
        if (empresaInstance == null) {
            notFound()
            return
        }

        if (empresaInstance.hasErrors()) {
            respond empresaInstance.errors, view:'edit'
            return
        }

        empresaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Empresa.label', default: 'Empresa'), empresaInstance.id])
                redirect empresaInstance
            }
            '*'{ respond empresaInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(["hasAnyRole('ADMINISTRACION')"])
    def delete(Empresa empresaInstance) {

        if (empresaInstance == null) {
            notFound()
            return
        }

        empresaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Empresa.label', default: 'Empresa'), empresaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'empresa.label', default: 'Empresa'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }


    def cambiarEmpresa(Empresa empresa){
        //def empresa=Empresa.get(params.id)
        //println 'Cambiando a empres: '+empresa
        session.empresa=empresa
        println 'Empresa en operacion: '+session.empresa

        redirect controller:'home',action:'index'

    }
}

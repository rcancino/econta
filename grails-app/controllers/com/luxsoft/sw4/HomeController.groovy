package com.luxsoft.sw4
import grails.plugin.springsecurity.annotation.Secured



@Secured(["hasRole('USUARIO')"])
class HomeController {

    def index() { }

    def catalogos(){

    }

    /*
    def seleccionarEmpresa(){
         def origin=request.getHeader('referer')
         session.periodo=command
         redirect(uri: request.getHeader('referer') )
    }
    */
}

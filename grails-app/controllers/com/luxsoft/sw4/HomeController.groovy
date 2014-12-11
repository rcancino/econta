package com.luxsoft.sw4
import grails.plugin.springsecurity.annotation.Secured



@Secured(["hasRole('USUARIO')"])
class HomeController {

    def index() { }

    def catalogos(){

    }
}

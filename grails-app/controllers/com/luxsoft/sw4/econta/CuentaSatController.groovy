package com.luxsoft.sw4.econta

import org.springframework.security.access.annotation.Secured

@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])
class CuentaSatController {

    def index() { 
    	params.max=500
    	params.sort='codigo'
    	params.order='asc'
    	[cuentaSatInstanceList:CuentaSat.list(params),cuentaSatInstanceCount:CuentaSat.count()]
    }
}

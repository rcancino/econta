
import grails.plugin.springsecurity.SpringSecurityUtils

def loggedIn = { -> 
    springSecurityService.principal instanceof String
}
def loggedOut = { -> 
    !(springSecurityService.principal instanceof String)
}
def isAdmin = { -> 
    SpringSecurityUtils.ifAllGranted('ADMIN')
}
def isAdministracion = { -> 
    SpringSecurityUtils.ifAllGranted('ADMINISTRACION')
}
def isMostrador ={
	SpringSecurityUtils.ifAllGranted('MOSTRADOR')	
}
def isCajero ={
	SpringSecurityUtils.ifAllGranted('CAJERO')	
}

navigation={
	user{
		home(action:'index',titleText:'Inicio'){}
		
		catalogos(controller:'home',action:'catalogos'){
			empresas(controller:'empresa',action:'index')
			cuentasSat(controller:'cuentaSat',action:'index')
			cuentasContables(controller:'cuenta',action:'index')
			bitacoras(controller:'catalogoLog',action:'index',titleText:'Bitácora de envios')
		}
		contabilidad(controller:'balanza',action:'index'){
			balanza(controller:'balanza',action:'index')
			polizas()
		}
		operaciones()
		//configuracion()
		consultas()
	}
	
}
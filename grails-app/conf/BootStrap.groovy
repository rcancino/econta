import com.luxsoft.sec.*
import org.bouncycastle.jce.provider.BouncyCastleProvider

class BootStrap {

    def init = { servletContext ->

    	java.security.Security.addProvider(new BouncyCastleProvider())

    	
		def userRole=Rol.findOrSaveWhere(authority:'USUARIO')
		def mostradorRole=Rol.findOrSaveWhere(authority:'OPERADOR')
		def administracionRole=Rol.findOrSaveWhere(authority:'ADMINISTRACION')
		def adminRole=Rol.findOrSaveWhere(authority:'ADMIN')

		def admin=Usuario.findByUsername('admin')
		if(!admin){
			admin=new Usuario(username:'admin'
				,password:'admin'
				,apellidoPaterno:'nd'
				,apellidoMaterno:'nd'
				,nombres:'admin',nombre:'ADMIN').save(flush:true)
			UsuarioRol.create(admin,userRole,true)
			UsuarioRol.create(admin,adminRole,true)
		}

    }
    def destroy = {
    }
}

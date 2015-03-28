package com.luxsoft.sw4.econta
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional
//import mx.luxsoft.econta.x1.CatalogoDocument
import org.apache.xmlbeans.XmlOptions

import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument.Catalogo
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument.Catalogo.Mes


@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])	
@Transactional
class CatalogoLogController {

    def index() {
    	def empresa=session.empresa
    	if(!empresa) {
    		flash.message="DEBE SELECCIONAR UNA EMPRESA"
    		return
    	}
    	def catalogoLogInstanceList=CatalogoLog.findAllByEmpresa(empresa);
    	[catalogoLogInstanceList:catalogoLogInstanceList]
    }

    def mostrarXml(CatalogoLog log){
        ByteArrayInputStream is=new ByteArrayInputStream(log.getXml())
        CatalogoDocument document=CatalogoDocument.Factory.parse(is)
        render(text: document.xmlText(), contentType: "text/xml", encoding: "UTF-8")
    }
    /*
     def mostrarXml2(CatalogoLog log){
        ByteArrayInputStream is=new ByteArrayInputStream(log.getXml())
        CatalogoDocument document=CatalogoDocument.Factory.parse(is)
		render(text: document.xmlText(), contentType: "text/xml", encoding: "UTF-8")
    }
    */

    def descargarXml(CatalogoLog log){
        this.log.info 'Descargando archivo xml de catalogo de cuentas: '+log.id
        response.setContentType("application/octet-stream")
        String mes=org.apache.commons.lang.StringUtils.leftPad(log.mes.toString(),2,'0')
        String name="$log.empresa.rfc"+"$log.ejercicio"+mes+"CT.xml"
        response.setHeader("Content-disposition", "filename=$name")
        response.outputStream << log.xml
        return
    }

    def uploadAcuse(CatalogoLog log){
    	def data=request.getFile('acuse')
		def comentario=params.comentario
		log.acuse=data.getBytes()
		log.save failOnError:true
		redirect action:'index'
    }

    def descargarAcuse(CatalogoLog log){
        response.setContentType("application/octet-stream")
        String name="Catalogo_"+"$log.empresa.clave"+"_$log.ejercicio"+"_$log.mes"+"_acuse.xml"
        response.setHeader("Content-disposition", "filename=$name")
        response.outputStream << log.acuse
        return
    }

    def XmlOptions getOptions(){
		XmlOptions options = new XmlOptions();
		options.setCharacterEncoding("UTF-8");
		options.put( XmlOptions.SAVE_INNER );
		options.put( XmlOptions.SAVE_PRETTY_PRINT );
		options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );
		options.put( XmlOptions.SAVE_USE_DEFAULT_NAMESPACE );
		options.put(XmlOptions.SAVE_NAMESPACES_FIRST);
		return options;
	}
}

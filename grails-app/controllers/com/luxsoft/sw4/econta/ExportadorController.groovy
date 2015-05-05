package com.luxsoft.sw4.econta

import mx.luxsoft.econta.x1.CatalogoDocument

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;

import mx.luxsoft.econta.x1.CatalogoDocument;
import mx.luxsoft.econta.x1.CatalogoDocument.Catalogo;
import mx.luxsoft.econta.x1.CatalogoDocument.Catalogo.Mes;
import mx.luxsoft.econta.x1.CatalogoDocument.Catalogo.Ctas;
import mx.luxsoft.econta.x1.BalanzaDocument;

import com.luxsoft.sw4.Empresa
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional
import groovy.xml.XmlUtil

@Secured(["hasAnyRole('OPERADOR','ADMINISTRACION')"])	
@Transactional
class ExportadorController {

	
	def catalogoDeCuentasBuilder


	def exportarCatalogoDeCuentas(Empresa empresa){

		//assert catalogoDeCuentasBuilder,'No se registro el CatalogoDeCuentasBuilder'
		//def mes=1
		//def mes=session.mes
    	def ejercicio=session.ejercicio
		def documento=catalogoDeCuentasBuilder.build(empresa,ejercicio,mes)

		CatalogoLog log=new CatalogoLog(empresa:empresa,ejercicio:ejercicio,mes:mes)
		ByteArrayOutputStream os=new ByteArrayOutputStream()
		documento.save(os, getOptions())
		log.xml=os.toByteArray()
		log.save failOnError:true
    	redirect controller:'catalogoLog',action:'index'

	}
	

	/**
	* Genera el archivo XML del catalogo de cuentas
	*
	*/
    def exportarCatalogoDeCuentasOld(Empresa empresa) {
    	
    	def cuentas=Cuenta.findAllByEmpresa(empresa)
    	def mes=getMes(session.mes+1)
    	def ejercicio=session.ejercicio

    	CatalogoDocument documento=CatalogoDocument.Factory.newInstance();
		Catalogo catalogo=documento.addNewCatalogo();
		catalogo.setVersion("1.0");
		catalogo.setAno(ejercicio);
		catalogo.setMes(mes);
		catalogo.setRFC(empresa.rfc);
		catalogo.setTotalCtas(cuentas.size());
    	
    	cuentas.each{ c->
    		int nivel=1
    		Ctas ctas=catalogo.addNewCtas()
			ctas.setCodAgrup(c?.cuentaSat?.codigo)
			ctas.setNumCta(c.clave)
			ctas.setDesc(XmlUtil.escapeXml(c.descripcion))
			ctas.setNivel(1)
			ctas.setNatur(c.naturaleza=='DEUDORA'?'D':'A')
			c.subCuentas.each{ c2->
				Ctas ctas2=catalogo.addNewCtas()
				ctas2.setCodAgrup(c2?.cuentaSat?.codigo)
				ctas2.setNumCta(c2.clave)
				ctas2.setDesc(XmlUtil.escapeXml(c.descripcion))
				ctas2.setSubCtaDe(c2.padre.clave)
				ctas2.setNivel(2)
				ctas2.setNatur(c2.naturaleza=='DEUDORA'?'D':'A')
			}
    	}

    	CatalogoLog log=new CatalogoLog(empresa:empresa,ejercicio:ejercicio,mes:session.mes)
		ByteArrayOutputStream os=new ByteArrayOutputStream()
		documento.save(os, getOptions())
		log.xml=os.toByteArray()
		log.save failOnError:true

    	redirect controller:'catalogoLog',action:'index'
    	//render(text: documento.toString(), contentType: "text/xml", encoding: "UTF-8")
    	//render(text: documento.xmlText(getOptions()).toString(), contentType: "text/xml", encoding: "UTF-8")
    }


    def Mes.Enum getMes(int xmes){
		switch (xmes) {
		case 1:
			return Mes.Enum.forInt(Mes.INT_X_01);
		case 2:
			return Mes.Enum.forInt(Mes.INT_X_02);
		case 3:
			return Mes.Enum.forInt(Mes.INT_X_03);
		case 4:
			return Mes.Enum.forInt(Mes.INT_X_04);
		case 5:
			return Mes.Enum.forInt(Mes.INT_X_05);
		case 6:
			return Mes.Enum.forInt(Mes.INT_X_06);
		case 7:
			return Mes.Enum.forInt(Mes.INT_X_07);
		case 8:
			return Mes.Enum.forInt(Mes.INT_X_08);
		case 9:
			return Mes.Enum.forInt(Mes.INT_X_09);
		case 10:
			return Mes.Enum.forInt(Mes.INT_X_10);
		case 11:
			return Mes.Enum.forInt(Mes.INT_X_11);
		case 12:
			return Mes.Enum.forInt(Mes.INT_X_11);
		default:
			return null;
		}
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

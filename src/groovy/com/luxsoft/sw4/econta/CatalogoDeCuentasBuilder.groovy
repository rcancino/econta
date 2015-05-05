package com.luxsoft.sw4.econta

import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument.Catalogo
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument.Catalogo.Ctas
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument.Catalogo.Mes
import mx.gob.sat.esquemas.contabilidadE.x11.catalogosParaEsqContE.CCodAgrup
import com.luxsoft.sw4.Empresa
import groovy.xml.XmlUtil
import javax.xml.namespace.QName
import org.apache.xmlbeans.XmlCursor
import org.apache.xmlbeans.XmlValidationError
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlObject
import org.bouncycastle.util.encoders.Base64
import org.apache.commons.logging.LogFactory


class CatalogoDeCuentasBuilder{

	def selladorDigital

	def cadenaBuilder

	private static final log=LogFactory.getLog(this)

	def build(Empresa empresa,Integer ejercicio,Integer mes){
		CatalogoDocument documento=CatalogoDocument.Factory.newInstance();
		Catalogo catalogo=documento.addNewCatalogo();
		catalogo.setVersion("1.1");
		catalogo.setAnio(ejercicio);
		catalogo.setMes(getMes(mes));
		catalogo.setRFC(empresa.rfc);
		depurar(documento)

		def cuentas=Cuenta.findAllByEmpresa(empresa)
		
		cuentas.each{ c->
    		if(c.cuentaSat && c.cuentaSat.codigo){
    			int nivel=1
    			Ctas ctas=catalogo.addNewCtas()
	    		ctas.setCodAgrup(getCodigo(c.cuentaSat.codigo));
				ctas.setNumCta(c.clave)
				ctas.setDesc(XmlUtil.escapeXml(c.descripcion))
				ctas.setNivel(1)
				ctas.setNatur(c.naturaleza=='DEUDORA'?'D':'A')
				c.subCuentas.each{ c2->
					if(c2.cuentaSat && c2.cuentaSat.codigo){
						Ctas ctas2=catalogo.addNewCtas()
						ctas2.setCodAgrup(getCodigo(c2.cuentaSat.codigo))
						ctas2.setNumCta(c2.clave)
						ctas2.setDesc(XmlUtil.escapeXml(c.descripcion))
						ctas2.setSubCtaDe(c2.padre.clave)
						ctas2.setNivel(2)
						ctas2.setNatur(c2.naturaleza=='DEUDORA'?'D':'A')
					}
				}
				
    		}
    	}

		if(empresa.getCertificado()){
			byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
			catalogo.setCertificado(new String(encodedCert))
			catalogo.setNoCertificado(empresa.numeroDeCertificado);
			def cadena=cadenaBuilder.generarCadenaParaCatalogo(documento)
			//log.info 'Cadena de balanza: '+cadena
			def sello=selladorDigital.sellar(empresa.privateKey,cadena)
			log.info 'Sello: '+sello
			catalogo.setSello(sello)
		}    	
		

    	validarDocumento(documento)
    	

		return documento
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
			return Mes.Enum.forInt(Mes.INT_X_12);
		case 13:
			return Mes.Enum.forInt(Mes.INT_X_13)
		default:
			return null;
		}
	}

	public CCodAgrup.Enum getCodigo(def val){
		
		//return CCodAgrup.Enum.forInt(CCodAgrup.INT_X_100);
		def res=CCodAgrup.Enum.forString(val);
		
		return res
	}

//
//cursor.setAttributeText(qname,"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd" )
/**
*
*xsi:schemaLocation='www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd' 
**/
	def depurar(CatalogoDocument document){

		XmlCursor cursor=document.newCursor()
		if(cursor.toFirstChild()){
			QName qname=new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation","xsi")
			cursor.setAttributeText(qname,"www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd" )
			cursor.toNextToken()
			cursor.insertNamespace("catalogocuentas", "http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas")
		}
	}


	def validarDocumento(CatalogoDocument document) {
		List<XmlValidationError> errores=findErrors(document)
		if(errores.size()>0){
		 	StringBuffer buff=new StringBuffer();
		 	for(XmlValidationError e:errores){
		 		buff.append(e.getMessage()+"\n");
		 	}
		 	throw new RuntimeException("Error de validacion en XML para el catalodo de cuentas "+buff.toString())
		}
		//return errores
	}
	
	def findErrors(XmlObject node){
		XmlOptions options=new XmlOptions();
		List errors=new ArrayList();
		options.setErrorListener(errors);
		node.validate(options);
		return errors;
		
	}

}
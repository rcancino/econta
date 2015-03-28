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

class CatalogoDeCuentasBuilder{


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
			return Mes.Enum.forInt(Mes.INT_X_11);
		default:
			return null;
		}
	}

	public CCodAgrup.Enum getCodigo(def val){
		
		//return CCodAgrup.Enum.forInt(CCodAgrup.INT_X_100);
		def res=CCodAgrup.Enum.forString(val);
		
		return res
	}

	def depurar(CatalogoDocument document){

		XmlCursor cursor=document.newCursor()
		if(cursor.toFirstChild()){
			QName qname=new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation","xsi")
			cursor.setAttributeText(qname,"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd" )
			cursor.toNextToken()
			cursor.insertNamespace("catalogocuentas", "http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas")
		}
	}

}
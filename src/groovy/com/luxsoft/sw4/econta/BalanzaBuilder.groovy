package com.luxsoft.sw4.econta

import org.apache.xmlbeans.XmlCursor
import org.apache.xmlbeans.XmlOptions

// import mx.luxsoft.econta.x1.BalanzaDocument
// import mx.luxsoft.econta.x1.BalanzaDocument.Balanza
// import mx.luxsoft.econta.x1.BalanzaDocument.Balanza.Mes
// import mx.luxsoft.econta.x1.BalanzaDocument.Balanza.Ctas
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument.Balanza
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument.Balanza.Ctas
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument.Balanza.Mes
import org.apache.commons.lang.math.NumberUtils
import com.luxsoft.sw4.Empresa
import groovy.xml.XmlUtil
import javax.xml.namespace.QName
import org.apache.xmlbeans.XmlCursor
import org.apache.xmlbeans.XmlValidationError
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlObject
import org.bouncycastle.util.encoders.Base64
import org.apache.commons.logging.LogFactory
import org.apache.xmlbeans.XmlDateTime
import java.text.SimpleDateFormat

class BalanzaBuilder {

	def selladorDigital

	def cadenaBuilder

	private static final log=LogFactory.getLog(this)

	public BalanzaDocument build(com.luxsoft.sw4.econta.Balanza b){

		BalanzaDocument document=BalanzaDocument.Factory.newInstance();
		Balanza balanza=document.addNewBalanza();
		balanza.setVersion("1.1")
		balanza.setAnio(b.ejercicio)
		balanza.setMes(getMes(NumberUtils.toInt(b.mes)))
		balanza.setRFC(b.rfc)
		balanza.setTipoEnvio(b.tipo);

		depurar(document)
		b.partidas.each{
			Ctas ctas=balanza.addNewCtas();
			ctas.setNumCta(it.cuenta.clave)
			ctas.setSaldoIni(it.saldoIni)
			ctas.setDebe(it.debe)
			ctas.setHaber(it.haber)
			ctas.setSaldoFin(it.saldoFin)
		}

		def empresa=b.empresa
		//balanza.setFechaModBal(arg0);
		if(b.tipo=='C'){
			balanza.setFechaModBal(toXmlDate(new Date()).getCalendarValue())
		}
		byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
		balanza.setCertificado(new String(encodedCert))
		balanza.setNoCertificado(empresa.numeroDeCertificado);
		def cadena=cadenaBuilder.generarCadenaParaBalanza(document)
		//log.info 'Cadena de balanza: '+cadena
		def sello=selladorDigital.sellar(empresa.privateKey,cadena)
		log.info 'Sello: '+sello
		balanza.setSello(sello)

		def errors=validarDocumento(document)
		if(errors){
			throw new RuntimeException("Errores de generacion de archivo XML para balanza "+errors.toString())
		}

		return document;
	}

	// public Mes.Enum getMes(int xmes){
	// 	//return CCodAgrup.Enum.forInt(100);
	// 	return Mes.Enum.forInt(xmes)
	// }


	Mes.Enum getMes(int xmes){
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

	def depurar(BalanzaDocument document){

		XmlCursor cursor=document.newCursor()
		if(cursor.toFirstChild()){
			QName qname=new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation","xsi")
			cursor.setAttributeText(qname
				,"www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion/BalanzaComprobacion_1_1.xsd" )
			cursor.toNextToken()
			cursor.insertNamespace("BCE", " http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion")
		}
	}


	def validarDocumento(BalanzaDocument document) {
		List<XmlValidationError> errores=findErrors(document)
		if(errores.size()>0){
			StringBuffer buff=new StringBuffer();
			for(XmlValidationError e:errores){
				buff.append(e.getMessage()+"\n");
			}
		}
		return errores
	}
	
	def findErrors(XmlObject node){
		XmlOptions options=new XmlOptions();
		List errors=new ArrayList();
		options.setErrorListener(errors);
		node.validate(options);
		return errors;
		
	}

	BalanzaDocument cargar(com.luxsoft.sw4.econta.Balanza b){
		ByteArrayInputStream is=new ByteArrayInputStream(b.getXml())
		BalanzaDocument document=BalanzaDocument.Factory.parse(is)
		return document
	}

	XmlDateTime toXmlDate(Date fecha){
		Calendar c=Calendar.getInstance();
		c.setTime(fecha)
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		XmlDateTime xmlDateTime = XmlDateTime.Factory.newInstance()
		xmlDateTime.setStringValue(df.format(c.getTime()))
		return xmlDateTime
	}

}
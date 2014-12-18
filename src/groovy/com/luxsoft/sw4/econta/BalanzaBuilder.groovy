package com.luxsoft.sw4.econta

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import mx.luxsoft.econta.x1.BalanzaDocument;
import mx.luxsoft.econta.x1.BalanzaDocument.Balanza;
import mx.luxsoft.econta.x1.BalanzaDocument.Balanza.Mes;
import mx.luxsoft.econta.x1.BalanzaDocument.Balanza.Ctas;
import org.apache.commons.lang.math.NumberUtils

import com.luxsoft.sw4.Empresa

class BalanzaBuilder {

	static BalanzaDocument build(com.luxsoft.sw4.econta.Balanza b){

		BalanzaDocument document=BalanzaDocument.Factory.newInstance();
		Balanza balanza=document.addNewBalanza();
		balanza.setAno(b.ejercicio);
		balanza.setMes(getMes(NumberUtils.toInt(b.mes)));
		balanza.setRFC(b.rfc)
		balanza.setTotalCtas(b.partidas.size())
		balanza.setVersion(b.versionSat)

		b.partidas.each{
			Ctas ctas=balanza.addNewCtas();
			ctas.setNumCta(it.cuenta.clave)
			ctas.setSaldoIni(it.saldoIni)
			ctas.setDebe(it.debe)
			ctas.setHaber(it.haber)
			ctas.setSaldoFin(it.saldoFin)
		}

		return document;
	}

	static BalanzaDocument cargar(com.luxsoft.sw4.econta.Balanza b){
		ByteArrayInputStream is=new ByteArrayInputStream(b.getXml())
		BalanzaDocument document=BalanzaDocument.Factory.parse(is)
		return document
	}


	static Mes.Enum getMes(int xmes){
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

	static XmlOptions getOptions(){
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
package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import org.apache.xmlbeans.XmlCursor
import org.apache.xmlbeans.XmlOptions
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument


@Transactional
class BalanzaService {

	def balanzaBuilder

    def generarXml(Balanza balanza) {
    	BalanzaDocument document=balanzaBuilder.build(balanza)
		ByteArrayOutputStream os=new ByteArrayOutputStream()
		document.save(os, balanzaBuilder.getOptions())
		balanza.xml=os.toByteArray()
		balanza.save failOnError:true
		return balanza
    }


    
}

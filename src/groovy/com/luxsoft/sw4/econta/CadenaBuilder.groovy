package com.luxsoft.sw4.econta


import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.ResourceLoader
import mx.gob.sat.esquemas.contabilidadE.x11.catalogoCuentas.CatalogoDocument
import mx.gob.sat.esquemas.contabilidadE.x11.balanzaComprobacion.BalanzaDocument


/**
 * Generador de cadena original
 * 
 * @author Ruben Cancino 
 *
 */
class CadenaBuilder implements ResourceLoaderAware{
	
	File catalogoXsltFile
	File balanzaXsltFile
	
	/**
	 * Genera la cadena original para el sellar el catalogo de cuentas
	 * 
	 * @return La cadena original
	 */
	String generarCadenaParaCatalogo(CatalogoDocument document){
		TransformerFactory factory=TransformerFactory.newInstance()
		if(!catalogoXsltFile){
			catalogoXsltFile=resourceLoader.getResource("WEB-INF/sat/CatalogoCuentas_1_1.xslt").getFile()
		assert catalogoXsltFile.exists(),"No existe el archivo xslt para la cadena del catalogo de cuentas: "+catalogoXsltFile.getPath()
		}
		StreamSource source=new StreamSource(catalogoXsltFile);
		Transformer transformer=factory.newTransformer(source);
		Writer writer=new StringWriter();
		StreamResult out=new StreamResult(writer);
		Source so=new DOMSource(document.getDomNode());
		transformer.transform(so, out);
		return writer.toString();
		
	}

	String generarCadenaParaBalanza(BalanzaDocument document){
		TransformerFactory factory=TransformerFactory.newInstance()
		//if(!balanzaXsltFile){
			balanzaXsltFile=resourceLoader.getResource("WEB-INF/sat/BalanzaComprobacion_1_1.xslt").getFile()
			assert balanzaXsltFile.exists(),"No existe el archivo xslt para la cadena de la balanza: "+balanzaXsltFile.getPath()
		//}
		StreamSource source=new StreamSource(balanzaXsltFile);
		Transformer transformer=factory.newTransformer(source);
		Writer writer=new StringWriter();
		StreamResult out=new StreamResult(writer);
		Source so=new DOMSource(document.getDomNode());
		transformer.transform(so, out);
		return writer.toString();
		
	}
	
	ResourceLoader resourceLoader

	@Override
	public void setResourceLoader(ResourceLoader arg0) {
		resourceLoader=arg0
		
	}
	

}

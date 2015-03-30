package com.luxsoft.sw4

import com.luxsoft.sw4.Direccion
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import org.apache.commons.lang.exception.ExceptionUtils
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='rfc')
class Empresa {

	String nombre
	String clave
	String rfc
	String regimen
	String numeroDeCertificado
	byte[] certificadoDigital
	byte[] llavePrivada
	String passwordPfx
	X509Certificate certificado
	PrivateKey privateKey

	Direccion direccion

	Date dateCreated
	Date lastUpdated


    static constraints = {
    	clave minSize:3,maxSize:15
    	rfc size:12..13
    	nombre(blank:false,maxSize:255,unique:true)
		rfc(blank:false,minSize:12,maxSize:13)
		direccion(nullable:false)
		regimen (nullable:true,maxSize:300)
		numeroDeCertificado(nullable:true,minSize:1,maxSize:20)
		certificadoDigital(nullable:true,maxSize:1024*1024*5)
		llavePrivada(nullable:true,maxSize:1024*1024*5)
		
    }

    static embedded = ['direccion']

    static transients = ['certificado','certificadoPfx','privateKey']

    String toString(){
    	return "$nombre"
    }

    def beforeInsert() {
		capitalizarNombre()
	}

	def beforeUpdate() {
		if (isDirty('nombre') || isDirty('clave')) {
			capitalizarNombre()
		}
	}

	private capitalizarNombre(){
		nombre=nombre.toUpperCase()
		clave=clave.toUpperCase()
	}
	X509Certificate getCertificado(){
		
		if(certificadoDigital && !certificado){
			//assert certificadoDigital,'Debe cargar el binario del certificado '
			try {
				
				log.info('Cargando certificado digital en formato X509')
				CertificateFactory fact= CertificateFactory.getInstance("X.509","BC")
				InputStream is=new ByteArrayInputStream(certificadoDigital)
				certificado = (X509Certificate)fact.generateCertificate(is)
				certificado.checkValidity()
					//is.closeQuietly();
				is.close();
				this.certificado=certificado
			} catch (Exception e) {
				e.printStackTrace()
				println 'Error tratando de leer certificado en formato X509 :'+ExceptionUtils.getRootCauseMessage(e)
			}
			
			
		}
		
		return certificado;
	}
	
	String getCertificadoInfo(){
		return "$certificado?.subjectX500Principal"
	}
	
	PrivateKey getPrivateKey(){
		if(!privateKey && llavePrivada){
			try {
				final byte[] encodedKey=llavePrivada
				PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(encodedKey)
				final  KeyFactory keyFactory=KeyFactory.getInstance("RSA","BC")
				this.privateKey=keyFactory.generatePrivate(keySpec)
			} catch (Exception e) {
				e.printStackTrace()
				println 'Error tratando de leer llave privada :'+ExceptionUtils.getRootCauseMessage(e)
			}
			
		}
		return privateKey;
	}
}




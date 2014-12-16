package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import groovy.sql.Sql
import com.luxsoft.sw4.Empresa
import org.apache.commons.lang.StringUtils

@Transactional
class ImportadorDeBalanzaService {

	def dataSource_impap

    def importar(Empresa empresa,Integer ejercicio,Integer mes) {
    	log.info "Importando balanza de $empresa $ejercicio -  $mes"
    	switch(empresa.clave) {
    		case 'IMPAP':
    			importarImpap(empresa,ejercicio,mes)
    			break
    		default:
    			break
    	}

    }

    def importarImpap(Empresa empresa,Integer ejercicio,Integer mes){

    	def db=new Sql(dataSource_impap)
    	def found=Balanza.findByEmpresaAndEjercicioAndMes(empresa,ejercicio,mes)
    	if(!found){
    		found=new Balanza(empresa:empresa
    			,ejercicio:ejercicio
    			,mes:StringUtils.leftPad(mes.toString(), 2, '0')
    			,rfc:empresa.rfc)
    	}
    	if(found.acuse){
    		throw new BalanzaException(message:'Balanza ya cerrada no es modificable')
    	}
    	def res=db.eachRow(impapSql,[ejercicio,mes]) { row ->
    		def cuenta=Cuenta.findByEmpresaAndClave(empresa,row.clave)
    		
    		if(cuenta){
    			found.addToPartidas(cuenta:cuenta
    				,saldoIni:row.saldo_inicial
    				,debe:row.debe
    				,haber:row.haber
    				,saldoFin:row.saldo_final
    				)
    			log.info "Cuenta registrada: $cuenta.clave"
    		}else{
    			log.info "Cuenta no registrada $cuenta"
    		}
    		//if(!cuenta) throw new BalanzaException(message:"No existe la cuenta: "+row.clave+ " para la empresa: $empresa")

    		

    	}
	    
	    found.save failOnError:true
	    return found
    }


    def impapSql=""" 
    	SELECT (SELECT rfc FROM empresa ) as rfc				
		,(SELECT COUNT(*) FROM saldo_por_cuenta_contable X JOIN cuenta_contable Y ON(X.cuenta_id=Y.ID) WHERE Y.padre_id IS NULL AND X.YEAR=2014 AND X.MES=7 AND X.CUENTA_ID NOT IN(29,33,254,40,41,42,43))  AS CTAS				
		,S.YEAR,S.MES,S.CUENTA_ID ,C.CLAVE,C.DESCRIPCION,S.saldo_inicial,S.DEBE,S.HABER,S.saldo_final  				
		FROM saldo_por_cuenta_contable S JOIN cuenta_contable C ON(S.cuenta_id=C.ID) 				
		WHERE YEAR=? AND MES=? AND S.CUENTA_ID NOT IN(29,33,168,167,254,255,40,228,229,230,231,232,233,234,41,235,236,237,238,239,240,241,42,242,243,244,43,245,246,247,308)				
    """
}

class BalanzaException extends RuntimeException{
	String message
	Exception e
}

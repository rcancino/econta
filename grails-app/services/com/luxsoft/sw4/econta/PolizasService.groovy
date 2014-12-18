package com.luxsoft.sw4.econta

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import groovy.sql.Sql
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.econta.Polizas

@Transactional
class PolizasService {

	def dataSource_impap

    def importar(Empresa empresa,Integer ejercicio,Integer mes) {
    	
    	switch(empresa.clave) {
    		case 'IMPAP':
    			importarImpap(empresa,ejercicio,mes)
    			break
    		default:
    			break
    	}

    }

    @NotTransactional
    def importarImpap(Empresa empresa,Integer ejercicio,Integer mes){

        log.info "IMPORTANDO polizas de $empresa $ejercicio -  $mes"
    	
    	def found=Polizas.findByEmpresaAndEjercicioAndMes(empresa,ejercicio,mes)
    	if(!found){
    		found=new Polizas(
    			empresa:empresa
    			,ejercicio:ejercicio
    			,mes:mes
    			,rfc:empresa.rfc)
    	}
    	if(found.acuse){
    		throw new PolizaException(message:'Poliza ya cerrada no es modificable')
    	}
        def db=new Sql(dataSource_impap)
        //println impapSql
    	def res=db.eachRow(polizaImpapSql,[ejercicio,mes]) { row ->
            println 'Procesando: '+row

    	}
        
	    found.save failOnError:true
	    return found
    }



    def polizaImpapSql="""
		SELECT p.id,CASE WHEN P.TIPO='INGRESO' THEN 1 WHEN P.TIPO='EGRESO' THEN 2 ELSE 3 END AS Tipo,P.TIPO,p.folio as Num,date(P.Fecha) as Fecha,P.Descripcion as Concepto
		FROM poliza P
		where YEAR(fecha)=? and month(fecha)=?
    """

    def polizaSqlDet="""
SELECT D.id,D.cuenta_id,C.clave as NumCta,C.descripcion as Concepto,D.debe,D.haber,'MXN' AS Moneda,1 as TipCambio,D.poliza_id,d.entidad,d.referencia,d.origen,d.descripcion
FROM poliza P 
JOIN poliza_det D ON(D.poliza_id=P.id)
JOIN cuenta_contable C ON(C.id=D.cuenta_id)
WHERE p.id=3175
    """
}


class PolizaException extends RuntimeException{
	String message
	Exception e
}

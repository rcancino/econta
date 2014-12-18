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

    @Transactional
    def eliminarPolizas(Polizas polizas){
        if(polizas.acuse){
            throw new PolizaException(message:'Poliza ya  enviada no se puede eliminar')
        }
        polizas.delete flush:true
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
    	}else{
            if(found.acuse){
                throw new PolizaException(message:'Poliza ya cerrada no es modificable')
            }
            found.partidas.clear()
        }
    	
        def db=new Sql(dataSource_impap)
        //println impapSql
    	def res=db.eachRow(polizaImpapSql,[ejercicio,mes]) { row ->
            //println 'Procesando: '+row
            def pol=new Poliza(tipo:row.tipo
                ,num:row.num
                ,descripcion:row.concepto
                ,fecha:row.fecha
                ,concepto:row.concepto
                ,origen:row.id
                ,tipoOrigen:row.tipoOrigen)
            

            db.eachRow(polizaImpapSqlDet,[row.id]) { r2->
                pol.addToPartidas(
                    numCta:r2.numCta
                    ,concepto:r2.concepto
                    ,debe:r2.debe
                    ,haber:r2.haber
                    ,moneda:r2.moneda
                    ,tipCamb:r2.tipCambio
                )
            }

            found.addToPartidas(pol)

    	}
        
	    found.save failOnError:true
	    return found
    }



    def polizaImpapSql="""
		SELECT p.id,CASE WHEN P.TIPO='INGRESO' THEN 1 WHEN P.TIPO='EGRESO' THEN 2 ELSE 3 END AS tipo
            ,p.TIPO as tipoOrigen
            ,p.folio as num,date(P.Fecha) as fecha
            ,P.Descripcion as concepto
		FROM poliza p
		where YEAR(fecha)=? and month(fecha)=?
    """

    def polizaImpapSqlDet="""
        SELECT D.id,D.cuenta_id,C.clave as numCta,C.descripcion as concepto,D.debe as debe
            ,D.haber as haber,'MXN' AS moneda,1 as tipCambio
            ,D.poliza_id,d.entidad
            ,d.referencia
            ,d.origen
            ,d.descripcion
        FROM poliza P 
        JOIN poliza_det D ON(D.poliza_id=P.id)
        JOIN cuenta_contable C ON(C.id=D.cuenta_id)
        WHERE p.id=?
    """
}


class PolizaException extends RuntimeException{
	String message
	Exception e
}

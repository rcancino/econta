package com.luxsoft.sw4.econta

import grails.transaction.Transactional

@Transactional
class ImportadorDeBalanzaService {

    def serviceMethod() {

    }


    def impapSql=""" 
    	SELECT (SELECT rfc FROM empresa ) as rfc				
		,(SELECT COUNT(*) FROM saldo_por_cuenta_contable X JOIN cuenta_contable Y ON(X.cuenta_id=Y.ID) WHERE Y.padre_id IS NULL AND X.YEAR=2014 AND X.MES=7 AND X.CUENTA_ID NOT IN(29,33,254,40,41,42,43))  AS CTAS				
		,S.YEAR,S.MES,S.CUENTA_ID ,C.CLAVE,C.DESCRIPCION,S.saldo_inicial,S.DEBE,S.HABER,S.saldo_final  				
		FROM saldo_por_cuenta_contable S JOIN cuenta_contable C ON(S.cuenta_id=C.ID) 				
		WHERE YEAR=2014 AND MES=7 AND S.CUENTA_ID NOT IN(29,33,168,167,254,255,40,228,229,230,231,232,233,234,41,235,236,237,238,239,240,241,42,242,243,244,43,245,246,247,308)				
    """
}

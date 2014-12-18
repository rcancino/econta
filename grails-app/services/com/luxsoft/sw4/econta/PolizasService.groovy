package com.luxsoft.sw4.econta

import grails.transaction.Transactional

@Transactional
class PolizasService {

    def serviceMethod() {

    }



    def polizaSql="""
	SELECT p.id,CASE WHEN P.TIPO='INGRESO' THEN 1 WHEN P.TIPO='EGRESO' THEN 2 ELSE 3 END AS Tipo,P.TIPO,p.folio as Num,date(P.Fecha) as Fecha,P.Descripcion as Concepto
FROM poliza P
where YEAR(fecha)=2014 and month(fecha)=7 
    """

    def polizaSqlDet="""
SELECT D.id,D.cuenta_id,C.clave as NumCta,C.descripcion as Concepto,D.debe,D.haber,'MXN' AS Moneda,1 as TipCambio,D.poliza_id,d.entidad,d.referencia,d.origen,d.descripcion
FROM poliza P 
JOIN poliza_det D ON(D.poliza_id=P.id)
JOIN cuenta_contable C ON(C.id=D.cuenta_id)
WHERE p.id=3175
    """
}

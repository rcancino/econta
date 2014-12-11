package com.luxsoft.sw4.econta

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='cuenta')
class BalanzaDet {

	Cuenta cuenta
	BigDecimal saldoIni
	BigDecimal debe
	BigDecimal haber
	BigDecimal saldoFin

	static belongsTo = [balanza: Balanza]

    static constraints = {
    	
    }


}

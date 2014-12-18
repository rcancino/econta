package com.luxsoft.sw4.econta

class PolizaDet {

	String numCta
	String concepto
	BigDecimal debe
	BigDecimal haber
	String moneda
	BigDecimal tipCamb

	static belongsTo = [poliza: Poliza]


    static constraints = {
    }
}

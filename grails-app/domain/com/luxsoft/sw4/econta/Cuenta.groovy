package com.luxsoft.sw4.econta

import com.luxsoft.sw4.Empresa
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


//@EqualsAndHashCode(includes='cuenta')
class Cuenta {
	
	Empresa empresa
	
	Integer mes
	Integer ejercicio

	String clave
	String descripcion
	String tipo
	String subTipo
	Cuenta padre
	Integer nivel=1
	boolean detalle=false
	boolean deResultado=false

	String naturaleza
	boolean presentacionContable=false
	boolean presentacionFiscal=false
	boolean presentacionFinanciera=false
	boolean presentacionPresupuestal=false
	
	CuentaSat cuentaSat

	Long origen

	Date dateCreated
	Date lastUpdated
	
	static hasMany = [subCuentas:Cuenta]

    static constraints = {
		clave nullable:true,maxSize:100 //,unique:['empresa','descripcion']
		descripcion(blank:false,maxSize:300)
		detalle(nullable:false)
		tipo(nullable:false,inList:['ACTIVO','PASIVO','CAPITAL','ORDEN'])
		subTipo(nullable:true)
		naturaleza(inList:['DEUDORA','ACREEDORA','MIXTA'])
		cuentaSat nullable:true
		nivel range:1..5
		mes range:1..12
    	ejercicio range:2014..2099
    }
	
	static mapping ={
		subCuentas cascade: "all-delete-orphan"
	}
	
	String toString(){
		return clave+" "+descripcion
	}
	
	boolean equals(Object obj){
		if(!obj.instanceOf(Cuenta))
			return false
		if(this.is(obj))
			return true
		def eb=new EqualsBuilder()
		eb.append(empresa, obj.empresa)
		eb.append(clave, obj.clave)
		return eb.isEquals()
	}
	
	int hashCode(){
		def hb=new HashCodeBuilder(17,35)
		hb.append(empresa)
		hb.append(clave)
		return hb.toHashCode()
	}


	
	static Cuenta buscarPorClave(String clave){
		return Cuenta.findByClave(clave)
	}
}

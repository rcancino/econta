// Place your Spring DSL code here

import com.luxsoft.sw4.econta.CatalogoDeCuentasBuilder
import com.luxsoft.sw4.econta.CadenaBuilder
import com.luxsoft.sw4.econta.BalanzaBuilder
import com.luxsoft.sw4.econta.SelladorDigital

beans = {

	catalogoDeCuentasBuilder(CatalogoDeCuentasBuilder){
		cadenaBuilder=ref("cadenaBuilder")
		selladorDigital=ref("selladorDigital")
	}

	balanzaBuilder(BalanzaBuilder){
		cadenaBuilder=ref("cadenaBuilder")
		selladorDigital=ref("selladorDigital")
	}

	cadenaBuilder(CadenaBuilder){}

	selladorDigital(SelladorDigital){}
}

package com.luxsoft.sw4

class CoreFilters {

    def filters = {
        all(controller:'*', action:'*') {

            before = {
                
                if(!session.ejercicion){
                    session.ejercicio=Periodo.obtenerYear(new Date())
                }
                if(!session.empresa){
                    flash.message="Seleccion una empresa"
                    //redirect controller:'home',action:'seleccionarEmpresa'
                }
                if(!session.mes){
                    session.mes=Periodo.obtenerMes(new Date())
                }
            }
            after = { Map model ->
                
            }
            afterView = { Exception e ->

            }
        }
        empresaFilter(controller:'login',action:'*'){
            after={ Map model->
                //println 'Resolviendo empresa '+params
                /*
                def empresa=Empresa.findById(params.empresaId)
                session.empresa=empresa
                */
            }
        }

    }
}

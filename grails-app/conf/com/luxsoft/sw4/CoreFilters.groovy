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
    }
}

package com.luxsoft.sw4.econta

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.lang.WordUtils
import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.Empresa




@Secured(["hasAnyRole('ADMINISTRACION','OPERADOR')"])
class ReportController {

	def jasperService

	static defaultAction = "index"

	def index(){}

    def balanza(Balanza balanza){
		def repParams=[:]
		
		repParams['YEAR']=balanza.ejercicio.toString()
		repParams['MES']=balanza.mes.toString()
		repParams['EMPRESA_ID']=balanza.empresa.id
		
		repParams.reportName='BalanzaDeSatEmpresarial'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}

	def catalogoDecuentas(Empresa empresa){
		def repParams=[:]
		repParams['EMPRESA_ID']=empresa.id
		repParams.reportName='CatalogoDeCuentasEmpresarialSatl'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}



	
/*
	def catalogoDeCuentas(){
		if(request.method=='GET'){
			return [tipos:['ACTIVOS','SUSPENDIDOS','TODOS']]
		}
		def repParams=['TIPO':params.tipo]
		repParams['reportName']='CatalogoDeSocios'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}
*/

	

	private runReport(Map repParams){
		File logoFile = grailsApplication.mainContext.getResource("images/kyo_logo.png").file

		if(logoFile.exists()){
			repParams['EMPRESA_LOGO']=logoFile.newInputStream()
		}
		def nombre=WordUtils.capitalize(repParams.reportName)
		log.info "Ejectuando reporte $nombre params:"+repParams
		def reportDef=new JasperReportDef(
			name:nombre
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		return pdfStream
		
	}
	
	
}
/*
class ReportCommand{
	String reportName
	static constraints={
		reportName nullable:false
	}
}

@Validateable 
class FechaCommand extends ReportCommand{
	
	@BindingFormat('dd/MM/yyyy')
	Date fecha=new Date()

	static constraints={
		fecha nullable:false
	}
}
*/
/*
@Validateable 
class PeriodoCommand extends ReportCommand{
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial=new Date()-30
	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal=new Date()

	static constraints={
		fechaInicial nullable:false
		fechaFinal nullable:false
	}

	Periodo toPeriodo(){
		retur new Periodo(fechaInicial,fechaFinal)
	}
}
*/


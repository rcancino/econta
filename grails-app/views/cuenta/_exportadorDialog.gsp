<div class="modal fade" id="generarXmlDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">XML del catálogo de cuentas</h4>
			</div>
			
			<g:form  class="form-horizontal" controller="exportador" action="exportarCatalogoDeCuentas" >
				<div class="modal-body">
					<f:with bean="${new com.luxsoft.sw4.econta.ExportadorCommand()}">
						<g:hiddenField name="empresa.id" value="${session.empresa.id}"/>
						<f:field property="ejercicio" 
							input-class="form-control" value="${session.ejercicio}"
							input-readonly="readonly"/>
						<f:field property="mes" input-class="form-control"/>
					</f:with>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"value="Aceptar" />
				</div>
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>

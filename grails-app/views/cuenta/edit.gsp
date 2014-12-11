<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Cuenta ${cuentaInstance.clave}</title>
	<asset:stylesheet src="jquery-ui.css"/>
	<asset:javascript src="jquery-ui/autocomplete.js"/>
</head>
<body>

	<content tag="header">
		<h3>Cuenta ${cuentaInstance.clave} - ${cuentaInstance.descripcion}</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Catálogo
  			    </g:link>
  			</li>
  			<li>
  				<a href="#agregarSubcuentaDialog" data-toggle="modal">
  					<span class="glyphicon glyphicon-plus"></span> Agregar cuenta
  				</a>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Datos generales</content>
	
	<content tag="form">

		<ul class="nav nav-tabs" role="tablist" id="myTab">
		  <li role="presentation" class="active"><a href="#generales" role="tab" data-toggle="tab">Generales</a></li>
		  <li role="presentation"><a href="#subcuentas" role="tab" data-toggle="tab">SubCuentas</a></li>
		</ul>

		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="generales">
  				<g:hasErrors bean="${cuentaInstance}">
  		            <div class="alert alert-danger">
  		                <g:renderErrors bean="${cuentaInstance}" as="list" />
  		            </div>
  		        </g:hasErrors>
  		        <g:form class="form-horizontal" action="update" method="PUT">
  		        	<g:hiddenField name="empresa.id" value="${cuentaInstance.empresa.id}"/>
  		        	<g:hiddenField name="id" value="${cuentaInstance.id}"/>
  		        	<g:hiddenField name="version" value="${cuentaInstance.version}"/>
  		        	<f:with bean="${cuentaInstance }">
  		        		<div class="form-group">
  		        			<label class="col-sm-3 control-label">Empresa </label>
  		        			<div class="col-sm-9">
  		        				<strong>
  		        					<p class="form-control-static ">
  		        						${cuentaInstance.empresa.nombre}
  		        					</p>
  		        				</strong>
  		        			</div>
  		        		</div>
  		        		<f:field property="clave" input-class="form-control uppercase-field " input-autofocus="on"/>
  		        		<f:field property="descripcion" input-class="form-control uppercase-field"/>
  		        		<f:field property="tipo" input-class="form-control "/>
  		        		<div class="form-group">
  		        			<label class="col-sm-3 control-label">Cuenta SAT </label>
  		        			<div class="col-sm-9">
  		        				<g:hiddenField id="cuentaSatId" name="cuentaSat.id" value="${cuentaInstance?.cuentaSat?.id}"/>
  		        				<input id="cuenta" name="cuentaSat.nombre" type="text" 
  		        					class="form-control" placeholder="Seleccione una cuenta" autocomplete="off"
  		        					value="${cuentaInstance?.cuentaSat}">
  		        			</div>
  		        		</div>
  		        	</f:with>
  		        	
  		        	<div class="form-group">
  		            	<div class="col-sm-offset-9 col-sm-2">
  		              		<button type="submit" class="btn btn-default">
  		              			<span class="glyphicon glyphicon-floppy-save"></span> Salvar
  		              		</button>
  		            	</div>
  		          	</div>
  		        </g:form>
			</div>
		<div role="tabpanel" class="tab-pane" id="subcuentas">
					<div class="grid-panel">
						
						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>Clave</th>
									<th>Descripción</th>
									<th>Cuenta SAT</th>
								</tr>
							</thead>
							<tbody>
								<g:each in="${cuentaInstance.subCuentas}" var="row">
									<tr id="${row.id}">
										<td>
											<g:link  action="show" id="${row.id}">
												${fieldValue(bean:row,field:"clave")}
											</g:link>
										</td>
										<td>
											<g:link  action="show" id="${row.id}">
												${fieldValue(bean:row,field:"descripcion")}
											</g:link>
										</td>
										<td>${row.cuentaSat}</td>	
										
										
									</tr>
								</g:each>
							</tbody>
						</table>
					</div>
		</div>
		</div>
		
		
		
		

	</content>

	<content tag="gridPanel">
		
		<div class="modal fade" id="agregarSubcuentaDialog" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h3 class="modal-title" id="myModalLabel">Agregar Sub cuenta a ${cuentaInstance.clave}</h3>
						<p class="center-text"><h4>SAT:(${cuentaInstance?.cuentaSat})</h4></p>
						
					</div>

					<g:form class="form-horizontal" action="search" >
						<g:hiddenField name="cuenta.id" value="${cuentaInstance.id}" />
						<div class="modal-body">
							<div class="form-group">
								<label for="nombre" class="col-sm-2 control-label">Clave</label>
								<div class="col-sm-10">
									<g:field id="clave" type="text" placeholder="Clave" 
										name="clave" class="form-control"  />
								</div>
							</div>
							
							<div class="form-group">
								<label for="descripcion" class="col-sm-2 control-label">Descripción</label>
								<div class="col-sm-10">
									<g:field  type="text" name="descripcion" class="form-control"  />
								</div>
							</div>

							<div id="targetSub" class="form-group">
	  		        			<label class="col-sm-2 control-label">Cuenta SAT </label>
	  		        			<div class="col-sm-10">
	  		        				<g:hiddenField id="subcuentaSatId" name="cuentaSat.id" />
	  		        				<input id="subcuenta" name="cuentaSat.nombre" type="text" 
	  		        					class="form-control" placeholder="Seleccione una cuenta" autocomplete="off">
	  		        			</div>
							</div>
							
							
						</div>	
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
							<g:submitButton class="btn btn-primary" name="aceptar"
									value="Aceptar" />
						</div>
					</g:form>

				</div>
				<!-- moda-content -->
			</div>
			<!-- modal-di -->

		</div>


		
	</content>

<content tag="javascript">
	<script type="text/javascript">
	$(document).ready(function(){
		$("#cuenta").autocomplete({
			//source:'/kio-core/socio/getSociosJSON',
			source:'${g.createLink(action:'getCuentasSatJSON') }',
			minLength:3,
			select:function(e,ui){
				console.log('Socio seleccionado: '+ui.item.value);
				$("#cuentaSatId").val(ui.item.id);
				
			}
		});
		
		$("#subcuenta").autocomplete({
			//source:'/kio-core/socio/getSociosJSON',
			source:'${g.createLink(action:'getCuentasSatJSON') }',
			minLength:3,
			select:function(e,ui){
				console.log('Socio seleccionado: '+ui.item.value);
				$("#subcuentaSatId").val(ui.item.id);
				
			},
			appendTo: "#targetSub" 

		});
		
		
	});
	</script>
</content>
	
</body>
</html>
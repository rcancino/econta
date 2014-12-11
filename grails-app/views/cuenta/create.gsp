<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Cuenta nueva</title>
</head>
<body>

	<content tag="header">
		<h3>Registro de cuenta nueva</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Catálogo
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-plus"></span> Nuevo
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Datos generales</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${cuentaInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${cuentaInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save" >
			<g:hiddenField name="empresa.id" value="${session.empresa.id}"/>
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
				<f:field property="naturaleza" input-class="form-control"/>
			</f:with>
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Salvar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		<r:script>
			
		</r:script>

	</content>
	
</body>
</html>
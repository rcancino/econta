<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="layout" content="contabilidad"/>
	<asset:stylesheet src="datatables/dataTables.css"/>
	<asset:javascript src="datatables/dataTables.js"/> 
	<title>Importar</title>
</head>
<body>

<content tag="header">
	<h3>Importar balanza para ${session.empresa}</h3>
</content>
<content tag="operaciones">
	
</content>
<content tag="toolbar">
	
</content>

<content tag="document">
	<div class="row">
		<div class="col-md-12">
			<div class="well">
				<g:hasErrors bean="${importadorCommand}">
		            <div class="alert alert-danger">
		                <g:renderErrors bean="${importadorCommand}" as="list" />
		            </div>
		        </g:hasErrors>
				<g:form class="form-horizontal" action="importarBalanza">
						<f:with bean="${importadorCommand}">
							<f:field property="ejercicio" 
								input-class="form-control" />
							<f:field property="mes" 
								input-class="form-control" />
							
						</f:with>

						<div class="form-group">
					    	<div class="col-sm-offset-8 col-sm-4">
					    		<g:link action="index" class="btn btn-default">
										<span class="glyphicon glyphicon-arrow-left"></span> Cancelar
							    	</g:link>
					      		<button type="submit" class="btn btn-default">
					      			<i class="fa fa-upload"></i></span> Importar
					      		</button>
					    	</div>
					  	</div>
				</g:form>
			</div>
		</div>
	</div>

</content><!-- End content document -->

<content tag="searchForm">
	%{-- <g:render template="search"/> --}%
</content>
<content tag="javascript">
	
	
</content>

	
</body>
</html>
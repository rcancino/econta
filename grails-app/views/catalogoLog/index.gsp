<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="layout" content="catalogos"/>
	<asset:stylesheet src="datatables/dataTables.css"/>
	<asset:javascript src="datatables/dataTables.js"/> 
	<title>Bitácora de envíos</title>
</head>
<body>
<content tag="header">
	<h3>Catálogo de cuentas ${session.empresa}  Ejercicio:${session.ejercicio}</h3>
</content>
<content tag="operaciones">
	
	<li><g:link controller="exportador" action="exportarCatalogoDeCuentas" id="${session.empresa.id}">
  				<i class="fa fa-file-code-o"></i></span> Generar XML 
  		</g:link>
  	</li>
  	<li>
  		<g:link action="registrarAcuse" class="">
  			<i class="fa fa-file-text-o"></i></span> Acuse
  		</g:link>
  	</li>
</content>

<content tag="reportes">
	<li>
		<g:link controller="report" action="catalogoDecuentas" id="${session.empresa.id}"> Bitacora</g:link>
	</li>
	<li>
		<g:link controller="report" action="catalogoDecuentas" id="${session.empresa.id}"> XML</g:link>
	</li>
</content>

<content tag="document">
<div class="">
	<table id="grid" class="table table-striped table-bordered table-condensed">

		<thead>
			<tr>
				<th>Empresa</th>
				<th>Ejercicio</th>
				<th>Mes</th>
				<th>Acuse SAT</th>
				<th>Desc</th>
				<th>Ver</th>
				<th>Creado</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${catalogoLogInstanceList}" var="row">
				<tr id="${row.id}">
					<td>${row.empresa.clave}</td>
					<td>${row.ejercicio}</td>
					<td>${row.mes}</td>
					<td>
						<g:if test="${row.acuse}">
							<g:link  action="descargarAcuse" id="${row.id}">
								Descargar
							</g:link>
						</g:if>
						<g:else>
							<a href="#uploadAcuseDialog" data-toggle="modal">Cargar</a>
						</g:else>
					</td>
					<td>
						<g:link action="descargarXml" id="${row.id}">
							<i class="fa fa-download"></i>
						</g:link>
					</td>	
					<td>
						<g:link action="mostrarXml" id="${row.id}">
							<i class="fa fa-file-code-o"></i>
						</g:link>
					</td>
					<td>${g.formatDate(date:row.dateCreated,format:'dd/MM/yyyy HH:mm')}</td>	
				</tr>
			</g:each>
		</tbody>
	</table>
	<div class="pagination">
		<g:paginate total="${cuentaInstanceCount ?: 0}"/>
	</div>
</div>
</content><!-- End content document -->

<content tag="searchForm">
	<g:render template="uploadFileDialog"/>
	%{-- <g:render template="search"/> --}%
</content>
<content tag="javascript">
	<script type="text/javascript">
		$(document).ready(function(){
			
			$('#grid').dataTable( {
	        	"paging":   false,
	        	"ordering": false,
	        	"info":     false
	        	,"dom": '<"toolbar col-md-4">rt<"bottom"lp>'
	    	} );
	    	
	    	$("#filtro").on('keyup',function(e){
	    		var term=$(this).val();
	    		$('#grid').DataTable().search(
					$(this).val()
	    		        
	    		).draw();
	    	});

		});
	</script>
	
</content>

	
</body>
</html>
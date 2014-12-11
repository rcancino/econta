<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="layout" content="catalogos"/>
	<title>Empresas</title>
	<asset:stylesheet src="datatables/dataTables.css"/>
	<asset:javascript src="datatables/dataTables.js"/> 
</head>
<body>
<content tag="header">
	<h3>Catálogo de empresas</h3>
</content>

<content tag="document">
<div class="">
	
	
	<table id="grid" class="table table-striped table-bordered table-condensed">

		<thead>
			<tr>
				<th>Folio</th>
				<th>Clave</th>
				<th>Nombre</th>
				<th>RFC</th>
				<th>Modificado</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${empresaInstanceList}" var="row">
				<tr id="${row.id}">
					<td >
						<g:link  action="show" id="${row.id}">
							${fieldValue(bean:row,field:"id")}
						</g:link>
					</td>
					<td >
						<g:link  action="show" id="${row.id}">
							${fieldValue(bean:row,field:"clave")}
						</g:link>
					</td>
					<td>
						<g:link  action="show" id="${row.id}">
							${fieldValue(bean:row,field:"nombre")}
						</g:link>
					</td>
					<td>${fieldValue(bean:row,field:"rfc")}</td>
					<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy HH:mm"/></td>
				</tr>
			</g:each>
		</tbody>
	</table>
	<div class="pagination">
		<g:paginate total="${empresaInstanceCount ?: 0}"/>
	</div>
</div>
</content><!-- End content document -->

<content tag="searchForm">
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
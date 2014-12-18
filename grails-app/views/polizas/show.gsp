<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="grids"/>
	<title>Polizas ${polizasInstance.id}</title>
</head>
<body>

	<content tag="header">
		<g:link action="index">
			<h3>Polizas ${polizasInstance.empresa}  (${polizasInstance.ejercicio} / ${polizasInstance.mes})</h3>
		</g:link>
		<h5>Estatus : ${polizasInstance.acuse?'XML ENVIADO':'REVISION'} </h5>
		<h5>Xml: ${polizasInstance.xml?'GENERADO':'PENDIENTE'} </h5>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index" class="">
  					<span class="glyphicon glyphicon-arrow-left"></span> Polizas
  			    </g:link>
  			</li>
  			<li><g:link action="generarXml" id="${polizasInstance.id}">
  					<i class="fa fa-file-code-o"></i> Generar XML 
  			    </g:link>
  			</li>
  			<g:if test="${polizasInstance.xml}">

  				<li><g:link action="descargarXml" id="${polizasInstance.id}">
  						<i class="fa fa-download"></i> Descargar XML 
  				    </g:link>
  				</li>
  				<li><g:link action="mostrarXml" id="${polizasInstance.id}">
  						<i class="fa fa-desktop"></i> Mostrar XML 
  				    </g:link>
  				</li>
  			</g:if>
	
  			

  			<li><g:link action="registrarAcuse" class="">
  					<i class="fa fa-file-text-o"></i></span> Acuse
  			    </g:link>
  			</li>
		</ul>
	</content>

	<content tag="reportes">
		
		
	</content>
	
	<content tag="formTitle">Polizas</content>

	<content tag="document">
		<div class="grid-panel">
			<table id ="grid" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Folio</th>
						<th>Tipo Origen</th>
						<th>Tipo</th>
						<th>Num</th>
						<th>Fecha</th>
						<th>Concepto</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${polizaInstanceList}" var="row">
						<tr id="${row.id}">
							<td>
								<g:link action="showPoliza" id="${row.id}">
									${row.id}
								</g:link>
							</td>
							<td>${row.tipoOrigen}</td>
							<td>${row.tipo}</td>
							<td>${row.num}</td>
							<td>${g.formatDate(date:row.fecha,format:'dd/MM/yyyy')}</td>
							<td>${row.concepto}</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</content>

	<content tag="gridPanel">

		
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
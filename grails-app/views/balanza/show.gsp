<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="grids"/>
	<title>Balanza ${balanzaInstance.id}</title>
</head>
<body>

	<content tag="header">
		<h3>Balanza ${balanzaInstance.empresa}  (${balanzaInstance.ejercicio} / ${balanzaInstance.mes})</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index" class="">
  					<span class="glyphicon glyphicon-arrow-left"></span> Balanzas
  			    </g:link>
  			</li>
  			<li><g:link action="generarXml" class="">
  					<i class="fa fa-file-code-o"></i></span> Generar XML 
  			    </g:link>
  			</li>

  			<li><g:link action="registrarAcuse" class="">
  					<i class="fa fa-file-text-o"></i></span> Acuse
  			    </g:link>
  			</li>
		</ul>
	</content>

	<content tag="reportes">
		<li>
			<g:link  controller="report" action="balanza" id="${balanzaInstance.id}">
				<i class="fa fa-print"></i> Balanza
			</g:link>
		</li>
		
	</content>
	
	<content tag="formTitle">Cargos y abonos</content>

	<content tag="document">
		<div class="grid-panel">
			<table id ="grid" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Clave</th>
						<th>Descripción</th>
						<th>Saldo Inicial</th>
						<th>Debe</th>
						<th>Haber</th>
						<th>Saldo Final</th>
						
					</tr>
				</thead>
				<tbody>
					<g:each in="${balanzaInstance.partidas}" var="row">
						<tr id="${row.id}">
							<td>
								${fieldValue(bean:row,field:"cuenta.clave")}
							</td>
							<td>
								${fieldValue(bean:row,field:"cuenta.descripcion")}
							</td>
							<td>${g.formatNumber(number:row.saldoIni,type:'currency')}</td>

							<td>${g.formatNumber(number:row.debe,type:'currency')}</td>
							<td>${g.formatNumber(number:row.haber,type:'currency')}</td>
							<td>${g.formatNumber(number:row.saldoFin,type:'currency')}</td>
							
							
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
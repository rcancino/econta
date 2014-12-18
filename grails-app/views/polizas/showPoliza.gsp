<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="grids"/>
	<title>Poliza ${polizaInstance.id}</title>
</head>
<body>

	<content tag="header">
		<g:link action="show" id="${polizaInstance.polizas.id}">
			<h3>Poliza ${polizaInstance.tipo}  (${polizaInstance.num}  ${polizaInstance.descripcion})</h3>
		</g:link>
		
		<h5>Ejercicio : ${polizaInstance.polizas.ejercicio}/${polizaInstance.polizas.mes} </h5>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="show" id="${polizaInstance.polizas.id}">
  					<span class="glyphicon glyphicon-arrow-left"></span> Poliza ${polizaInstance.polizas.id}
  			    </g:link>
  			</li>
		</ul>
	</content>

	<content tag="reportes">
		<li>
			<g:link controller="report" action="reporteDePoliza" id="${polizaInstance.id}">
  				<span class="glyphicon glyphicon-print"></span> Poliza
  			</g:link>
  		</li>
	</content>
	
	<content tag="formTitle">Transacciones</content>

	<content tag="document">
		<div class="grid-panel">
			<table id ="grid" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Cta</th>
						<th>Concepto</th>
						<th>Debe</th>
						<th>Haber</th>
						<th>Moneda</th>
						<th>TC</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${polizaInstance.partidas}" var="row">
						<tr id="${row.id}">
							<td>${row.numCta}</td>
							<td>${row.concepto}</td>
							<td>${g.formatNumber(number:row.debe,type:'currency')}</td>
							<td>${g.formatNumber(number:row.haber,type:'currency')}</td>
							<td>${row.moneda}</td>
							<td>${g.formatNumber(number:row.tipCamb,type:'currency')}</td>
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
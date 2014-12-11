
<nav class="navbar navbar-default navbar-fixed-bottom navbar-inverse" role="navigation">
	<div class="container">
		
		<sec:ifLoggedIn>
			
			
			<a class="navbar-brand" href="#empresaDialog" data-toggle="modal">
				<strong>
				Empresa: "${session.empresa?session.empresa.nombre:'Seleccionar empresa'}"
				</strong>
			</a>
			<p class="navbar-text navbar-left"> 
			
			</p>
			<p class="navbar-text navbar-right">Usuario: 
				<a href="#" class="navbar-link">
					<sec:loggedInUserInfo field="username"/>
				</a>
			</p>
		</sec:ifLoggedIn>
		<sec:ifNotLoggedIn>
			<p class="navbar-text navbar-left"> Ingreso al sistema</p>
		</sec:ifNotLoggedIn>
	</div>
</nav>

<div class="modal fade" id="empresaDialog" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Seleccionar empresa</h4>
			</div>
				<g:form class="form-horizontal" action="cambiarEmpresa" controller="empresa" >
					<div class="modal-body">
					<div class="form-group">
						<label for="empresa" class="col-sm-2 control-label">Empresa</label>
						<div class="col-sm-10">
							<g:select class="form-control"  
								name="id" 
								from="${com.luxsoft.sw4.Empresa.findAll()}" 
								optionKey="id" 
								optionValue="nombre"/>
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
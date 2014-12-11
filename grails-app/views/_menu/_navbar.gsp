<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#mainMenu">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<g:link controller="home" action="index" class="navbar-brand">
				<i class="fa fa-calculator"></i> SW4 eConta
			</g:link>
		</div>

		<sec:ifLoggedIn>
		
		<div class="collapse navbar-collapse" id="mainMenu">
			
			<nav:menu class="nav navbar-nav" scope="user" /> 

			
			<ul class="nav navbar-nav navbar-right">
				<g:render template="/_menu/user"/>
				
			</ul>
			
			
		</div>
		
		</sec:ifLoggedIn>
	</div>
	
</nav>
<g:applyLayout name="application">
    <html>
    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    </html>
    <body>

        <div class="container">

            <div class="row">
                <div class="col-md-12">
                    <div class="alert alert-info">
                        <g:if test="${pageProperty(name:'page.header')}">
                            <g:pageProperty name="page.header"/>
                        </g:if>
                        <g:else> <h3>Catálogos del sistema</h3></g:else>  
                         <g:if test="${flash.message}">
                            <div class="col-md-offset-4">
                                <span class="label label-warning">${flash.message}</span>
                            </div>
                        </g:if>  
                    </div>
                </div>
            </div><!-- end .row 1 Header -->
            
            <div class="row">
            	
            	<div class="col-md-3">

            		<div class="list-group">
                        <g:if test="${pageProperty(name:'page.tasks')}">
                            <g:pageProperty name="page.tasks"/>
                        </g:if>
                        <g:else>
                            
                            <nav:menu id="nav" custom="true" scope="user/catalogos">
                                <p:callTag tag="g:link"
                                           attrs="${linkArgs + [class:active ? 'list-group-item  active' : 'list-group-item ']}">
                                  
                                   <i class="${item.data.icon ?:'fa fa-bars'}"></i>&nbsp;<nav:title item="${item}"/>
                                </p:callTag>
                                
                            </nav:menu>

                           
                            
                        </g:else>  
					</div>

            	</div><!-- End .col-md-2 side bar -->

                <div class="col-md-9">
                    <g:if test="${pageProperty(name:'page.toolbar')}">
                        <g:pageProperty name="page.toolbar"/>    
                    </g:if>
                    <g:else>
                        <div class="row toolbar-panel">
                            <div class="col-md-6">
                                <div class="input-group">
                                  
                                  <input type='text' id="filtro" placeholder="Filtrar" class="form-control" autofocus="on">
                                  <span class="input-group-btn">
                                    <button type="button" name="search"
                                        class="btn btn-default" data-toggle="modal" data-target="#searchDialog">
                                        <i class="fa fa-search"></i> Buscar
                                </button>
                                  </span>
                                </div><!-- /input-group -->
                              </div>
                            
                           

                            <div class="btn-group">
                                
                                <g:link action="index" class="btn btn-default ">
                                    <span class="glyphicon glyphicon-repeat"></span> Refrescar
                                </g:link>
                            </div>
                            <div class="btn-group">
                                <button type="button" name="operaciones"
                                        class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        role="menu">
                                        Operaciones <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <g:if test="${pageProperty(name:'page.operaciones')}">
                                        <g:pageProperty name="page.operaciones"/>    
                                    </g:if>
                                    <g:else>
                                        <li>
                                            <g:link action="create" >
                                                <i class="fa fa-plus"></i> Nuevo
                                            </g:link>
                                        </li>
                                    </g:else>
                                    
                                </ul>
                            </div>
                            <div class="btn-group">
                                <button type="button" name="reportes"
                                        class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        role="menu">
                                        Reportes <span class="caret"></span>
                                </button>

                                <ul class="dropdown-menu">
                                    <g:pageProperty name="page.reportes"/>    
                                    
                                </ul>
                            </div>
                            
                        </div>
                    </g:else>
                </div>
            	
            	<div class="col-md-9">
            		<g:pageProperty name="page.document"/>
            	</div>
            	
            </div><!-- end .row 2  -->
            
            <div class="row">
            	<g:pageProperty name="page.searchForm"/>
            </div>
        
        <g:pageProperty name="page.javascript"/>    
        </div>
        
    </body>
</g:applyLayout>
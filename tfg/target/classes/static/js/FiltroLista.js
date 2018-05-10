
(function(){
    'use strict';
	var $ = jQuery;
	$.fn.extend({
		filterTable: function(){
			return this.each(function(){
				$(this).on('keyup', function(e){
					$('.filterTable_no_results').remove();
					var $this = $(this), 
                        search = $this.val().toLowerCase(), 
                        target = $this.attr('data-filters'), 
                        $target = $(target), 
                        $rows = $target.find('tbody tr');
                        
					if(search == '') {
						$rows.show(); 
					} else {
						$rows.each(function(){
							var $this = $(this);
							$this.text().toLowerCase().indexOf(search) === -1 ? $this.hide() : $this.show();
						})
						if($target.find('tbody tr:visible').length  === 0) {
							var col_count = $target.find('tr').first().find('td').length ;
							var no_results = $('<tr class="filterTable_no_results"><td colspan="'+col_count+'">No results found</td></tr>')
							$target.find('tbody').append(no_results);
						}
					}
				});
			});
		}
	});
	$('[data-action="filter"]').filterTable();
})(jQuery);

$(function(){
    // attach table filter plugin to inputs
	$('[data-action="filter"]').filterTable();
	
	$('.container').on('click', '.panel-heading span.filter', function(e){
		var $this = $(this), 
			$panel = $this.parents('.panel');
		
		$panel.find('.panel-body').slideToggle();
		if($this.css('display') != 'none') {
			$panel.find('.panel-body input').focus();
		}
	});
	$('[data-toggle="tooltip"]').tooltip();
})



function marcarEpisodio(accion, idEpisodio, nombreUsuario){
	var url = "/Episodio/" + accion + "/" + idEpisodio + "/" + nombreUsuario;

	$.post(url).done(function(data){
		actualizarListas(accion, idEpisodio, nombreUsuario);
	});
}

function infoEpisodio(titulo, descripcion, duracion, estreno){

	var mensaje = "<div class = \"row\">" + descripcion +
						"<p>Duracion: " + duracion + "</p>" +
						"<p>Fecha de estreno: " + estreno + "</p>"
					"</div>";
	
	bootbox.dialog({
		title: titulo,
		message: mensaje
	});
}


function eliminarSerieLista(titulo, idSerie, nombre){
	bootbox.confirm({
	    title: "Eliminar " + titulo,
	    message: "¿Esta seguro de que desea eleminar " + titulo + " de su lista de series?",
	    buttons: {
	        cancel: {
	            label: '<i class="fa fa-times"></i> Cancelar'
	        },
	        confirm: {
	            label: '<i class="fa fa-check"></i> Aceptar'
	        }
	    },
	    callback: function (result) {
	        if(result == true){
	        	var url = "/EliminarMiSerie/" + idSerie + "/" + nombre;
	        	$.post(url).done(function(){
	        		location.reload();
	        	});
	        }
	    }
	});
}
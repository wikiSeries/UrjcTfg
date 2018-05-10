/*$(document).ready(function(){
	$.get("https://ipapi.co/json/", function(data){
		document.getElementById("ciudad").value = data.city;
		document.getElementById("pais").value = data.country_name;
		document.getElementById("ip").value = data.ip;
		document.getElementById("provincia").value = data.region;
		document.getElementById("codigoPostal").value = data.postal;
	}, "json");
})*/

var TFG = (function($, Promise){
	
	var DATA = (function(){
		var conexionPromise = new Promise(function(resolve, reject){
			$.ajax({
				url: 'https://ipapi.co/json/',	
				dataType: 'json',
				success: function(data){
					return resolve(data);
				},
				error: function(){
					var error = new Error("Se ha producido un error al obtener la informacion de la conexion del cliente.");
					return reject(error);
				}
			})
		});
		
		function parseConexionToEntity(result){
			var conexion = {};
			conexion.ciudad = result.city;
			conexion.pais = result.country_name;
			conexion.ip = result.ip;
			conexion.provincia = result.region;
			conexion.codigoPostal = result.postal;
			
			return conexion;
		}
		
		return {
			get_ClientConexion: function(){
				return conexionPromise
					.then(function(result){
						return parseConexionToEntity(result);
					});
					
			}
		}
	})();
	
	var VIEW = (function(){
		var viewConfig = {
			idFieldsForm :["ciudad", "pais", "ip", "provincia", "codigoPostal"]
		};
		
		return {
			completeForm: function(dataConexion){
				var ids = viewConfig.idFieldsForm;
				for(i = 0; i < ids.length; i++){
					document.getElementById(ids[i]).value = dataConexion[ids[i]];
				}
			}
		}
	})();
	
	
	function clientConexion(){
		
		this.InfoClientConexion = {};
		this.InfoClientConexion.Loaded = false;
		this.InfoClientConexion.Data = {};
		
		var infoClientConexion = this.InfoClientConexion;
		DATA.get_ClientConexion()
			.then(function(data){
				infoClientConexion.Loaded = true;
				infoClientConexion.Data = data;
				VIEW.completeForm(data);
			})
			.catch(function(error){
				console.error(error);
			});
			
		return infoClientConexion;	
	}
	
	return {
		Conexion: function(){
			return new clientConexion();
		}
	}
	
})($, Promise);

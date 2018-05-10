$(document).ready(function(){
	
	$('.rating').rating();
});


jQuery.fn.extend({
  rating: function(options){
    if(typeof(options)=='undefined') options={};
    var ratings=[];
    objs=this;
    if(objs.length){
      for(var i=0;i<objs.length;i++){
        ratings.push(new simpleRating(options,objs[i]));
      }
    }
  },
});

class simpleRating{
  constructor(options,obj) {
    this.obj=obj;
    this.options=options;
    this.rating=0;
    this.init();
  }

  init(){
	var puntuacion = document.getElementById("controlPuntuacion").getAttribute("data-puntuacionMedia");
	
    var html='<div class="simple-rating star-rating">';
    for(var i=0;i<5;i++){
    	
    	if(i < puntuacion){
			html+='<i class="fa fa-star" data-rating="'+(i+1)+'"></i>';
		}
		else{
			html+='<i class="fa fa-star-o" data-rating="'+(i+1)+'"></i>';
			
		}
    }
    
    html+='</div>';
    
    this.rating = puntuacion;
  
    $(this.obj)
      .attr('type','hidden')
      .after(html);

    $(this.obj).next().children().click({classObj:this},function(e){
    	var puntuado = document.getElementById("controlPuntuacion").getAttribute("data-puntuado");
		if(puntuado != "1"){
			e.data.classObj.rate(this);
		}
    });

    $(this.obj).next().children().mouseenter({classObj:this},function(e){
    	var puntuado = document.getElementById("controlPuntuacion").getAttribute("data-puntuado");
		if(puntuado != "1"){
			e.data.classObj.setstars($(this).data('rating'));
		}
    });

    $(this.obj).next().children().mouseleave({classObj:this},function(e){
    	var puntuado = document.getElementById("controlPuntuacion").getAttribute("data-puntuado");
		if(puntuado != "1"){
			e.data.classObj.setstars(e.data.classObj.rating);
		}
    });
  }

  rate(obj){
		
	var rating=$(obj).data('rating');
	guardarPuntuacion(rating, obj, this.obj, this.rating);
    
  }

  refresh(){
    this.setstars(this.rating);
  }

  setstars(rating){
    var stars=$(this.obj).next().children();
    for(var i=0;i<5;i++){
      var starObj=$(this.obj).next().children()[i];
      if(i<rating){
        $(starObj).removeClass('fa-star-o');
        $(starObj).addClass('fa-star');
      }else{
        $(starObj).addClass('fa-star-o');
        $(starObj).removeClass('fa-star');
      }
    }
  }
}

function guardarPuntuacion(puntuacion, obj, thisObj, thisRating){
	var idSerie = document.getElementById("serieActual").getAttribute("data-idSerie");
	var nombreUsuario = document.getElementById("usuarioActual").getAttribute("data-usuario");
	
	$.get("/DamePuntuacionMediaSerie", {
				puntuacion: puntuacion,
				idSerie: idSerie,
				nombreUsuario: nombreUsuario
			}, "json").done(function(data){
				
				 if(data < 0){
				    	data = 0;
				    }
				    
				    $(obj).parent().prev().val(data);
				    thisRating = data;
				    var stars=$(thisObj).next().children();
				    for(var i=0;i<5;i++){
				      var starObj=$(thisObj).next().children()[i];
				      if(i<data){
				        $(starObj).removeClass('fa-star-o');
				        $(starObj).addClass('fa-star');
				      }else{
				        $(starObj).addClass('fa-star-o');
				        $(starObj).removeClass('fa-star');
				      }
				    }
				    
					document.getElementById("controlPuntuacion").dataset.puntuado = 1;
			});
	
}

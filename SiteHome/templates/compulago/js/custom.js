var generalsPositionBottom;
var caracteristicasPositionTop;
var fichaPositionTop;
var enviosPositionTop;
var garantiaPositionTop;

$(document).ready(function(){
	setTimeout(ocultarNota,5000);
	leerfavoritos();
	$('[data-toggle="tooltip"]').tooltip();
	$('.owl-carousel').owlCarousel({
	    loop:true,
	    margin:10,
	    slideBy:4,
	    autoplayTimeout:10000,
	    responsiveClass:true,
	    rewind:true,
	    autoplay:true,
	    autoplayHoverPause:true,
	    dots:false,
	    navText:['<i class="icon-left-open"><i class="fa fa-angle-left"></i>','<i class="icon-right-open"><i class="fa fa-angle-right"></i></i>'],
	    responsive:{
	        0:{
	            items:1,
	            nav:true
	        },
	        600:{
	            items:3,
	            nav:false
	        },
	        1000:{
	            items:4,
	            nav:true,
	            loop:false
	        }
	    }
	})


	$('.owl-carousel-relacionados').owlCarousel({
	    loop:true,
	    margin:10,
	    slideBy:4,
	    autoplayTimeout:10000,
	    responsiveClass:true,
	    rewind:true,
	    autoplay:true,
	    autoplayHoverPause:true,
	    dots:false,
	    navText:['<i class="icon-left-open"><i class="fa fa-angle-left"></i>','<i class="icon-right-open"><i class="fa fa-angle-right"></i></i>'],
	    responsive:{
	        0:{
	            items:1,
	            nav:true
	        },
	        600:{
	            items:3,
	            nav:false
	        },
	        1000:{
	            items:4,
	            nav:true,
	            loop:false
	        }
	    }
	})



	$('.flexslider').flexslider({
		animation: "slide",
		controlNav: "thumbnails"
	});

    var generalsPositionTop = $('.menu-generals').offset().top;
	generalsPositionBottom = generalsPositionTop + $('.menu-generals').height()-45;

	fichaPositionTop = $('#details').offset().top-90;
	caracteristicasPositionTop = $('#caracteristicas-generales').offset().top-90;
	enviosPositionTop = $('#envios').offset().top-90;
	garantiaPositionTop = $('#garantias').offset().top-90;
});

$(window).scroll(function() {

    if ($(this).scrollTop() > 99){  

    	$('#search').removeClass("sticky");
    	$('header #header-scroll').addClass("sticky");

    	if ($(this).scrollTop() > generalsPositionBottom) {
    		$('.generals-space2').addClass("sticky");
    	}else{
    		$('.generals-space2').removeClass("sticky");
    	}

    	if ($(this).scrollTop() > fichaPositionTop) {
    		$('.pestana').removeClass("active");
    		$('.pestana.pestana-2').addClass("active");
    	}

    	if ($(this).scrollTop() > caracteristicasPositionTop) {
    		$('.pestana').removeClass("active");
    		$('.pestana.pestana-1').addClass("active");
    	} 

    	if ($(this).scrollTop() > enviosPositionTop) {
    		$('.pestana').removeClass("active");
    		$('.pestana.pestana-3').addClass("active");
    	} 

    	if ($(this).scrollTop() > garantiaPositionTop) {
    		$('.pestana').removeClass("active");
    		$('.pestana.pestana-4').addClass("active");
    	} 


    }else{
    	if ($(this).scrollTop() < 100){
    		$('header #header-scroll').removeClass("sticky");
    	}
    };

});

$(function() {
    $('a[href*="#"]:not([href="#"])').click(function() {
      if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
        var target = $(this.hash);
        target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
        if (target.length) {
          $('html, body').animate({
            scrollTop: target.offset().top - 80
          }, 1000);
          return false;
        }
      }
    });
});	

$('.bxslider').bxSlider({
	responsive:true,
	pager:false,
	infiniteLoop:true,
	auto:false,
	pause:50000
});

$("#top-header .opt-left > button").click(function(){ //boton top menu telefonos
	$('#direcciones').removeClass("sticky");
	$('#catalogos').removeClass("sticky");
	if ($('#telefonos').hasClass('sticky')){
		$('#telefonos').removeClass("sticky");
	}else{
		$('#telefonos').addClass("sticky");
	}
});

$(".col-mapaTiendas a").click(function(){
	$('#telefonos').removeClass("sticky");
	$('#catalogos').removeClass("sticky");
	if ($('#direcciones').hasClass('sticky')){
		$('#direcciones').removeClass("sticky");
	}else{
		$('#direcciones').addClass("sticky");
	}
});

$(".col-mapaTiendas button").click(function(){ //boton main menu tiendas
	$('#direcciones').removeClass("sticky");
	$('#catalogos').removeClass("sticky");
	if ($('#telefonos').hasClass('sticky')){
		$('#telefonos').removeClass("sticky");
	}else{
		$('#telefonos').addClass("sticky");
	}
/*	$('#telefonos').removeClass("sticky");
	$('#catalogos').removeClass("sticky");
	if ($('#direcciones').hasClass('sticky')){
		$('#direcciones').removeClass("sticky");
	}else{
		$('#direcciones').addClass("sticky");
	}*/
});

$(".col-telefonos a").click(function(){
	$('#direcciones').removeClass("sticky");
	$('#catalogos').removeClass("sticky");
	if ($('#telefonos').hasClass('sticky')){
		$('#telefonos').removeClass("sticky");
	}else{
		$('#telefonos').addClass("sticky");
	}
});

$(".col-catalogos button").click(function(){
	$('#direcciones').removeClass("sticky");
	$('#telefonos').removeClass("sticky");
	if ($('#catalogos').hasClass('sticky')){
		$('#catalogos').removeClass("sticky");
	}else{
		$('#catalogos').addClass("sticky");
	}
});


$( "a.closeTel" ).click(function() {
	$('#telefonos').removeClass("sticky");
	$('#direcciones').removeClass("sticky");	
	$('#catalogos').removeClass("sticky");	
});

$("#ciudades").change(function(){
	$('#direcciones .col-mapaTiendas .temporal').removeClass("sticky");
	$('#tiendas .ciudad').removeClass("sticky");
	$('.sede').removeClass("activo");
	switch($('select[id=ciudades]').val()) {
	    case "0":
	        $('#tiendas .ciudad.inicial').addClass("sticky");
	        $('#direcciones .col-mapaTiendas .temporal').addClass("sticky");
	        $('#mapasede').attr("src",'');
	        break;
	    case "1":
	        $('#tiendas .ciudad.Cartagena').addClass("sticky");
	        $('#mapasede').attr("src",'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3924.3482711851934!2d-75.49035348458425!3d10.393888892581788!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x7928102698b335d9!2scompulago!5e0!3m2!1ses!2s!4v1480784240307');
	        $('#tiendas .ciudad.Cartagena .sede.edificio').addClass("activo");
	        break;
	    case "2":
	        $('#tiendas .ciudad.Monteria').addClass("sticky");
	        $('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=monteria%20Cl%2032%204-06&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
	        $('#tiendas .ciudad.Monteria .sede.paseo-central').addClass("activo");
	        break;
	    case "3":
	        $('#tiendas .ciudad.SantaMarta').addClass("sticky");
	        $('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=santa%20marta%20compulago&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
	        $('#tiendas .ciudad.SantaMarta .sede.plazuela-23').addClass("activo");
	        break;
	    case "4":
	        $('#tiendas .ciudad.Riohacha').addClass("sticky");
	        $('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=riohacha%20Cl.%2015%20%238-56&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
	        $('#tiendas .ciudad.Riohacha .sede.suchiimma').addClass("activo");
	        break;
	    case "5":
	        $('#tiendas .ciudad.Valledupar').addClass("sticky");
	        $('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=valledupar%20cra%2011%20%2316a-52&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
	        $('#tiendas .ciudad.Valledupar .sede.centroV').addClass("activo");
	        break;
	}	
});

$('#tiendas .ciudad.Cartagena .sede.edificio').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Cartagena .sede').removeClass("activo");
	$('#tiendas .ciudad.Cartagena .sede.edificio').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3924.3482711851934!2d-75.49035348458425!3d10.393888892581788!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x7928102698b335d9!2scompulago!5e0!3m2!1ses!2s!4v1480784240307');
});
$('#tiendas .ciudad.Cartagena .sede.centro-uno').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Cartagena .sede').removeClass("activo");
	$('#tiendas .ciudad.Cartagena .sede.centro-uno').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=cartagena%20centro%20uno%20compulago&key=AIzaSyBki67ZKBd9nodrnm0YCCkSW3mbrptnXnM');
});
$('#tiendas .ciudad.Cartagena .sede.plazuela').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Cartagena .sede').removeClass("activo");
	$('#tiendas .ciudad.Cartagena .sede.plazuela').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3924.3901566281693!2d-75.48123508458431!3d10.390554392584114!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8ef624336dce3bf7%3A0xeedd35ffff3e85ee!2sCOMPULAGO+iN!5e0!3m2!1ses!2s!4v1480784009762');
});
$('#tiendas .ciudad.Cartagena .sede.castellana').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Cartagena .sede').removeClass("activo");
	$('#tiendas .ciudad.Cartagena .sede.castellana').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=centro%20comercial%20paseo%20de%20la%20castellana&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});
$('#tiendas .ciudad.Cartagena .sede.av-venezuela').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Cartagena .sede').removeClass("activo");
	$('#tiendas .ciudad.Cartagena .sede.av-venezuela').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d245.2469405099416!2d-75.54619679280374!3d10.425459382258294!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x4c4c3e36940419f2!2sCalzado+Spring+Step!5e0!3m2!1ses-419!2s!4v1481142731756');
});

$('#tiendas .ciudad.Monteria .sede.paseo-central').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Monteria .sede').removeClass("activo");
	$('#tiendas .ciudad.Monteria .sede.paseo-central').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=monteria%20Cl%2032%204-06&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.Monteria .sede.alamedas').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Monteria .sede').removeClass("activo");
	$('#tiendas .ciudad.Monteria .sede.alamedas').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=monteria%20alamedas%202&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.Monteria .sede.centroM').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Monteria .sede').removeClass("activo");
	$('#tiendas .ciudad.Monteria .sede.centroM').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=monteria%20Cl%2033%20%232-69&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.SantaMarta .sede.plazuela-23').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.SantaMarta .sede').removeClass("activo");
	$('#tiendas .ciudad.SantaMarta .sede.plazuela-23').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=santa%20marta%20compulago&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.SantaMarta .sede.ocean-mall').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.SantaMarta .sede').removeClass("activo");
	$('#tiendas .ciudad.SantaMarta .sede.ocena-mall').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=santa%20marta%20compulago%20ocean%20mall&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.SantaMarta .sede.buenavista').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.SantaMarta .sede').removeClass("activo");
	$('#tiendas .ciudad.SantaMarta .sede.buenavista').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=santa%20marta%20buena%20vista&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.Riohacha .sede.suchiimma').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Riohacha .sede').removeClass("activo");
	$('#tiendas .ciudad.Riohacha .sede.suchiimma').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=riohacha%20Cl.%2015%20%238-56&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

$('#tiendas .ciudad.Valledupar .sede.mayales').click(function(){
	$('#direcciones').addClass("sticky");
	$('#tiendas .ciudad.Valledupar .sede').removeClass("activo");
	$('#tiendas .ciudad.Valledupar .sede.mayales').addClass("activo");
	$('#mapasede').attr("src",'https://www.google.com/maps/embed/v1/place?q=Centro%20Comercial%20Mayales%20Plaza%20-%20Calle%2037%2C%20Valledupar%20-%20Cesar%2C%20Colombia&key=AIzaSyDhyO_CjqAlhzuLsrZ3DI6lcX0gQxNez9I');
});

function ampliarpdf(archivo){
	var parent = $('embed#espacio-pdf').parent();
	var newPdf;
	var newElement;
	switch(archivo) {
	    case 1:
	    	$(".visualizador .recuadro .titulo").text('Computadores');
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-catalogos").addClass("sticky");
	    	$("embed#espacio-pdf").remove();
	    	newPdf = '<embed src="/resources/firmadecorreo/pdf/pcescritorio.pdf" id="espacio-pdf" height="500px" type="application/pdf" class="col-md-12 nopadding archivo-pdf">';
	    	newElement = $(newPdf);
	    	parent.append(newElement);
	    	break;
	    case 2:
	    	$(".visualizador .recuadro .titulo").text('Portatiles');
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-catalogos").addClass("sticky");
	    	$("embed#espacio-pdf").remove();
	    	newPdf = '<embed src="/resources/firmadecorreo/pdf/portatiles.pdf" id="espacio-pdf" height="500px" type="application/pdf" class="col-md-12 nopadding archivo-pdf">';
	    	newElement = $(newPdf);
	    	parent.append(newElement);
	    	break;	    
	    case 3:
	    	$(".visualizador .recuadro .titulo").text('Tablets');
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-catalogos").addClass("sticky");
	    	$("embed#espacio-pdf").remove();
	    	newPdf = '<embed src="/resources/firmadecorreo/pdf/tablets.pdf" id="espacio-pdf" height="500px" type="application/pdf" class="col-md-12 nopadding archivo-pdf">';
	    	newElement = $(newPdf);
	    	parent.append(newElement);
	    	break;	    
	    case 4:
	    	$(".visualizador .recuadro .titulo").text('Impresoras');
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-catalogos").addClass("sticky");
	    	$("embed#espacio-pdf").remove();
	    	newPdf = '<embed src="/resources/firmadecorreo/pdf/impresoras.pdf" id="espacio-pdf" height="500px" type="application/pdf" class="col-md-12 nopadding archivo-pdf">';
	    	newElement = $(newPdf);
	    	parent.append(newElement);
	    	break;	    
	    case 5:
	    	$(".visualizador .recuadro .titulo").text('Partes');
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-catalogos").addClass("sticky");
	    	$("embed#espacio-pdf").remove();
	    	newPdf = '<embed src="/resources/firmadecorreo/pdf/partes.pdf" id="espacio-pdf" height="500px" type="application/pdf" class="col-md-12 nopadding archivo-pdf">';
	    	newElement = $(newPdf);
	    	parent.append(newElement);
	    	break;	    

	}
}
$('#middle-header .toggled').click(function(){
	if($('#middle-header .toggled').hasClass('sticky')){
		$('#middle-header .toggled').removeClass('sticky');
		$('#middle-header .toggled i').addClass('fa-bars').removeClass('fa-times');
		$('#middle-header .menu-movil').removeClass('sticky');
	}else{
		$('#middle-header .toggled').addClass('sticky');
		$('#middle-header .toggled i').removeClass('fa-bars').addClass('fa-times');
		$('#middle-header .menu-movil').addClass('sticky');
	}
});

$('#box-catalogos a i').click(function(){
	$("#fondo-full").removeClass("sticky");
	$("#box-catalogos").removeClass("sticky");
	$("#espacio-pdf").attr("src",'');
});

$('#box-videos a i').click(function(){
	$("#fondo-full").removeClass("sticky");
	$("#box-videos").removeClass("sticky");
	$("#espacio-video").attr("src",'');
});

$('#main-content .content-area .left-space2 .submenu div.titulo > h4').click(function(){
	if ($(this).hasClass("menu")){
		$("#main-content .content-area .left-space2 .submenu.filtros").removeClass("sticky");
		if ($("#main-content .content-area .left-space2 .submenu.menu").hasClass("sticky")){
			$("#main-content .content-area .left-space2 .submenu.menu").removeClass("sticky");
		}else{
			$("#main-content .content-area .left-space2 .submenu.menu").addClass("sticky");
		}
	}
	if ($(this).hasClass("filtros")){
		$("#main-content .content-area .left-space2 .submenu.menu").removeClass("sticky");			
		if ($("#main-content .content-area .left-space2 .submenu.filtros").hasClass("sticky")){
			$("#main-content .content-area .left-space2 .submenu.filtros").removeClass("sticky");
		}else{
			$("#main-content .content-area .left-space2 .submenu.filtros").addClass("sticky");
		}
	}
});

$('#main-content .content-area .left-space2 .submenu div.titulo > span').click(function(){
	if ($(this).hasClass("menu")){
		$("#main-content .content-area .left-space2 .submenu.filtros").removeClass("sticky");		
		if ($("#main-content .content-area .left-space2 .submenu.menu").hasClass("sticky")){
			$("#main-content .content-area .left-space2 .submenu.menu").removeClass("sticky");
		}else{
			$("#main-content .content-area .left-space2 .submenu.menu").addClass("sticky");
		}
	}
	if ($(this).hasClass("filtros")){
		$("#main-content .content-area .left-space2 .submenu.menu").removeClass("sticky");				
		if ($("#main-content .content-area .left-space2 .submenu.filtros").hasClass("sticky")){
			$("#main-content .content-area .left-space2 .submenu.filtros").removeClass("sticky");
		}else{
			$("#main-content .content-area .left-space2 .submenu.filtros").addClass("sticky");
		}
	}
});

function pasoCompra(paso){
	$("#espacio-video").attr("src",'');
	switch(paso) {
	    case 1:
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-videos").addClass("sticky");
	    	$("#espacio-video").attr("src",'https://www.youtube.com/embed/NsZZDq14AzA?autoplay=1');
	    	break;
	    case 2:
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-videos").addClass("sticky");
	    	$("#espacio-video").attr("src",'https://www.youtube.com/embed/oao8MpoD4KQ?autoplay=1');
	    	break;
	    case 3:
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-videos").addClass("sticky");
	    	$("#espacio-video").attr("src",'https://www.youtube.com/embed/hkdx9V6B-nE?autoplay=1');
	    	break;
	    case 4:
	    	$("#fondo-full").addClass("sticky");
	    	$("#box-videos").addClass("sticky");
	    	$("#espacio-video").attr("src",'https://www.youtube.com/embed/Wesmi8Cmh-c?autoplay=1');
	    	break;
	}
}

$(".videos .video.show a").click(function(){
	var lineaVideo = $('#videoURLpromocional').val() + "?autoplay=1";
	$("#fondo-full").addClass("sticky");
	$("#box-videos").addClass("sticky");
	$("#espacio-video").attr("src",lineaVideo);
});

$(".videos .video.unboxing a").click(function(){
	var lineaVideo = $('#videoURLunboxing').val() + "?autoplay=1";
	$("#fondo-full").addClass("sticky");
	$("#box-videos").addClass("sticky");
	$("#espacio-video").attr("src",lineaVideo);
});

function verDisponibilidad(){
	if ($("#fondo-full").hasClass("sticky")){
		$("#fondo-full").removeClass("sticky");
		$("#disponibilidad").removeClass("sticky");
	}else{
		$("#fondo-full").addClass("sticky");
		$("#disponibilidad").addClass("sticky");
	}
}

function mostrarDireccion(ciudad){
	$(ciudad).removeClass("active");
	if(ciudad != ""){
		$('#disponibilidad .sedes-disponible div.sede').removeClass("active");
		$(ciudad).addClass("active");
	}else{
		$('#disponibilidad .sedes-disponible div.sede').removeClass("active");
	}
}

$("#header-scroll .all-trigger").hover(function(){
    $('#header-scroll .header-box').addClass('menu-opened');
    $('#fondo').addClass('sticky');
},function(){
	$('#header-scroll .header-box').removeClass('menu-opened');
	$('#fondo').removeClass('sticky');
	$("#header-scroll ul.cat-nav").hover(function(){
		$('#header-scroll .header-box').addClass('menu-opened');
		$('#fondo').addClass('sticky');
	},function(){
		$('#header-scroll .header-box').removeClass('menu-opened');
		$('#fondo').removeClass('sticky');
	});
});

$("#mainmenu-header .all-trigger").hover(function(){
	$('#mainmenu-header .header-box').addClass('menu-opened');
    $('#fondo').addClass('sticky');
},function(){
	$('#mainmenu-header .header-box').removeClass('menu-opened');
	$('#fondo').removeClass('sticky');
	$("#mainmenu-header ul.cat-nav").hover(function(){
		$('#mainmenu-header .header-box').addClass('menu-opened');
		$('#fondo').addClass('sticky');
	},function(){
		$('#mainmenu-header .header-box').removeClass('menu-opened');
		$('#fondo').removeClass('sticky');
	});
});



function gridchange() {
	$('li#list').removeClass("selected");
	$('li#grid').addClass("selected");
	$('.box-item').removeClass("col-sm-12").removeClass("col-md-12").removeClass("col-lg-12").removeClass("lista");
	$('.box-item').addClass("col-sm-6").addClass("col-md-3").addClass("col-lg-3");
	$('.image-product').removeClass("col-xs-4").removeClass("col-sm-4").removeClass("col-md-4").removeClass("col-lg-4").removeClass("pull-left");
	$('.image-product').addClass("col-xs-12").addClass("col-sm-12").addClass("col-md-12").addClass("col-lg-12");
	$('.brand-product').removeClass("col-xs-8").removeClass("col-sm-8").removeClass("col-md-8").removeClass("col-lg-8").removeClass("pull-right");
	$('.brand-product').addClass("col-xs-12").addClass("col-sm-12").addClass("col-md-12").addClass("col-lg-12");
	$('.name-product').removeClass("col-xs-8").removeClass("col-sm-8").removeClass("col-md-8").removeClass("col-lg-8").removeClass("pull-right");
	$('.name-product').addClass("col-xs-12").addClass("col-sm-12").addClass("col-md-12").addClass("col-lg-12");
}

function listchange() {
	$('li#grid').removeClass("selected");
	$('li#list').addClass("selected");
	$('.box-item').removeClass("col-sm-6").removeClass("col-md-3").removeClass("col-lg-3");
	$('.box-item').addClass("col-sm-12").addClass("col-md-12").addClass("col-lg-12").addClass("lista");
	$('.image-product').removeClass("col-xs-12").removeClass("col-sm-12").removeClass("col-md-12").removeClass("col-lg-12");
	$('.image-product').addClass("col-xs-4").addClass("col-sm-4").addClass("col-md-4").addClass("col-lg-4").addClass("pull-left");
	$('.brand-product').removeClass("col-xs-12").removeClass("col-sm-12").removeClass("col-md-12").removeClass("col-lg-12");
	$('.brand-product').addClass("col-xs-8").addClass("col-sm-8").addClass("col-md-8").addClass("col-lg-8").addClass("pull-right");
	$('.name-product').removeClass("col-xs-12").removeClass("col-sm-12").removeClass("col-md-12").removeClass("col-lg-12");
	$('.name-product').addClass("col-xs-8").addClass("col-sm-8").addClass("col-md-8").addClass("col-lg-8").addClass("pull-right");
}

$("#newuser").click(function(){
	$('#fondo-full').addClass("sticky");
	$('#formnewuser').addClass("sticky");
});

$("#formnewuser .cerrar a").click(function(){
	$('#fondo-full').removeClass("sticky");
	$('#formnewuser').removeClass("sticky");
});

// formulario Suscripcion Mail List
function CheckField771891(fldName, frm){ if ( frm[fldName].length ) { for ( var i = 0, l = frm[fldName].length; i < l; i++ ) {  if ( frm[fldName].type =='select-one' ) { if( frm[fldName][i].selected && i==0 && frm[fldName][i].value == '' ) { return false; }  if ( frm[fldName][i].selected ) { return true; } }  else { if ( frm[fldName][i].checked ) { return true; } }; } return false; } else { if ( frm[fldName].type == "checkbox" ) { return ( frm[fldName].checked ); } else if ( frm[fldName].type == "radio" ) { return ( frm[fldName].checked ); } else { frm[fldName].focus(); return (frm[fldName].value.length > 0); }} }
function rmspaces(x) {var leftx = 0;var rightx = x.length -1;while ( x.charAt(leftx) == ' ') { leftx++; }while ( x.charAt(rightx) == ' ') { --rightx; }var q = x.substr(leftx,rightx-leftx + 1);if ( (leftx == x.length) && (rightx == -1) ) { q =''; } return(q); }
function checkfield(data) {if (rmspaces(data) == ""){return false;}else {return true;}}
function isemail(data) {var flag = false;if (  data.indexOf("@",0)  == -1 || data.indexOf("\\",0)  != -1 ||data.indexOf("/",0)  != -1 ||!checkfield(data) ||  data.indexOf(".",0)  == -1  ||  data.indexOf("@")  == 0 ||data.lastIndexOf(".") < data.lastIndexOf("@") ||data.lastIndexOf(".") == (data.length - 1)   ||data.lastIndexOf("@") !=   data.indexOf("@") ||data.indexOf(",",0)  != -1 ||data.indexOf(":",0)  != -1 ||data.indexOf(";",0)  != -1  ) {return flag;} else {var temp = rmspaces(data);if (temp.indexOf(' ',0) != -1) { flag = true; }var d3 = temp.lastIndexOf('.') + 4;var d4 = temp.substring(0,d3);var e2 = temp.length  -  temp.lastIndexOf('.')  - 1;var i1 = temp.indexOf('@');if (  (temp.charAt(i1+1) == '.') || ( e2 < 1 ) ) { flag = true; }return !flag;}}
function _checkSubmit771891(frm){
if ( !CheckField771891("fldfirstname", frm) ) { 
   alert("Por favor introduzca el Nombre");
   return false;
}
if ( !isemail(frm["fldEmail"].value) ) { 
   alert("Por favor introduzca el Email");
   return false;
}
 return true; }
 // Fin formulario Suscripcion Mail List

 //wishlist
 function producto (referencia)
 {
    this.referencia = referencia;
 }

 function leerfavoritos(){
 	if(localStorage.favoritos){
 		var prodObjecto = JSON.parse(localStorage.favoritos);
 		var cantidad = prodObjecto.length;
	    $('#numFavoritos').text(cantidad);
	    $('#numFavoritos-flotante').text(cantidad);
 	}
 }

 function guardarfavorito(sku){
    var codRecordar = sku;
    var productObject = new producto(codRecordar.valueOf());
    if(!localStorage.favoritos){
        localStorage.favoritos = "[" + JSON.stringify( productObject ) + "]";
        console.log("Añadido correctamente");
        mensaje("Operacion Exitosa","Se ha guardado correctamente el articulo en tu lista de favoritos.",1);
    }else{
        var prodObjecto = JSON.parse(localStorage.favoritos);
        if (!existeRecordatorio(prodObjecto,codRecordar)){
            prodObjecto.push(productObject);
            localStorage.favoritos = JSON.stringify(prodObjecto);
            console.log("Añadido:" + JSON.stringify(prodObjecto));
            mensaje("Operacion Exitosa","Se ha guardado correctamente el articulo en tu lista de favoritos.",1);
        }else{
        	mensaje("Error","El articulo que desea adicionar a favoritos ya existe.",2);
        }
    }
    leerfavoritos();
 }

 function existeRecordatorio(productosFavoritos,codRecordado){
 	var cantidad = productosFavoritos.length;
    var existe = false;
    for (i = 0;i<=cantidad-1;i++){
        if (productosFavoritos[i].referencia == codRecordado){
            existe = true;
            break;
        }
    }
    return existe;
 }

 
 //Fin wishlist

 //mensaje
 function mensaje(titulo,contenido,tipo){
 	$('#nota2').addClass("sticky");
 	$('#nota2').removeClass("ok").removeClass("fault");
 	if(tipo == 1){
		$('#nota2').addClass("ok");
 	}else{
		$('#nota2').addClass("fault");
 	}
 	$('#nota2 .texto .titulo').text(titulo);
 	$('#nota2 .texto .contenido').text(contenido);
 	setTimeout(ocultarNota2,5000);
 }
 function ocultarNota(){
 	$('#nota').removeClass("sticky");
 }

function ocultarNota2(){
 	$('#nota2').removeClass("sticky");
}
 //fin mensaje

 
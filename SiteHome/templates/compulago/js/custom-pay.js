$(document).ready(function(){
	$('#btn-guardar-seguir').attr("disabled", true);
	$('#submitBtn').attr("disabled", true);
});

$(".direccion a").click(function(){

	$(".direccion a").removeClass("activo");
	$('#shippingAddress').val("");
	$('#btn-guardar-seguir').attr("disabled", true);

	if ($(this).hasClass('activo')){
		$(this).removeClass("activo");
	}else{
		$(this).addClass("activo");
		$('#shippingAddress').val($(this + '.activo input#direccion').val());
	}

	if ($(".envioEnlinea a").hasClass('activo')){
		if ($(".direccion2 a").hasClass('activo')){
			$(".terminos-sin").addClass("off");
			$(".terminos").addClass("activo");
		}
	}

	if ($(".envioContraentrega a").hasClass('activo')){
		if ($(".direccion2 a").hasClass('activo')){
			$(".terminos-sin").addClass("off");
			$(".terminos").addClass("activo");
		}
	}
});

$(".direccion2 a").click(function(){

	$(".direccion2 a").removeClass("activo");
	$('#billingAddress').val("");
	$('#btn-guardar-seguir').attr("disabled", true);
	$("#aceptoTerminos").prop("checked", "")

	if ($(this).hasClass('activo')){
		$(this).removeClass("activo");
	}else{
		$(this).addClass("activo");
		$('#billingAddress').val($(this + '.activo input#direccion2').val());
	}

	if ($(".envioEnlinea a").hasClass('activo')){
		if ($(".direccion a").hasClass('activo')){
			$(".terminos-sin").addClass("off");
			$(".terminos").addClass("activo");
		}
	}

	if ($(".envioContraentrega a").hasClass('activo')){
		if ($(".direccion a").hasClass('activo')){
			$(".terminos-sin").addClass("off");
			$(".terminos").addClass("activo");
		}
	}

	if ($(".recogerEntienda a").hasClass('activo')){
		$(".terminos-sin").addClass("off");
		$(".terminos").addClass("activo");
	}

});

$(".envioEnlinea a").click(function(){
	$('#deliveryType').val($('#collapseInfo .envioEnlinea a input.tipoEntrega').val());
	$("#aceptoTerminos").prop("checked", "")
	$('#btn-guardar-seguir').attr("disabled", true);
	$(".envioContraentrega a").removeClass("activo");
	$(".recogerEntienda a").removeClass("activo");
	$(".direccion-sin").removeClass("off");
	$(".direccion2-sin").removeClass("off");
	$(".direccionAdicional-sin").removeClass("off");
	$(".terminos-sin").removeClass("off");
	$(".direccionesAdicional").removeClass("disponible");
	$(".direccionesDisponibles").removeClass("disponible");
	$(".direccionesDisponibles2").removeClass("disponible");
	$(".direccion a").removeClass("activo");
	$(".direccion2 a").removeClass("activo");
	$(".direccion-recoger").removeClass("activo");
	$(".direccionAdicional-recoger").removeClass("activo");
	$(".terminos").removeClass("activo");	
	if ($(".envioEnlinea a").hasClass('activo')){
		$(".envioEnlinea a").removeClass("activo");
	}else{
		$(".envioEnlinea a").addClass("activo");
		$(".direccion-sin").addClass("off");
		$(".direccion2-sin").addClass("off");
		$(".direccionAdicional-sin").addClass("off");
		$(".direccionesAdicional").addClass("disponible");
		$(".direccionesDisponibles").addClass("disponible");
		$(".direccionesDisponibles2").addClass("disponible");
	}
});

$(".envioContraentrega a").click(function(){
	$('#deliveryType').val($('#collapseInfo .envioContraentrega a input.tipoEntrega').val());
	$("#aceptoTerminos").prop("checked", "")
	$('#btn-guardar-seguir').attr("disabled", true);
	$(".envioEnlinea a").removeClass("activo");
	$(".recogerEntienda a").removeClass("activo");
	$(".direccion-sin").removeClass("off");
	$(".direccion2-sin").removeClass("off");
	$(".direccionAdicional-sin").removeClass("off");
	$(".terminos-sin").removeClass("off");
	$(".direccionesAdicional").removeClass("disponible");
	$(".direccionesDisponibles").removeClass("disponible");
	$(".direccionesDisponibles2").removeClass("disponible");
	$(".direccion a").removeClass("activo");
	$(".direccion2 a").removeClass("activo");
	$(".direccion-recoger").removeClass("activo");
	$(".direccionAdicional-recoger").removeClass("activo");
	$(".terminos").removeClass("activo");	
	if ($(".envioContraentrega a").hasClass('activo')){
		$(".envioContraentrega a").removeClass("activo");
	}else{
		$(".envioContraentrega a").addClass("activo");
		$(".direccion-sin").addClass("off");
		$(".direccion2-sin").addClass("off");
		$(".direccionAdicional-sin").addClass("off");
		$(".direccionesAdicional").addClass("disponible");
		$(".direccionesDisponibles").addClass("disponible");
		$(".direccionesDisponibles2").addClass("disponible");
	}
});

$(".recogerEntienda a").click(function(){
	$('#deliveryType').val($('#collapseInfo .recogerEntienda a input.tipoEntrega').val());
	$("#aceptoTerminos").prop("checked", "")
	$('#btn-guardar-seguir').attr("disabled", true);
	$(".envioEnlinea a").removeClass("activo");
	$(".envioContraentrega a").removeClass("activo");
	$(".recogerEntienda a").removeClass("activo");
	$(".direccion-sin").removeClass("off");
	$(".direccion2-sin").removeClass("off");
	$(".direccionAdicional-sin").removeClass("off");
	$(".terminos-sin").removeClass("off");
	$(".direccionesAdicional").removeClass("disponible");
	$(".direccionesDisponibles").removeClass("disponible");
	$(".direccionesDisponibles2").removeClass("disponible");
	$(".direccion a").removeClass("activo");
	$(".direccion2 a").removeClass("activo");
	$(".direccion-recoger").removeClass("activo");
	$(".direccionAdicional-recoger").removeClass("activo");
	$(".terminos").removeClass("activo");		
	if ($(".recogerEntienda a").hasClass('activo')){
		$(".recogerEntienda a").removeClass("activo");
	}else{
		$(".recogerEntienda a").addClass("activo");
		$(".direccionesDisponibles2").addClass("disponible");
		$(".direccion-sin").addClass("off");
		$(".direccion2-sin").addClass("off");
		$(".direccion-recoger").addClass("activo");
		$(".direccionAdicional-sin").addClass("off");
		$(".direccionAdicional-recoger").addClass("activo");	
	}
});

$('#aceptoTerminos').click(function() {
    if ($(this).is(':checked')) {
        $('#btn-guardar-seguir').attr("disabled", false);
    }else{
    	$('#btn-guardar-seguir').attr("disabled", true);
    }
    $('#userComments').val($('textarea#datoAdicional').val());
});

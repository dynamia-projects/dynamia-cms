$(document).ready(function() {

	console.log("holaaaaa");
	$('div.bannerslider').flexslider({
		animation : "slide",
		start : function(slider) {
			$('body').removeClass('loading');
		}
	});

});
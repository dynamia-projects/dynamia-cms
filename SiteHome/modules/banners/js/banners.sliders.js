$(document).ready(function() {

	$('.bannerslider').flexslider({
		animation : "fade",
		start : function(slider) {
			$('body').removeClass('loading');
		}
	});
});
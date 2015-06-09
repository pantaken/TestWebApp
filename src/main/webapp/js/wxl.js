function pop(event,targetId) {
	var input = $(event.target);
	console.log(document.documentElement.clientHeight);
	$('#'+targetId).css({"display":"","top":input.offset().top + input.outerHeight(false) - document.body.scrollTop,"left":input.offset().left});
}
$(document).ready(function(){
	$(".wxl-pop-close").click(function(){
		var targetId = $(this).attr('aria-label');
		$("#" + targetId).hide();
	});
});
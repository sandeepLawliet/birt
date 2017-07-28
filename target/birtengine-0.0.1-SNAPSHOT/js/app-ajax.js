//$(document).ready(function() {
//        $('#userName').blur(function(event) {
//                var name = $('#userName').val();
//                $.get('GetUserServlet', {
//                        userName : name
//                }, function(responseText) {
//                        $('#ajaxGetUserServletResponse').text(responseText);
//                });
//        });
//});
//

$(document).ready(function() {
	$('#ReportName').blur(function() {
		$.ajax({
			url : 'GetReportServlet',
			data : {
				ReportName : $('#ReportName').val()
			},
			success : function(responseText) {
				$('#ajaxGetUserServletResponse').html(responseText);
			}
		});
	});
});
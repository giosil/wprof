<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map, org.dew.wprof.util.WebUtil"%>
<%
	Map<String, Object> mapData = WebUtil.getMapAttribute(request, "data", true);
	String message = WebUtil.getStringAttribute(request, "msg", null);
%>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>WProf</title>
  
  <link href="css/bootstrap.min.css" rel="stylesheet">
  
  <link href="css/main.css" rel="stylesheet">

  <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

  <link href="css/plugins/jquery-ui/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
  <link href="css/plugins/datapicker/datepicker3.css" rel="stylesheet">
  <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
  <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
  
  <!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
   <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body>
  
  <%@ include file="_menu.jsp" %>
  
  <div class="container">
    <div class="starter-template" id="view-root">
      <div id="view-root"></div>
    </div>
  </div>
  
  <script src="data.js?<%= System.currentTimeMillis() %>"></script>
  
  <script src="js/jquery-3.4.1.js"></script>
  <script src="js/bootstrap.min.js"></script>
  
  <script src="js/plugins/wow/wow.min.js" type="text/javascript"></script>
  <script src="js/plugins/sweetalert/sweetalert.min.js" type="text/javascript"></script>
  <script src="js/plugins/toastr/toastr.min.js" type="text/javascript"></script>
  <script src="js/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
  <script src="js/plugins/datapicker/bootstrap-datepicker.js" type="text/javascript"></script>
  <script src="js/plugins/chartJs/Chart.min.js" type="text/javascript"></script>
  
  <script src="js/i18n/datapicker/bootstrap-datepicker.it.js" type="text/javascript"></script>
  
  <script src="js/main.js"></script>

  <script src="out/wux/wux.base.js"></script>
  <script src="out/wux/wux.cfg.js"></script>
  <script src="out/wux/wux.comp.js"></script>
  <script src="out/wux/wux.charts.js"></script>

  <script src="out/wprof/wprof_base.js"></script>
  <script src="out/wprof/wprof_gui.js"></script>

  <script type="text/javascript">
    $(document).ready(function() {
      
      WuxDOM.render(new WP.GUIAnalyze(), 'view-root');
      
    });
  </script>
</body>
</html>
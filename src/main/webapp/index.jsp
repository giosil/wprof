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

  <link href="css/plugins/jquery-ui/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
  <link href="css/plugins/datapicker/datepicker3.css" rel="stylesheet">
  
  <!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
   <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body>
  
  <%@ include file="_menu.jsp" %>
  
  <div class="container">
    <div class="starter-template" id="view-root">
      
      <h1>WProf</h1>
      
      <div class="table-responsive">
        <table class="table table-bordered table-striped table-hover" id="tabResult">
          <thead>
            <tr>
              <th scope="col" class="text-center">Key</th>
              <th scope="col" class="text-center">Value</th>
            </tr>
          </thead>
          <tbody>
            <% WebUtil.printTableRows(out, mapData); %>
          </tbody>
        </table>
      </div>
      
      <div class="row">
        <div class="col-md-12">
          <% WebUtil.printSpan(out, message, "msg", "", "color:#aa0000;font-weight:bold;"); %>
        </div>
      </div>
      
    </div>
  </div>
  
  <script src="js/jquery-3.4.1.js"></script>
  <script src="js/bootstrap.min.js"></script>
  
  <script src="js/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
  <script src="js/plugins/datapicker/bootstrap-datepicker.js" type="text/javascript"></script>
  
  <script src="js/i18n/datapicker/bootstrap-datepicker.it.js" type="text/javascript"></script>
</body>
</html>
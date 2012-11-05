<html>
<head>
	 <script src="scripts/jquery-1.7.1.min.js"></script>
</head>
<body>
<script type="text/javascript"> 
    $(this).ready(function () {
    	 var href = window.location.hash.substring(1); 
    	 window.location = "/social.fb.crm/Facebook/?"+href; 
    }); 
</script> 
</body>
</html>

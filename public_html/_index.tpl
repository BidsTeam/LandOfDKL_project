<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">

    <title>Land of DKL</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=yes, maximum-scale=1, minimum-scale=1">

	<!--<link rel="stylesheet" type="text/css" href="css/lib/bootstrap.css">-->
	<link rel="stylesheet" type="text/css" media="(min-device-width: 800px)" href="css/main.css">
    <link rel="stylesheet" type="text/css" media="(max-device-width: 780px)" href="css/mobile.css">
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.structure.css">

    <%= devScriptInit %>

</head>

<body>

<div id="bgndVideo" class="bgndVideo" data-property='{videoURL:"0W0Wc1E8ffk",containment:"#bgndVideo",autoPlay:true, mute:true, startAt:8,opacity:1, loop:true, stopAt:30, addRaster:false, showControls:false, stopMovieOnBlur: false}'>
    <div class="background-video-overlayer"></div>
</div>

<div id="app-container" class="app">
    <div id="main-container" class="main-container">

    </div>
</div>

<div class="loader-screen" id="loader-screen">
    <div class="loader-screen__indicator-container">
        <div class="preloader">
            <div class="preloader-box">
                <div>W</div>
                <div>A</div>
                <div>I</div>
                <div>T</div>
                <div>I</div>
                <div>N</div>
                <div>G</div>
            </div>
        </div>
        <div class="abort-button-container">
            <button class="btn cancel-button">Отмена</button>
        </div>
    </div>
</div>

<%= productionScriptInit %>

</body>
</html>
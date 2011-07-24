<%@page import="java.net.URL"%>
<!doctype html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <title>qipki:web</title>
        <style type="text/css">
            body {
                overflow: hidden;
            }
            #loading_top {
                position: absolute; z-index: 1;
                top:0; left: 0;
                width: 100%;
                height: 50%;
                background-color: #ccd;
                border-bottom: 1px solid #007;
            }  
            #loading_top div {
                position: absolute;
                left: 50%;
                bottom: 0px;
                width: 128px;
                height: 42px;
                margin-left: -64px;
                font-size: 14px;
                font-weight: bold;
                line-height: 42px;
                text-align: center;
                color: #007;
            }
            #loading_bottom {
                position: absolute; z-index: 1;
                bottom: 0; left: 0;
                width: 100%;
                height: 50%;
                background-color: #ccd;
                border-top: 1px solid #007;
            }
        </style>
        <script type="text/javascript">
            function animate(opts) {
                var start = new Date  
                var id = setInterval(function() {
                    var timePassed = new Date - start
                    var progress = timePassed / opts.duration
                    if (progress > 1) 
                        progress = 1
                    var delta = opts.delta(progress)
                    opts.step(delta)
                    if (progress == 1) {
                        clearInterval(id)
                        if (opts.hide != null)
                            opts.hide()
                            
                    }
                }, opts.delay || 10)
            }
            function loading_show() {
                var body = document.getElementsByTagName( 'body' )[0];
                var top = document.createElement('div');
                var logo = document.createElement('div');
                var bottom = document.createElement('div');
                top.id = 'loading_top'
                bottom.id = 'loading_bottom'
                logo.appendChild( document.createTextNode('qipki:web') );
                top.appendChild(logo);
                body.appendChild(top);
                body.appendChild(bottom);
            }
            function loading_hide() {
                var loading_top = document.getElementById('loading_top');
                if (loading_top != null) {
                    var loading_bottom = document.getElementById('loading_bottom');
                    var delta_func = function( progress ) { 
                        return Math.pow( progress, 4 ) 
                    };
                    var step_func = function( delta ) {
                        loading_top.style.top = "-"+(window.innerHeight/2)*delta + "px";
                        loading_bottom.style.bottom = "-"+(window.innerHeight/2)*delta + "px";
                    };
                    var hide_func = function() { 
                        loading_top.parentNode.removeChild(loading_top); 
                        loading_bottom.parentNode.removeChild(loading_bottom); 
                    };
                    animate({
                        delay: 10,
                        duration: 500,
                        delta: delta_func,
                        step: step_func,
                        hide: hide_func
                    })
                }
            }
        </script>
        <!-- Consider inlining CSS to reduce the number of requested files -->
        <link type="text/css" rel="stylesheet" href="qipkiweb.css">
        <script type="text/javascript" src="qipkiweb/qipkiweb.nocache.js"></script>
        <script type="text/javascript">
            <% URL requestURL = new URL( request.getRequestURL().toString() );%>
                var apis = {
                    'x509-ca': '<%= new URL( requestURL.getProtocol(), requestURL.getHost(), requestURL.getPort(), "/api" ).toString() %>'
                };
        </script>
    </head>
    <body onload="loading_show()">
        <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
        <noscript>
        <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
            Your web browser must have JavaScript enabled
            in order for this application to display correctly.
        </div>
        </noscript>
    </body>
</html>

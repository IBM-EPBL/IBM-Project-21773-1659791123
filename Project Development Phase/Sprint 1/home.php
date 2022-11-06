<?php

session_start();

if (isset($_SESSION['id']) && isset($_SESSION['uname'])) {

?>
    <!DOCTYPE html>

    <head>

        <title>CZAP PORTAL</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <style>
            body,
            h1 {
                font-family: "Raleway", sans-serif
            }

            body,
            html {
                height: 100%
            }

            .bgimg {
                background-image: url('https://wallpaperaccess.com/full/432471.jpg');
                min-height: 100%;
                min-width: 100%;
                background-position: center;
                background-size: cover;
            }
        </style>

    </head>

    <body>


        <div class="bgimg w3-display-container w3-animate-opacity w3-text-white">

            <div class="w3-display-middle">
                <h1 class="w3-jumbo w3-animate-top">CONTAINMENT ZONE ALERTING APPLICATION</h1>
                <hr class="w3-border-grey" style="margin:auto;width:40%">
                <p class="w3-large w3-center"> Hello, <?php echo $_SESSION['uname']; ?></p>
            </div>
        </div>
    </body>

    </html>
<?php

} else {

    header("Location: index.php");

    exit();
}

?>
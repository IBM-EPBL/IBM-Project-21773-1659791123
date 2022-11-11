<!DOCTYPE html>

<head>
    <title>CZAP Admin</title>
    <link href="index.css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/8e99297c05.js" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
</head>
</head>

<body>
    <div class="wrapper">
        <div class="logo">
            <img src="CZAP.png" alt="CZAP Logo" height="50" width="50">
        </div>
        <div class="text-center mt-4 name">
            ADMIN
        </div>
        <form action="login.php" class="p-3 mt-3" method="post">
            <?php if (isset($_GET['error'])) { ?>

                <p class="error"> <?php echo $_GET['error']; ?></p>

            <?php } ?>
            <div class="form-field d-flex align-items-center">
                <span class="far fa-user"></span>
                <input type="text" name="uname"  placeholder="Username">
            </div>
            <div class="form-field d-flex align-items-center">
                <span class="fas fa-key"></span>
                <input type="password" name="password"  placeholder="Password">
            </div>
            <button class="btn mt-3">Login</button>
        </form>

    </div>
</body>
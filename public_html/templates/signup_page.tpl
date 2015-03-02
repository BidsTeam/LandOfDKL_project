<div class="menu-container">
    <div id="signup-page" class="center">
        <div class="menu-name">Signup</div>
        <form id="signup-form" class="form-horizontal" action="/api/auth/signup" method="POST" enctype="application/x-www-form-urlencoded" style="padding-left:130px;">
            <div class="form-group">
                <label for="signup-username-field" class="col-sm-2 control-label"> Username </label>
                <input type="text" name="username" class="form-control" id="signup-username-field">
            </div>
            <div class="form-group">
                <label for="signup-email-field" class="col-sm-2 control-label"> E-mail </label>
                <input type="text" name="email" class="form-control" id="signup-email-field">
            </div>
            <div class="form-group">
                <label for="signup-password-field" class="col-sm-2 control-label"> Пароль </label>
                <input type="password" name="password" class="form-control" id="signup-password-field">
            </div>
            <div class="form-group">
                <label for="signup-password-repeat-field" class="col-sm-2 control-label">Повторите пароль </label>
                <input type="password" name="repeat_password" class="form-control" id="signup-password-repeat-field">
            </div>
            <div class="form-group">
                <input type="submit" value="Войти" class="btn btn-large btn-success" disabled style="margin-right:70px;">
            </div>
        </form>
        <a href="#" class="btn btn-large btn-primary"> Назад </a>
    </div>
</div>
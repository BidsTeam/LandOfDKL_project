<img src="/images/background_1.jpg" id="logo">
<div class="menu-container">
    <div id="signup-page" class="center">
        <div class="menu-name">Signup</div>
        <form id="signup-form" action="/api/auth/signup" method="POST" enctype="application/x-www-form-urlencoded">
            <div class="form-group">
                <label for="signup-username-field"> Username </label> <input type="text" name="username" id="signup-username-field">
            </div>
            <div class="form-group">
                <label for="signup-email-field"> E-mail </label> <input type="text" name="email" id="signup-email-field">
            </div>
            <div class="form-group">
                <label for="signup-password-field"> Пароль </label> <input type="password" name="password" id="signup-password-field">
            </div>
            <div class="form-group">
                <label for="signup-password-repeat-field"> Повторите пароль </label> <input type="password" name="repeat-password" id="signup-password-repeat-field">
            </div>
            <div class="form-group">
                <input type="submit" value="Войти" class="btn btn-large btn-success" disabled>
            </div>
        </form>
        <a href="#" class="btn btn-large btn-primary"> Назад </a>
    </div>
</div>
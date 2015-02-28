<img src="/images/background_1.jpg" id="logo">
<div class="menu-container">
	<div id="auth-page" class="center">
		<div class="menu-name">Login</div>
		<form id="auth-form" action="/api/auth/signin" method="POST" enctype="application/x-www-form-urlencoded">
			<div class="form-group">
				<label> Login <input type="text" name="login" id="auth-login-field"> </label>
				<label> Пароль <input type="password" name="password" id="auth-password-field"> </label>
				<input type="submit" value="Войти" class="btn btn-large btn-success" disabled>
			</div>
		</form>
		<a href="#" class="btn btn-large btn-primary"> Назад </a>
	</div>
</div>
<div class="menu-container">
	<div id="auth-page">
		<div class="menu-name">Login</div>
		<form id="auth-form" action="/api/v1/auth/signin" method="POST">
			<div class="form-group">
				<label> E-mail <input type="text" name="email"> </label>
				<label> Пароль <input type="password" name="password"> </label>
				<input type="submit" value="Войти" class="btn btn-large btn-success">
			</div>
		</form>
		<a href="#" class="btn btn-large btn-primary"> Назад </a>
	</div>
</div>
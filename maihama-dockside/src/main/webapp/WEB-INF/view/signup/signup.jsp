<!DOCTYPE html>
<html lang="ja" xmlns:th="http://www.thymeleaf.org">
<head>
<!--/*/ <th:block th:include="/common/layout.html :: head ('Sign up')"> /*/-->
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="../../../css/reset.css" />
	<link rel="stylesheet" type="text/css" href="../../../css/common.css" />
	<link rel="stylesheet" type="text/css" href="../../../css/individual.css" />
	<title>Preview Title</title>
<!--/*/ </th:block> /*/-->
<!--/* individual Css write to after */-->
</head>
<body>
<header th:replace="/common/layout.html :: header">
	<section class="nav-main cf">
		<div class="wrap">
			<h1 class="main-title"><a href="#">Maihama<span> (LastaFlute Example)</span></a></h1>
			<ul class="nav-home">
				<li><a href="../product/product_list.html" ><span class="link-block">Products</span></a></li>
				<li><a href="../member/member_list.html" ><span class="link-block">Members</span></a></li>
				<li><a href="../withdrawal/withdrawal.html"><span class="link-block">Withdrawal</span></a></li>
			</ul>
			<ul class="nav-user">
				<li>
					<p class="nameHeader">Welcome, Mr.Guest</p>
					<ul class="child">
						<li><a href="#">Profile</a></li>
						<li><a href="#">Sign out</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</section>
</header>
<main>
<div class="">
	<h2 class="content-title"><la:caption key="labels.signup.title"/></h2>
	<la:errors/>
	<section class="sign-in-box">
		<form styleClass="signup-form" action="#" th:action="@{/signup/}" method="post">
			<ul>
				<li>
					<span th:value="${labels.signup.input.name}">Member Name</span>
					<input type="text" name="name" th:value="${name}"/>
					<span th:style="'color : red;'" th:each="er : ${errors.part('name')}" th:text="${er.message}"/>
				</li>
				<li>
					<span th:value="${labels.signup.input.account}">Member Account</span>
					<input type="text" name="account" th:value="${account}"/>
					<span th:style="'color : red;'" th:each="er : ${errors.part('account')}" th:text="${er.message}"/>
				</li>
				<li>
					<span th:value="${labels.signup.input.password}">Password</span>
					<input type="password" name="password" th:value="${password}"/>
					<span th:style="'color : red;'" th:each="er : ${errors.part('password')}" th:text="${er.message}"/>
				</li>
				<li>
					<span th:value="${labels.signup.input.reminder.question}">Reminder Question</span>
					<input type="password" name="reminderQuestion" th:value="${reminderQuestion}"/>
					<span th:style="'color : red;'" th:each="er : ${errors.part('reminderQuestion')}" th:text="${er.message}"/>
				</li>
				<li>
					<span th:value="${labels.signup.input.reminder.answer}">Reminder Answer</span>
					<input type="password" name="reminderAnswer" th:value="${reminderAnswer}"/>
					<span th:style="'color : red;'" th:each="er : ${errors.part('reminderAnswer')}" th:text="${er.message}"/>
				</li>
				<li>
					<input type="submit" th:value="#{labels.signup.button}" value="signup"/>
				</li>
			</ul>
		</form>
	</section>
</div>
</main>
<footer th:replace="/common/layout.html :: footer">
	<div class="wrap">
		<ul class="footer-link">
			<li><a href="http://dbflute.seasar.org/">DBFlute Top</a></li>
			<li><a href="http://dbflute.seasar.org/lastaflute">LastaFlute Top</a></li>
		</ul>
		<p class="copyright">&copy; LastaFlute project</p>
	</div>
</footer>
<!--/*/ <th:block th:include="/common/layout.html :: afterScript"> /*/-->
<!-- script contents -->
<script src="../../../js/jquery-2.1.3.min.js" ></script>
<script src="../../../js/common.js" ></script>
<!--/*/ </th:block> /*/-->
<!--/* individual Script write to after */-->
</body>
</html>
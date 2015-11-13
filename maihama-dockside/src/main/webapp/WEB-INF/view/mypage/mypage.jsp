<c:import url="${viewPrefix}/common/default_layout.jsp">
<c:param name="contents">
<!-- <main> start main content -->
<div class="contents">
	<h2 class="content-title"><la:caption key="labels.mypage.title"/></h2>
	<section class="recent-purchase-box">
		<h3>YOUR RECENT PURCHASE</h3>
	</section>
	<section class="follow-box">
		<h3>FOLLOW</h3>
		<ul>
			<c:forEach var="bean" items="${beans}">
				<li>${f:h(bean.productName)}, ${f:h(bean.regularPrice)}</li>
			</c:forEach>
		</ul>
	</section>
</div>
<!-- </main> end of main content -->
</c:param>
</c:import>
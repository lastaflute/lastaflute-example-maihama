<c:import url="${viewPrefix}/common/default_layout.jsp">
<c:param name="contents">
<!-- <main> start main content -->
<div class="contents">
	<h2 class="pg-content-title"><la:caption key="labels.withdrawal.title"/></h2>
	<section class="select-withdrawal-reason">
		<h3 class="pg-content-title-second">Select Withdrawal Reason</h3>
		<la:form styleClass="withdrawal-form">
			<ul class="withdrawal-reason-list">
				<li class="withdrawal-reason-element">
					<la:select property="reasonCode">
						<la:option value="" key="labels.listbox.caption.tell"/>
						<la:optionsCls name="WithdrawalReason"/>
					</la:select>
				</li>
				<li class="withdrawal-reason-element">
					<div>Withdrawal Reason</div>
					<la:textarea class="withdrawal-write-reason" property="reasonInput"/> <la:errors property="reasonInput"/>
				</li>
			</ul>
			<la:submit property="confirm" value="labels.confirm"/>
		</la:form>
	</section>
</div>
<!-- </main> end of main content -->
</c:param>
</c:import>


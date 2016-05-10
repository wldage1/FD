<ul id="tabnav">
	<li class="tab_1" ><a href="${base}${market_salemanage_manage_overview.controller}?c=${market_salemanage_manage_overview.code}&id=${product.id}" id="tab_${market_salemanage_manage_overview.code}"><msg:message code="product.sale.tab.distribute.overview"/></a></li>
	<c:if test="${product.sellStatus != 5 && product.sellStatus != 6 && product.sellStatus != 7 &&  product.sellStatus != 4 &&  product.sellStatus != 8}">
	<li class="tab_2" ><a href="${base}${market_salemanage_manage_neworder.controller}?c=${market_salemanage_manage_neworder.code}&id=${product.id}" id="tab_${market_salemanage_manage_neworder.code}" ><msg:message code="product.sale.tab.distribute.list"/></a></li>
	<li class="tab_3" ><a href="${base}${market_salemanage_manage_shared.controller}?c=${market_salemanage_manage_shared.code}&id=${product.id}" id="tab_${market_salemanage_manage_shared.code}" ><msg:message code="product.sale.tab.shared.list"/></a></li>
	<li class="tab_4" ><a href="${base}${market_salemanage_manage_payed.controller}?c=${market_salemanage_manage_payed.code}&id=${product.id}" id="tab_${market_salemanage_manage_payed.code}" ><msg:message code="product.sale.tab.payed.list"/></a></li>
	<li class="tab_4" ><a href="${base}${market_salemanage_manage_limit.controller}?c=${market_salemanage_manage_limit.code}&id=${product.id}" id="tab_${market_salemanage_manage_limit.code}" ><msg:message code="product.sale.tab.miniorders.list"/></a></li>
	</c:if>
</ul>
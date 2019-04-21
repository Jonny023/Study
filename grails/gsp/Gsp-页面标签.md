# 页面标签

```html
// 输出值
<% out << "print me!" %>

<%="print me!" %>

<p><c:out value="${bookmark.title}" /></p>

<p>${bookmark.title}</p>

// 存入变量
<g:set var="bookmarkTitle" value="${bookmark.title}" />

<g:set var="session.user" value="${user}" />

// 判断
<g:if test="${bookmark?.url ==~ 'https://.+'}">
 Secure Site: <a href="${boomark.url}" target="_blank">${bookmark.title}</a>
</g:if>
<g:elseif test="${bookmark?.url ==~ 'ftp://.+'}">
 FTP Link: <a href="${boomark.url}">${bookmark.title}</a>
</g:elseif>
<g:else>
 Web Link: <a href="${boomark?.url}" target="_blank">${bookmark?.title}</a>
</g:else>

// 遍历
<g:each in="${bookmark.tags?}">
 <span class="tag">${it.name}</span>
</g:each>

<ol>
	 <g:collect in="${bookmarks}" expr="${it.title}">
		<li>${it}</li>
	 </g:collect>
</ol>

// 循环
<g:set var="i" expr="${bookmark.tags?.size()}" />
<g:while test="${i > 0}">
	<g:set var="i" expr="${i-1}" />
</g:while>

<% bookmarks.collect{ it.title }.each { %>
	 <li>${it}</li>
	 <%}%>
</ol>

// 根据条件查询
<g:findAll in="${bookmarks}" expr="${it.tags?.name.contains('grails')}">
 <p>
 <a href="${it.url}">${it.title}</a>
 </p>
</g:findAll>

<g:grep in="${bookmarks.tags.name}" filter="${~/groovy|grails|agile/}">
 <p>${it}</p>
</g:grep>
<!-- Finds all bookmark instances that are of type Blog -->
Blogs:
<g:grep in="${bookmarks}" filter="${Blog}">
 <p>
 <a href="${it.url}">${it.title}</a>
 </p>
</g:grep>

// 项目上下文
${application.contextPath}
${request.contextPath}

// 链接
<a href="<g:createLink action="list" />">A dynamic link</a>
<a href="${createLink(action:'list')}">A dynamic link</a>
<g:link>
// 参数：controller/action/id/params
<g:link controller="bookmark" action="list">List Bookmarks</g:link>
<g:link action="show" id="1">Show bookmark with id 1</g:link>
<g:link controller="bookmark" action="list" params="[max:10,order:'title']">Show first ten ordered by Title</g:link>
<g:link action="create" params="${params}">Pass parameters from this action to next</g:link>

// 错误信息
<div class="${hasErrors(bean:bookmark,field:'title','errors')}">
 ...
</div>

<g:hasErrors bean="${bookmark}">
 <ul class="errors">
	 <g:eachError bean="${bookmark}">
	 <li>${it.defaultMessage}</li>
	 </g:eachError>
 </ul>
</g:hasErrors>

// 引入css
<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}"></link>

// 表单
<form action="/bookmarks/bookmark/save" method="POST">
 ...
</form>

<g:form url="[controller:'bookmark', action:'save']">
 ...
</g:form>

<g:form controller="bookmark" action="save">
 <g:textField name="title" value="${bookmark?.title}" />
 ...
</g:form>

// 单选
<g:radio name="myGroup" value="1" checked="${someValue == 1}" /> Radio 1

// 下拉框
<g:select name="rating" from="${1..10}" value="${bookmark.rating}" />
<g:select name="category" from="${['blog', 'article', 'general', 'news']}" value="${bookmark.category}" />

<g:currencySelect name="myCurrency" value="${ Currency.getInstance(request.getLocale()) }" />
<%-- Sets the locale to the locale of the request --%>
<g:localeSelect name="myLocale" value="${ request.getLocale() }" />
<%-- Sets value to default time zone --%>
<g:timeZoneSelect name="myTimeZone" value="${ TimeZone.getDefault() }" />

// 日期标签
<g:datePicker name="myDate" value="${new Date()}" />
<g:datePicker name="myDate" value="${new Date()}" precision="day" />

// 国际化
<g:message code="welcome.message" />

// 分页标签
<g:paginate controller="controller" 
 action="list" 
 total="${Bookmark.countByUser(session.user)}" />
 
```

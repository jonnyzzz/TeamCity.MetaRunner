<%--
~ Copyright 2000-2010 JetBrains s.r.o.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
--%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ page import="jetbrains.buildserver.metarunner.xml.TextType" %>
<%@ page import="jetbrains.buildserver.metarunner.xml.ChooserType" %>
<%@ include file="/include.jsp" %>
<jsp:useBean id="runner" scope="request" type="jetbrains.buildserver.metarunner.xml.RunnerSpec"/>

<l:settingsGroup title="${runner.shortName} Parameters">
  <c:forEach items="${runner.parameterDefs}" var="p">
    <jsp:useBean id="p" type="jetbrains.buildserver.metarunner.xml.ParameterDef"/>
    <tr>
      <th>
        <c:if test="${not p.parameterType.name eq 'hidden'}">
          <label for="${p.key}">${p.shortName}:</label>
        </c:if>
      </th>
      <td>
        <c:choose>
          <c:when test="${p.parameterType.name eq 'text'}">
            <c:set var="text" value='<%= (TextType) p.parameterType() %>'/>
            <c:choose>
              <c:when test="${text.useTextArea}">
                <props:multilineProperty name="${p.key}" value="${p.defaultValue}"/>
              </c:when>
              <c:otherwise>
                <props:textProperty name="${p.key}" value="${p.defaultValue}"/>
              </c:otherwise>
            </c:choose>
          </c:when>
          <c:when test="${p.parameterType.name eq 'hidden'}">
            <props:hiddenProperty name="${p.key}" value="${p.defaultValue}"/>
          </c:when>
          <c:when test="${p.parameterType.name eq 'list'}">
            <c:set var="chooser" value='<%= (ChooserType) p.parameterType() %>'/>
            <props:selectProperty name="${p.key}">
              <c:forEach items="${chooser.items}" var="item">
                <props:option value="${item.value}" selected="${item.default}">${item.description}</props:option>
              </c:forEach>
            </props:selectProperty>
          </c:when>
        </c:choose>
        <span class="smallNote">${p.description}</span>
      </td>
    </tr>
  </c:forEach>
</l:settingsGroup>

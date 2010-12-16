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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"  %>
<jsp:useBean id="specs" scope="request" type="jetbrains.buildserver.metarunner.editor.SpecsBean"/>
<jsp:useBean id="descr" scope="request" type="jetbrains.buildServer.web.openapi.PluginDescriptor"/>

<div class="metaRunnersSettings">
<h3>Configured meta-runners:</h3>

  <p>There are <strong>${specs.size}</strong> meta-runners configured.</p>

  <table class="metaRunnersTable">
  <thead>
  <tr>
    <td>Runner Name</td>
    <td>Description</td>
    <td>Used Runners</td>
    <td>Referred from</td>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="it" items="${specs.runnerSpecs}">
    <tr>
      <td><c:out value="${it.runType}"/></td>
      <td style="width:30%">
        <c:out value="${it.shortName}"/>
        <div class="smallNote"><bs:trimWithTooltip>${it.description}</bs:trimWithTooltip></div>
      </td>
      <td>
        <c:forEach var="ii" items="${it.basedOnRunners}" varStatus="s">
          <c:if test="${not s.first}">, </c:if>
          <c:out value="${ii}"/>
        </c:forEach>
      </td>
      <td>
        <c:set var="isFirst" value="${true}"/>
        <c:set var="separator"><br /></c:set>
        <c:forEach var="ii" items="${it.configurationReferences}" varStatus="s">
          <c:if test="${not isFirst}">${separator}</c:if>
          <c:set var="isFirst" value="${false}"/>
          <bs:buildTypeLinkFull buildType="${ii.buildType}"/>
        </c:forEach>
        <c:forEach var="ii" items="${it.metaRunnerReferences}" varStatus="s">
          <c:if test="${not isFirst}">${separator}</c:if>
          <c:set var="isFirst" value="${false}"/>
          Meta-Runner :: <c:out value="${ii.metaRunnerName}"/>
        </c:forEach>
        <c:if test="${isFirst}">No references</c:if>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</div>
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="runner" scope="request" type="jetbrains.buildserver.metarunner.RunnerSpecBean"/>

<c:forEach items="${runner.parameterDefs}" var="p">
  <div class="parameter">
    <c:out value="${p.shortName}"/>: <props:displayValue name="${p.key}" emptyValue="<empty>"/>
  </div>
</c:forEach>

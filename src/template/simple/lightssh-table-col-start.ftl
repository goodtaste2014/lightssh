<td <#rt/>
<#if parameters.cssClass?exists>
 class="${parameters.cssClass?html}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
><@s.property value="stack.findValue(parameters.value)"/>
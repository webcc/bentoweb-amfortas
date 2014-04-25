<?xml version="1.0"?>
<!--
	(c) BenToWeb 2004-2007
-->

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
>
	 
<xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>

	<xsl:template match="/">
   		<tcdls>
   			<xsl:apply-templates select="//*[local-name()='testCaseDescription']"/>
   		</tcdls>
  	</xsl:template>

  	<xsl:template match="*[local-name()='testCaseDescription']">
  		<xsl:variable name="id" select="./@id"/>
   		<tcdl id="{$id}">
   			<xsl:apply-templates/>
   		</tcdl>
  	</xsl:template>
  	
  	<xsl:template match="@*|node()">
   		<xsl:copy>
      		<xsl:apply-templates select="@*|node()" />
   		</xsl:copy>
	</xsl:template> 	
 
</xsl:stylesheet>

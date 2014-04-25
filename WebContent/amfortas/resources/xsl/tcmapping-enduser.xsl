<?xml version="1.0"?>
<!--
	(c) BenToWeb 2004-2007

	filters test cases that is for end users (at least one testMode)
-->

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
>
	 
<xsl:output method="xml" 
	media-type="text/xml" 
	encoding="UTF-8" 
	indent="yes" 
	omit-xml-declaration="no"
/>

  	<xsl:template match="tcdls">
   		<tcdls>
   			<xsl:apply-templates select="tcdl" />
   		</tcdls>
	</xsl:template>
	
  	<xsl:template match="tcdl">
		<xsl:if test="normalize-space(*[local-name()='testCase']/*[local-name()='requiredTests']/*[local-name()='testModes']//*[local-name()='testMode']) = 'enduser'">
		  	<xsl:variable name="id" select="./@id"/>
	   		<tcdl id="{$id}">
	   			<xsl:apply-templates select="*[local-name()='testCase']" />
	   		</tcdl>
   		</xsl:if>
	</xsl:template>


  	<xsl:template match="*[local-name()='testCase']">
			<testCase>
				<xsl:apply-templates />
			</testCase>
  	</xsl:template>


 <xsl:template match="@*|node()" >
 	<xsl:copy>
 		<xsl:apply-templates select="@*|node()"/>
 	</xsl:copy>
</xsl:template>

</xsl:stylesheet>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:variable name="base-url" select="navigation/base-url"/>

  <xsl:template match="group">
    <xsl:variable name="link" select="@link"/>
    <xsl:variable name="desc" select="desc"/>
		<ul class="menulist">
			<xsl:apply-templates select=".//item"/>
		</ul>
  </xsl:template>

  <xsl:template match="item">
  <xsl:variable name="link" select="@link"/>
  <xsl:variable name="desc" select="title"/>
      <li id="{$link}">
		<a title="{$desc}" href="{$base-url}{$link}"><xsl:value-of select="title"/></a>
	</li>
  </xsl:template>
</xsl:stylesheet>


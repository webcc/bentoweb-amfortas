<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- ===== identity.xsl =====
    Version: 2.0
    Date: 2005-12-21
    Description:
    Simple identity stylesheet for XML documents defined in mode copy.
    -->

  <xsl:template match="*" mode="copy">
    <xsl:element name="{name()}">
      <xsl:apply-templates select="node()|@*" mode="copy" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="@*|text()|processing-instruction()|comment()"
    mode="copy">
    <xsl:copy />
  </xsl:template>

</xsl:stylesheet>

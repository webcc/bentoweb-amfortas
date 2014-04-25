<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:dir="http://bentoweb.org/ns/2005/cvsdir"
  xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="dir">

  <!-- ===== identity.xsl =====
    Version: 1.0
    Date: 2005-12-21
    Description:
    Main stylesheet for the CVS directory listing.
  -->

  <xsl:include href="util/identity.xslt" />
  <xsl:param name="lang" />

  <xsl:template match="/">
    <xsl:apply-templates select="dir:directory" />
  </xsl:template>

  <xsl:template match="dir:directory">
    <html lang="{$lang}" xml:lang="{$lang}">
      <xsl:call-template name="head" />
      <xsl:call-template name="body" />
    </html>
  </xsl:template>

  <xsl:template name="head">
    <head>
      <title>BenToWeb Test Suite</title>
    </head>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <ol>
        <xsl:apply-templates select="dir:file" />
      </ol>
    </body>
  </xsl:template>

  <xsl:template match="dir:file">
    <li>
      <a href="{@name}"><xsl:value-of select="@name" /></a>
    </li>
  </xsl:template>

</xsl:stylesheet>

<?xml version="1.0"?>
<!--
	(c) BenToWeb 2004-2007
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	 
  <xsl:include href="main-navigation.xslt"/>
  <xsl:include href="main-forms-styling.xsl"/>
  <!--<xsl:include href="forms-styling.xsl"/>-->

<xsl:output method="xml" media-type="text/html" encoding="UTF-8" indent="yes" omit-xml-declaration="no"
  doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" 
/>

  <xsl:param name="contextPath"/>
  <xsl:param name="servletPath" select="string('/samples')"/>
  <xsl:param name="sitemapURI"/>

  <xsl:variable name="directory" select="substring-before($servletPath,$sitemapURI)"/>
  <!-- assume that sitemapURIs don't occur in servletPath more than once -->
  <xsl:variable name="sitemap" select="concat($directory,'sitemap.xmap')"/>
  
  <xsl:template match="site">
   <html>
     <head>
       <!-- <title>Amfortas - <xsl:value-of select="title"/></title>-->
       <title>Amfortas - Test Case Evaluation Framework</title>
       <!--<link rel="stylesheet" href="{$contextPath}/styles/main.css" title="Default Style"/>-->
       <link rel="stylesheet" type="text/css" href="resources/css/main.css" media="all"/>
       <meta name="description" content="User evaluation platform for web accessibility tests" />
	   <meta name="author" content="www.bentoweb.org" />
       <meta name="keywords" content="BenToWeb, Web, Benchmarking" />
	   <!-- to be extended ... -->
     </head>
     <body>
	   <xsl:apply-templates select="header" />
	   <xsl:apply-templates select="navigation" />
       <xsl:apply-templates select="content" />
       <xsl:apply-templates select="footer" />
     </body>
   </html>
  </xsl:template>
  
   <xsl:template match="header">
  	<div id="Header">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
 
  <xsl:template match="navigation">	  
  	<div id="Menu">
		<xsl:apply-templates/>
	</div>
  </xsl:template>
  
   <xsl:template match="content">
  	<div id="Content">
      	<xsl:apply-templates/>
    </div>
  </xsl:template>
  
   <xsl:template match="footer">
  	<div id="Footer">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
   
  <xsl:template match="title">
   <h2>
     <xsl:apply-templates/>
   </h2>
  </xsl:template>
  
    <xsl:template match="sendmail">
   <p>
     <xsl:apply-templates/>
   </p>
  </xsl:template>
  
  <xsl:template match="*">
      <!-- remove element prefix (if any) -->
      <xsl:element name="{local-name()}">
        <!-- process attributes -->
        <xsl:for-each select="@*">
          <!-- remove attribute prefix (if any) -->
          <xsl:attribute name="{local-name()}">
            <xsl:value-of select="."/>
          </xsl:attribute>
        </xsl:for-each>
        <xsl:apply-templates/>
      </xsl:element>
  </xsl:template>
  
<!--
  <xsl:template match="para">
   <p>
     <xsl:apply-templates/>
   </p>
  </xsl:template>

  <xsl:template match="link">
   <a href="{@href}">
     <xsl:apply-templates/>
   </a>
  </xsl:template>

  <xsl:template match="error">
    <span class="error">
      <xsl:apply-templates/>
    </span>
  </xsl:template>

 <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>-->
</xsl:stylesheet>

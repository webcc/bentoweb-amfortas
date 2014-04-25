<?xml version="1.0"?>
<!--
	(c) BenToWeb 2004-2007
-->

<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  		xmlns="http://www.w3.org/1999/xhtml"
  		xmlns:btw="http://bentoweb.org/refs/TCDL1.1"
  		xmlns:html="http://www.w3.org/1999/xhtml"
  		xmlns:xlink="http://www.w3.org/1999/xlink"
  		xmlns:dc="http://purl.org/dc/elements/1.1/"
  		exclude-result-prefixes="btw dc html xlink xml">
	 
  <xsl:include href="main-navigation.xslt"/>
  <xsl:include href="main-forms-styling.xsl"/>
  <xsl:include href="tcdl1.1_to_xhtml_print_en.xslt"/>

<xsl:output method="xml" media-type="text/html" encoding="UTF-8" indent="yes" omit-xml-declaration="no"
  doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" 
/>

  <!-- The test suite -->
  <xsl:param name="testcaseID" />
  <xsl:param name="scenarioID" />
  <xsl:param name="testsuiteID" />
  <xsl:param name="language" />
  <xsl:param name="q_counter" />
  
  
  <!-- In case locale!=nl === not supported language -->
  <xsl:variable name="xml_lang">
  	<xsl:choose>
  		<xsl:when test="starts-with($locale,'nl')">
    		<xsl:text>nl</xsl:text>
  		</xsl:when>
  		<xsl:when test="starts-with($locale,'en')">
    		<xsl:text>en</xsl:text>
  		</xsl:when>  		
  		<xsl:otherwise>
  			<xsl:text>en</xsl:text>
    		<!-- <xsl:value-of select="$locale"/> -->
  		</xsl:otherwise>
	</xsl:choose>
  </xsl:variable>
  <xsl:param name="testfileURI" />
  
  <!-- get text from runtest.xml for localisation-->
  <xsl:variable name="dontknow_label" select="//dontknow_label" />
  <xsl:variable name="dontknow_button" select="//dontknow_button" />
  <xsl:variable name="contentHeader" select="//runheader" />
  <xsl:variable name="contentNote" select="//runnote" />
  <!-- always point to the first test file, there must be at least one -->
  <xsl:variable name="testfilename" select="//btw:files/btw:file[1]/@xlink:href" />
  <xsl:variable name="tcText1" select="//runtext1" />
  <xsl:variable name="tcText2" select="//runtext2" />
  <xsl:variable name="buttonlabel" select="//runbutton" />
  <xsl:variable name="fslabel" select="//runlegend" />
  
  <xsl:template match="site">
   <html xmlns="http://www.w3.org/1999/xhtml" lang="{$xml_lang}" xml:lang="{$xml_lang}">
     <head>
       <!-- <title>Amfortas - <xsl:value-of select="title"/></title>-->
       <title>Amfortas - <xsl:value-of select="$contentHeader"/></title>
       <link rel="stylesheet" type="text/css" href="resources/css/main.css" media="all"/>
       <meta name="description" content="User evaluation platform for web accessibility tests" />
	   <meta name="author" content="www.bentoweb.org" />
       <meta name="keywords" content="BenToWeb, Web, Benchmarking" />
	   <!-- to be extended ... -->
     </head>
     <body>
	   <xsl:apply-templates select="header" />
	   <xsl:apply-templates select="navigation" />
	   <xsl:apply-templates select="btw:testCaseDescription" />
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
  
   <xsl:template match="btw:testCaseDescription">
  	<div id="Content">
  	<xsl:variable name="testcaseID_s" select="substring-before($testcaseID,'.xml')"/>
  		<h1><xsl:value-of select="$contentHeader"/>- [<xsl:value-of select="$q_counter"/>]</h1>
  		<!-- <dl>
  			<dt>Test Case:</dt>
  			<dd><a target="_blank" href="http://www.bentoweb.org/ts/XHTML1/metadata/{$testcaseID}"><xsl:value-of select="substring-before($testcaseID,'.xml')"/></a></dd>
			<dt>Scenario:</dt>
			<dd><xsl:value-of select="$scenarioID"/></dd>  			
  		</dl> -->
  		 <div>
  		 	<p><xsl:value-of select="$contentNote"/></p> 
  		 	<!-- 
  		 		<xsl:apply-templates select="btw:testCase/btw:requiredTests/btw:scenario[@id=$scenarioID]/btw:scenarioNotes"/>
  			 -->
  		 </div>
  		<!-- present error message -->
		<fieldset class="runtest">
			<div style="padding-top: 0.2em;">
			<!-- <legend><xsl:value-of select="$fslabel"/></legend> -->
			<form method="post" action="store-test">
      		<p><xsl:apply-templates select="btw:testCase/btw:requiredTests/btw:scenario[@id=$scenarioID]"/></p>
      		<p>
      		<input type="submit" value="{$buttonlabel}" name="subQuest" />

      		<label style="display:block;margin-top:2em;" for="dontknow_reason">
      			<xsl:value-of select="$dontknow_label"/>
      		</label> 
      		<textarea cols="50" rows="5" id="dontknow_reason" name="dontknow_reason">...</textarea>
      		<input type="submit" value="{$dontknow_button}" name="dontknow" />
      		<input type="hidden" value="{$testcaseID}" name="testcaseId" />
      		<input type="hidden" value="{$scenarioID}" name="scenarioId" />
      		<input type="hidden" value="{$testsuiteID}" name="testsuiteId" />
      		</p>
      		</form>
      		</div>
      	</fieldset>
    </div>
 	</xsl:template>
  
   <xsl:template match="footer">
  	<div id="Footer">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="btw:userGuidance">
  	<div class="userguidance">
	<xsl:for-each select="btw:p">
        	<p><xsl:apply-templates /></p>
    </xsl:for-each>  
    </div>
  </xsl:template>
  
  <!-- 
  	questions
   -->  
	<xsl:template match="btw:scenario">
	<xsl:if test="btw:userGuidance[@xml:lang=$xml_lang]">
		<div style="padding-top: 0.5em">
		<xsl:for-each select="btw:userGuidance[@xml:lang=$xml_lang]/btw:p">
        	<p><xsl:apply-templates /></p>
       	 </xsl:for-each>
       	</div>
	</xsl:if>
	<!--   ===   Detect the question type   ===   -->
	<xsl:choose>
	<!--   ===   Yes-no question   ===   -->
	<xsl:when test="btw:questions/btw:yesNoQuestion">
    	<p><a href="{concat($testfileURI,$testfilename)}" target="_blank"><xsl:value-of select="$tcText1"/></a> <xsl:value-of select="$tcText2"/></p>
    	<xsl:for-each select="btw:questions/btw:yesNoQuestion/btw:questionText[@xml:lang=$xml_lang]/btw:p">
        	<p class="txtMedb"><xsl:apply-templates /></p>
    	</xsl:for-each>
        <p>
        	<input type="radio" name="yesOrNo"
             value="{normalize-space(btw:questions/btw:yesNoQuestion/btw:optionYes/@value)}"><xsl:attribute name="id">yesRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute></input><label><xsl:attribute name="for">yesRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute><xsl:call-template name="getYes" /></label>
            <xsl:text disable-output-escaping="yes">&lt;br /></xsl:text>
            <input type="radio" name="yesOrNo"
             value="{normalize-space(btw:questions/btw:yesNoQuestion/btw:optionNo/@value)}"><xsl:attribute name="id">noRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute></input><label><xsl:attribute name="for">noRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute><xsl:call-template name="getNo" /></label>
            <xsl:text disable-output-escaping="yes">&lt;br /></xsl:text>
            <input type="radio" name="yesOrNo"
             value="0">
             	<xsl:attribute name="id">otherRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute>
             </input>
             <label>
             	<xsl:attribute name="for">otherRadio<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute>
             	<xsl:value-of select="btw:questions/btw:yesNoQuestion/btw:optionOther[@xml:lang=$xml_lang]/btw:p"/>
             </label>
        </p>
        <input type="hidden" value="yesnoQuestion" name="questType" />
     </xsl:when>
  
     <!--   ===   Likert scale   ===   -->
     <xsl:when test="btw:questions/btw:likertScale">
         <p><a href="{concat($testfileURI,$testfilename)}" target="_blank"><xsl:value-of select="$tcText1"/></a> <xsl:value-of select="$tcText2"/></p>
         <p class="txtMedb">
            <xsl:copy-of select="btw:questions/btw:likertScale/btw:questionText[@xml:lang=$xml_lang]/btw:p/node()" />
         </p>
         <ul style="list-style-type:none;">
          	<xsl:for-each select="btw:questions/btw:likertScale/btw:likertLevel">
          		<li>
          	    <xsl:variable name="likval" select="btw:value" />
          	    <input type="radio" value="{$likval}" name="likertScale" id="_{$likval}" />
          		<label for="_{$likval}"><xsl:copy-of select="btw:label[@xml:lang=$xml_lang]/node()" /></label>         		
          		</li>
            </xsl:for-each>
         </ul>
         <input type="hidden" value="likertScale" name="questType" />
     </xsl:when>
  
     <!--   ===   Multiple-choice question   ===   -->
     <xsl:when test="btw:questions/btw:multipleChoice">
         <p><a href="{concat($testfileURI,$testfilename)}" target="_blank"><xsl:value-of select="$tcText1"/></a> <xsl:value-of select="$tcText2"/></p>
         <xsl:for-each select="btw:questions/btw:multipleChoice/btw:questionText[@xml:lang=$xml_lang]/btw:p">
         	<p class="txtMedb"><xsl:apply-templates /></p>
         </xsl:for-each>
         <xsl:choose>
            <xsl:when test="starts-with(btw:questions/btw:multipleChoice/@select, 'one')">
            	<p>
                	<xsl:for-each select="btw:questions/btw:multipleChoice/btw:choice">
                		<xsl:if test="btw:label[@xml:lang=$xml_lang]!=0">
                    		<input type="radio" name="multiplechoice" id="{concat('choice_', position())}" value="{normalize-space(btw:value)}" /><label for="{concat('choice_', position())}"><xsl:value-of select="btw:label[@xml:lang=$xml_lang]"/></label><xsl:text disable-output-escaping="yes">&lt;br /></xsl:text><!-- instead of <br />-->
                    	</xsl:if>
                    </xsl:for-each>
                </p>
                <input type="hidden" value="multipleChoice" name="questType" />
            </xsl:when>
            <xsl:otherwise>
               	<p>
                	<xsl:for-each select="btw:questions/btw:multipleChoice/btw:choice">
                		<xsl:if test="btw:label[@xml:lang=$xml_lang]!=0">
                    		<input type="checkbox" name="multiplechoice" id="{concat('choice_', position())}" value="{normalize-space(btw:value)}" /><label for="{concat('choice_', position())}"><xsl:value-of select="btw:label[@xml:lang=$xml_lang]"/></label><xsl:text disable-output-escaping="yes">&lt;br /></xsl:text><!-- instead of <br />-->
                    	</xsl:if>
                    </xsl:for-each>
                </p>
                <input type="hidden" value="multipleChoice" name="questType" />
            </xsl:otherwise>
        </xsl:choose>
     </xsl:when>
  
     <!--   ===   Open-ended question   ===   -->
     <xsl:when test="btw:questions/btw:openQuestion">
       	 <p><a href="{concat($testfileURI,$testfilename)}" target="_blank"><xsl:value-of select="$tcText1"/></a> <xsl:value-of select="$tcText2"/></p>
     	<xsl:for-each select="btw:questions/btw:openQuestion/btw:questionText[@xml:lang=$xml_lang]/btw:p">
        	<p class="txtMedb"><xsl:apply-templates /></p>
       	 </xsl:for-each>
            <p>
            <label>
                <xsl:attribute name="for">usertext<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute>
                <xsl:call-template name="getTextAreaLabel" />
                </label><xsl:text disable-output-escaping="yes">&lt;br /></xsl:text><!-- instead of <br />-->
                <!-- When outputting XML, textarea must not be an 'empty element', because browsers do not support this! -->
                <textarea
                   name="openTxt" 
                   cols="40" 
                   rows="15"
                   onfocus="if(this.value=='...')this.value='';"
                   onblur="if(this.value=='')this.value='...';"><xsl:attribute name="id">usertext<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute><xsl:text disable-output-escaping="yes">...</xsl:text></textarea>
            </p>
            <input type="hidden" value="openQuestion" name="questType" />
     </xsl:when>
  
     <!--   ===   Yes-no question combined with open-ended question   ===   -->
     <xsl:when test="btw:questions/btw:yesNoOpenQuestion">
        <p><a href="{concat($testfileURI,$testfilename)}" target="_blank"><xsl:value-of select="$tcText1"/></a> <xsl:value-of select="$tcText2"/></p>
     	<xsl:for-each select="btw:questions/btw:yesNoOpenQuestion/btw:optionYes/preceding-sibling::btw:questionText[@xml:lang=$xml_lang]/btw:p">
        	<p class="txtMedb"><xsl:apply-templates /></p>
        </xsl:for-each>
        <p>
           <input type="radio" name="yesOrNo" id="yesRadio" 
                  value="{normalize-space(btw:questions/btw:yesNoOpenQuestion/btw:optionYes/@value)}" /><label for="yesRadio"><xsl:call-template name="getYes" /></label>
           <xsl:text disable-output-escaping="yes">&lt;br /></xsl:text>
           <input type="radio" name="yesOrNo" id="noRadio"
                  value="{normalize-space(btw:questions/btw:yesNoOpenQuestion/btw:optionNo/@value)}" /><label for="noRadio"><xsl:call-template name="getNo" /></label>
        </p>
        <xsl:for-each select="btw:questions/btw:yesNoOpenQuestion/btw:optionNo/following-sibling::btw:questionText[@xml:lang=$xml_lang]/btw:p">
        	<p><xsl:apply-templates /></p>
        </xsl:for-each>
        <p>
            <!-- When outputting XML, textarea must not be an 'empty element', because browsers do not support this! -->
            <label for="yesOrNoTxt">
     			<xsl:for-each select="btw:questions/btw:yesNoOpenQuestion/btw:optionOther[@xml:lang=$xml_lang]/btw:p">
        			<p><xsl:apply-templates /></p>
       	 		</xsl:for-each>              		
              </label>
              <textarea
              		  id="yesOrNoTxt"
              		  name="yesOrNoTxt" 
                      cols="40" 
                      rows="15"
                      onfocus="if(this.value=='...')this.value='';"
                      onblur="if(this.value=='')this.value='...';"><xsl:attribute name="id">usertext<xsl:number count="btw:testCase/btw:requiredTests/btw:scenario" value="position()"/></xsl:attribute><xsl:text disable-output-escaping="yes">...</xsl:text></textarea>
         </p>
         <input type="hidden" value="yesnoOpenQuestion" name="questType" />
      </xsl:when>
      </xsl:choose>
	</xsl:template>

</xsl:stylesheet>

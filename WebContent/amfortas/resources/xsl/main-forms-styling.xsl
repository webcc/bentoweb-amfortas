<?xml version="1.0"?>
<!--
  Copyright 1999-2004 The Apache Software Foundation

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
                exclude-result-prefixes="fi">

  <!--
  not needed for simple forms :)
  <xsl:include href="forms-page-styling.xsl"/>
  <xsl:include href="forms-field-styling.xsl"/>
  -->
  <xsl:param name="locale" select="locale" />
  <xsl:param name="deflocale" select="'en'" />
  <xsl:param name="i18n_path"  select="'../i18n/'" />
  <xsl:param name="recruitmsgsfile" select="'recruit-messages'"/>
  <xsl:param name="msgkey" select="'amformas.recruit.errorsum.msgtitle'"/>
 
  <!--
    fi:group : default is to enclose items in a div
  -->
  <xsl:template match="fi:group">
    <!-- <div title="{fi:hint}"> -->
    <div>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates mode="group-layout" select="."/>
    </div> 
  </xsl:template>

  <!--
    Group items layout : default is no layout
  -->
  <xsl:template match="fi:group" mode="group-layout">
   <ul class="form-group">
    <xsl:apply-templates select="fi:items/*"/>
   </ul> 
  </xsl:template>

  <!--
    Group layout radio : combination - which is a selection list (radio)
    with text input field.
  -->
  
  <xsl:template match="fi:group[fi:styling/@layout='combination']  ">			 
 	 <li>
    	<xsl:choose>
    		<xsl:when test="fi:items/fi:field[1]/fi:styling/@list-type='radio'">
    			<xsl:variable name="id" select="fi:items/fi:field[1]/@id"/>
				<xsl:variable name="value" select="fi:items/fi:field[1]/fi:value"/>
				<xsl:variable name="idtxt" select="fi:items/fi:field[2]/@id"/>
				<xsl:variable name="valuetxt" select="fi:items/fi:field[2]/fi:value"/>
				<xsl:variable name="hinttxt" select="fi:items/fi:field[2]/fi:hint"/>	
				
		 	 	<xsl:apply-templates select="fi:items/fi:field[1]" mode="label">
		       		<xsl:with-param name="id" select="generate-id()"/>
		    	</xsl:apply-templates>
		    	<xsl:apply-templates select="fi:items/fi:field[1]" mode="common"/>
		    	<br/>	
		    	<xsl:for-each select="fi:items/fi:field[1]/fi:selection-list/fi:item">
 		   		<xsl:if test="position()!=last()">
 		       		<input type="radio" id="{generate-id()}" name="{$id}" value="{@value}">
        	   			<xsl:if test="@value = $value">
            	        	<xsl:attribute name="checked">checked</xsl:attribute>
 		           		</xsl:if>
	                	<xsl:apply-templates select="../.." mode="styling"/>
  		          	</input>
  		          	<xsl:apply-templates select="." mode="listlabel">  
        	    		<xsl:with-param name="id" select="generate-id()"/>
 		           	</xsl:apply-templates>
        	    	<br/>
    	        </xsl:if>
	            <xsl:if test="position()=last()">
 		           	<input type="radio" id="{generate-id()}" name="{$id}" value="{@value}">
        	   			<xsl:if test="@value = $value">
            	        	<xsl:attribute name="checked">checked</xsl:attribute>
       		     		</xsl:if>
        	        	<xsl:apply-templates select="../.." mode="styling"/>
   		         	</input>
   		         	<xsl:apply-templates select="." mode="listlabel">  
        	    		<xsl:with-param name="id" select="generate-id()"/>
        	    	</xsl:apply-templates>
     		       	<input type="text" id="{generate-id()}" name="{$idtxt}" value="{@valuetxt}" title="{$hinttxt}">
      		          	<xsl:apply-templates select="../.." mode="styling"/>
      		      	</input>
            		<br/>
        	    </xsl:if>
   			 	</xsl:for-each>   
  			</xsl:when>
  			<xsl:otherwise>
    			<xsl:variable name="id" select="fi:items/fi:multivaluefield/@id"/>
				<xsl:variable name="value" select="fi:items/fi:multivaluefield/fi:value"/>
				<xsl:variable name="idtxt" select="fi:items/fi:field/@id"/>
				<xsl:variable name="valuetxt" select="fi:items/fi:field/fi:value"/>
				<xsl:variable name="hinttxt" select="fi:items/fi:field/fi:hint"/>		

		 	 	<xsl:apply-templates select="fi:items/fi:multivaluefield" mode="label">
		       		<xsl:with-param name="id" select="generate-id()"/>
		    	</xsl:apply-templates>
		    	<xsl:apply-templates select="fi:items/fi:field[1]" mode="common"/>
		    	<br/>

		    	<xsl:for-each select="fi:items/fi:multivaluefield/fi:selection-list/fi:item">
 		   		<xsl:if test="position()!=last()">
 		       		<input type="checkbox" id="{generate-id()}" name="{$id}" value="{@value}">
        	   			<xsl:if test="@value = $value">
            	        	<xsl:attribute name="checked">checked</xsl:attribute>
 		           		</xsl:if>
	                	<xsl:apply-templates select="../.." mode="styling"/>
  		          	</input>
  		          	<xsl:apply-templates select="." mode="listlabel">  
        	    		<xsl:with-param name="id" select="generate-id()"/>
 		           	</xsl:apply-templates>
        	    	<br/>
    	        </xsl:if>
	            <xsl:if test="position()=last()">
 		           	<input type="checkbox" id="{generate-id()}" name="{$id}" value="{@value}">
        	   			<xsl:if test="@value = $value">
            	        	<xsl:attribute name="checked">checked</xsl:attribute>
       		     		</xsl:if>
        	        	<xsl:apply-templates select="../.." mode="styling"/>
   		         	</input>
   		         	<xsl:apply-templates select="." mode="listlabel">  
        	    		<xsl:with-param name="id" select="generate-id()"/>
        	    	</xsl:apply-templates>
     		       	<input type="text" id="{generate-id()}" name="{$idtxt}" value="{@valuetxt}" title="{$hinttxt}">
      		          	<xsl:apply-templates select="../.." mode="styling"/>
      		      	</input>
            		<br/>
        	    </xsl:if>
   			 	</xsl:for-each>   
  			</xsl:otherwise>
    	</xsl:choose>
 	 </li>
  </xsl:template>
	
  <!--+
      | Generic fi:field : produce an <input>
      +
      -->
  <xsl:template match="fi:field"> 
  <li>
    <xsl:apply-templates select="." mode="label">
       <xsl:with-param name="id" select="generate-id()"/>
    </xsl:apply-templates>
    <xsl:apply-templates select="." mode="common"/>
    <xsl:if test="contains(@id, 'other')=false">
    <br/>
    </xsl:if>
    <input size="35" name="{@id}" id="{generate-id()}" value="{fi:value}" title="{fi:hint}" type="text">
      <xsl:apply-templates select="." mode="styling"/>
    </input>
    <!-- <xsl:apply-templates select="." mode="common"/> -->
    </li>
  </xsl:template>

 <!--+
      | Common stuff like fi:validation-message, @required.
      +-->
  <xsl:template match="fi:*" mode="common">
    <!-- required mark -->
    <xsl:if test="@required='true'">
      <span class="forms-field-required">*</span>
      <!-- validation message -->
    </xsl:if>
     <xsl:apply-templates select="fi:validation-message"/>
  </xsl:template>

  <!--+
      |
      +-->
  <xsl:template match="fi:validation-message">
<!--     <a href="#" class="forms-validation-message" onclick="alert('{normalize-space(.)}');return false;">&#160;!&#160;</a> -->
    <span class="forms-validation-message"><a href="#errorsummesgs"><xsl:value-of select="."/></a></span>
    
  </xsl:template>

  <!--+
      | Hidden fi:field : produce input with type='hidden'
      +-->
  <xsl:template match="fi:field[fi:styling/@type='hidden']" priority="2">  
    <input type="hidden" name="{@id}" id="{@id}" value="{fi:value}">
      <xsl:apply-templates select="." mode="styling"/>
    </input>
  </xsl:template>

  <!--+
      | fi:field with a selection list and @list-type 'radio' : produce
      | radio-buttons oriented according to @list-orientation
      | ("horizontal" or "vertical" - default)
      -->
  <xsl:template match="fi:field[fi:selection-list][fi:styling/@list-type='radio']" priority="2">
  <li>
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="value" select="fi:value"/>
    <xsl:variable name="vertical" select="string(fi:styling/@list-orientation) != 'horizontal'"/>
    <xsl:choose>
      <xsl:when test="$vertical">
        <xsl:apply-templates select="." mode="label">
           <xsl:with-param name="id" select="generate-id()"/>
        </xsl:apply-templates>
        <xsl:apply-templates select="." mode="common"/>
      	<br/>
          <xsl:for-each select="fi:selection-list/fi:item">
                <input type="radio" id="{generate-id()}" name="{$id}" value="{@value}">
                  <xsl:if test="@value = $value">
                    <xsl:attribute name="checked">checked</xsl:attribute>
                  </xsl:if>
                  <xsl:apply-templates select="../.." mode="styling"/>
                </input>
               	 <xsl:apply-templates select="." mode="listlabel">  
                  <xsl:with-param name="id" select="generate-id()"/>
                </xsl:apply-templates>
            <br/>
          </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <span title="{fi:hint}">
          <xsl:for-each select="fi:selection-list/fi:item">
            <input type="radio" id="{generate-id()}" name="{$id}" value="{@value}">
              <xsl:if test="@value = $value">
                <xsl:attribute name="checked">checked</xsl:attribute>
              </xsl:if>
              <xsl:apply-templates select="../.." mode="styling"/>
            </input>
            <xsl:apply-templates select="." mode="listlabel">
              <xsl:with-param name="id" select="generate-id()"/>
            </xsl:apply-templates>
          </xsl:for-each>
        </span>
        <xsl:apply-templates select="." mode="common"/>
      </xsl:otherwise>
    </xsl:choose>
    </li>
  </xsl:template>

  <!--+
      | Handling the common styling. You may only add attributes to the output
      | in this template as later processing might add attributes too, for
      | example @checked or @selected
      +-->
  <xsl:template match="fi:*" mode="styling">
    <xsl:apply-templates select="fi:styling/@*" mode="styling"/>

  	<xsl:if test="@state = 'disabled'">
  		<xsl:attribute name="disabled">disabled</xsl:attribute>
  	</xsl:if>

    <!--+ 
        | @listbox-size needs to be handled separately as even if it is not
        | specified some output (@size) must be generated.
        +-->
    <xsl:if test="self::fi:field[fi:selection-list][fi:styling/@list-type = 'listbox'] or
                  self::fi:multivaluefield[not(fi:styling/@list-type = 'checkbox')]">
      <xsl:variable name="size">
        <xsl:value-of select="fi:styling/@listbox-size"/>
        <xsl:if test="not(fi:styling/@listbox-size)">5</xsl:if>
      </xsl:variable>
      <xsl:attribute name="size">
        <xsl:value-of select="$size"/>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template match="fi:styling/@*" mode="styling">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template match="fi:styling/@submit-on-change" mode="styling">
    <xsl:if test=". = 'true'">
      <xsl:attribute name="onchange">forms_submitForm(this)</xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template match="fi:styling/@list-type | fi:styling/@list-orientation |
                       fi:styling/@listbox-size | fi:styling/@format | fi:styling/@layout"
                mode="styling">
    <!--+
        | Ignore marker attributes so they don't go into the resuling HTML.
        +-->
  </xsl:template>
  
  <xsl:template match="fi:styling/@type" mode="styling">
    <!--+ 
        | Do we have a duplicate semantic usage of @type?
        | @type is only a marker for the stylesheet in general, but some of the
        | types must/should be in the HTML output too.
        +-->
    <xsl:variable name="validHTMLTypes"
                  select="'text hidden checkbox radio password image reset submit'"/>
    <xsl:if test="normalize-space(.) and
                  contains(concat(' ', $validHTMLTypes, ' '), concat(' ', ., ' '))">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>
  
  <!--+
      | Labels for form elements.
      +-->
  <xsl:template match="fi:*" mode="label">
    <xsl:param name="id" select="id"/>
	
	<xsl:variable name="lId" select="@id"/>
    <xsl:variable name="labId" select="concat($lId,'_label')"/>
    
	<label class="main" id="{$labId}" for="{$id}" title="{fi:hint}">
      <xsl:copy-of select="fi:label/node()"/>
    </label>
  </xsl:template>

  <xsl:template match="fi:*" mode="listlabel">
    <xsl:param name="id" select="@id"/>
    <label for="{$id}" title="{fi:hint}">
      <xsl:copy-of select="fi:label/node()"/>
    </label>
  </xsl:template>
  <!--+
      | Labels for pure outputs must not contain <label/> as there is no element to point to.
      +-->
      <!-- 
  <xsl:template match="fi:output | fi:field[fi:styling/@type='output'] | fi:messages | fi:field[fi:selection-list][fi:styling/@list-type='radio']| fi:multivaluefield[fi:selection-list][fi:styling/@list-type='checkbox']" mode="label">
    <xsl:copy-of select="fi:label/node()"/>
  </xsl:template>
   -->
  <!--+
      | fi:booleanfield : produce a checkbox
      | A hidden booleanfield is not a checkbox, so 'value' contains 
      | the value and not the checked attribute
      +
  <xsl:template match="fi:booleanfield">
    <input id="{@id}" type="checkbox" value="true" name="{@id}" title="{fi:hint}">
      <xsl:apply-templates select="." mode="styling"/>
      <xsl:choose>
        <xsl:when test="./fi:styling[@type='hidden']">
          <xsl:if test="fi:value = 'false'">
            <xsl:attribute name="value">false</xsl:attribute>
          </xsl:if>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="fi:value = 'true'">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </input>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>-->

  <!--+
      | fi:booleanfield with @type 'output' : rendered as text
      +
  <xsl:template match="fi:booleanfield[fi:styling/@type='output']">
    <xsl:choose>
      <xsl:when test="fi:value = 'true'">
        yes
      </xsl:when>
      <xsl:otherwise>
        no
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>-->

  <!--+
      | fi:action
      +-->
  <xsl:template match="fi:action">
    <input id="{@id}" type="submit" name="{@id}" title="{fi:hint}">
      <xsl:attribute name="value"><xsl:value-of select="fi:label/node()"/></xsl:attribute>
      <xsl:apply-templates select="." mode="styling"/>
    </input>
  </xsl:template>

  <!--+
      | fi:continuation-id : produce a hidden "continuation-id" input
      +-->
  <xsl:template match="fi:continuation-id">
    <xsl:variable name="name">
      <xsl:value-of select="@name"/>
      <xsl:if test="not(@name)">continuation-id</xsl:if>
    </xsl:variable>
    <div style="display: none;">
      <input name="{$name}" type="hidden" value="{.}"/>
    </div>
  </xsl:template>

  <!--+
      | fi:multivaluefield : produce a list of checkboxes
      +-->
  <xsl:template match="fi:multivaluefield[fi:styling/@list-type='checkbox']">
  <li>
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="values" select="fi:values/fi:value/text()"/>

    <!-- <span title="{fi:hint}"> -->
    <xsl:apply-templates select="." mode="label">
    	<xsl:with-param name="id" select="generate-id()"/>
    </xsl:apply-templates>
<!-- 
    <xsl:apply-templates select="." mode="common"/>
     -->
    <br/>
      <xsl:for-each select="fi:selection-list/fi:item">
        <xsl:variable name="value" select="@value"/>
        <input id="{generate-id()}" type="checkbox" value="{@value}" name="{$id}">
          <xsl:if test="$values[. = $value]">
            <xsl:attribute name="checked">checked</xsl:attribute>  
          </xsl:if>
        </input>
        <xsl:apply-templates select="." mode="listlabel">
          <xsl:with-param name="id" select="generate-id()"/>
        </xsl:apply-templates>
        <br/>
      </xsl:for-each>
    <!-- </span> -->
    <xsl:apply-templates select="." mode="common"/>
    </li>
  </xsl:template>
  
  <!--+
      | fi:multivaluefield : produce a multiple-selection list
      +-->
  <xsl:template match="fi:multivaluefield">
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="values" select="fi:values/fi:value/text()"/>

    <span title="{fi:hint}">
      <select id="{@id}" name="{$id}" multiple="multiple">
        <xsl:apply-templates select="." mode="styling"/>
        <xsl:for-each select="fi:selection-list/fi:item">
          <xsl:variable name="value" select="@value"/>
          <option value="{$value}">
            <xsl:if test="$values[. = $value]">
              <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="fi:label/node()"/>
          </option>
        </xsl:for-each>
      </select>
    </span>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>

  <!--+
      | fi:upload
      +-->
  <xsl:template match="fi:upload">
    <xsl:choose>
      <xsl:when test="fi:value">
        <!-- Has a value (filename): display it with a change button -->
        <span title="{fi:hint}">
          <xsl:text>[</xsl:text>
          <xsl:value-of select="fi:value"/>
          <xsl:text>] </xsl:text>
          <input type="button" id="{@id}" name="{@id}" value="..." onclick="forms_submitForm(this)"/>
        </span>
      </xsl:when>
      <xsl:otherwise>
        <input type="file" id="{@id}" name="{@id}" title="{fi:hint}" accept="{@mime-types}">
          <xsl:apply-templates select="." mode="styling"/>
        </input>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>

  <!--+
      | fi:form-template|fi:form-generated 
      +-->
  <xsl:template match="fi:form-template|fi:form-generated">
  	<xsl:if test="(count(//fi:validation-message)>0)">
  	<!-- For localization of message -->
	  	<xsl:variable name="msgpath">
	  		<xsl:value-of select="concat($i18n_path,$recruitmsgsfile)"/>
	  	</xsl:variable>
	  	<xsl:variable name="errorsummesgs_title">
		  	<xsl:choose>
	  			<xsl:when test="count(document(concat($msgpath,'_',$locale,'.xml'))/catalogue/message[@key=$msgkey])>0">
  					<xsl:value-of select="document(concat($msgpath,'_',$locale,'.xml'))/catalogue/message[@key=$msgkey]"/>
			  	</xsl:when>
	  			<xsl:otherwise>
					<xsl:value-of select="document(concat($msgpath,'.xml'))/catalogue/message[@key=$msgkey]"/>
			  	</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	 
	  	<div id="errorsummesgs" tabindex="0">
	  	<span><xsl:value-of select="$errorsummesgs_title"/></span>
	  		<ul>
	  		<xsl:for-each select="//fi:validation-message">	  			
				<xsl:variable name="lId" select="../@id"/>
				<xsl:variable name="labId" select="concat($lId,'_label')"/>
				<xsl:variable name="hint" select="../fi:hint"/>
				<xsl:variable name="error" select="."/>
				<xsl:choose>
					<xsl:when test="$error!=''">
						<li><a href="#{$labId}"><xsl:value-of select="."/>
						<xsl:if test="$hint!=''">
	  						:<xsl:value-of select="../fi:hint"/>
	  					</xsl:if>
	  					</a></li>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="$hint!=''">
	  						<li><a href="#{$labId}"><xsl:value-of select="../fi:hint"/></a></li>
	  					</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
	  		</xsl:for-each>
	  		</ul>
	  	</div>
    </xsl:if>
    
    <form>
      <xsl:copy-of select="@*"/>
      <xsl:attribute name="onsubmit">forms_onsubmit(); <xsl:value-of select="@onsubmit"/></xsl:attribute>
      <div><input type="hidden" name="forms_submit_id"/></div>      
      <xsl:apply-templates/>
      
      <xsl:if test="self::fi:form-generated">
        <input type="submit"/>
      </xsl:if>
    </form>
  </xsl:template>
 
 <!-- <xsl:template match="@*|node()" priority="-1">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template> -->
  
  <xsl:template match="@*|node()" priority="-1">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <!-- 
  ##################################
  ################################## needed later on
  ##################################
   -->

  <!--
    fi:group of type tabs
 
  <xsl:template match="fi:group[fi:styling/@type='tabs']">
    <xsl:variable name="active">
      <xsl:variable name="value" select="normalize-space(fi:state/fi:*/fi:value)"/>
      <xsl:choose>
        <xsl:when test="$value">
          <xsl:value-of select="$value"/>
        </xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="state-widget" select="fi:state/fi:*/@id"/>
    <xsl:variable name="id" select="generate-id()"/>

    <div id="{$id}" title="{fi:hint}">
   
      <xsl:if test="$state-widget">
        <input type="hidden" name="{$state-widget}" value="{$active}"/>
      </xsl:if>
  
      <div class="forms-tabArea">
        <xsl:for-each select="fi:items/fi:*">
          <xsl:variable name="pos" select="position() - 1"/>
          <span id="{$id}_tab_{$pos}" onclick="forms_showTab('{$id}', {$pos}, {last()}, '{$state-widget}')">
            <xsl:attribute name="class">
              <xsl:text>forms-tab</xsl:text>
              <xsl:if test="$active = $pos"> forms-activeTab</xsl:if>
            </xsl:attribute>
            <xsl:copy-of select="fi:label/node()"/>
            <xsl:if test="fi:items/*//fi:validation-message">
              <span class="forms-validation-message">&#160;!&#160;</span>
            </xsl:if>
          </span>
        </xsl:for-each>
      </div>
      <xsl:for-each select="fi:items/fi:*">
        <xsl:variable name="pos" select="position() - 1"/>
        <div class="forms-tabContent" id="{$id}_items_{$pos}">
          <xsl:if test="$active != $pos">
            <xsl:attribute name="style">display:none</xsl:attribute>
          </xsl:if>
          <xsl:apply-templates select="."/>
        </div>
      </xsl:for-each>
    </div>
    <script type="text/javascript">
      if (window.onTabShownHandlers == undefined)
        window.onTabShownHandlers = new Object();
      var currentHandlers = new Object();
      var initialHandler = null;
      window.onTabShownHandlers["<xsl:value-of select="$id"/>"] = currentHandlers;
      <xsl:for-each select="fi:items/fi:*">
        <xsl:variable name="pos" select="position() - 1"/>
          <xsl:if test="@formsOnShow">
            currentHandlers["<xsl:value-of select="concat($id, '_items_', $pos)"/>"] = "<xsl:value-of select="@formsOnShow"/>";
            <xsl:if test="$active = $pos">
               initialHandler = "<xsl:value-of select="@formsOnShow"/>";
            </xsl:if>
          </xsl:if>
      </xsl:for-each>
      if (initialHandler != null) {
        eval(initialHandler);
        initialHandler = null;
      }
    </script>
  </xsl:template>-->

  <!--
    fi:group of type choice : a popup is used instead of tabs
  
  <xsl:template match="fi:group[fi:styling/@type='choice']">

    <xsl:variable name="active">
      <xsl:variable name="value" select="normalize-space(fi:state/fi:*/fi:value)"/>
      <xsl:choose>
        <xsl:when test="$value">
          <xsl:value-of select="$value"/>
        </xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
   
    <xsl:variable name="state-widget" select="fi:state/fi:*/@id"/>
    <xsl:variable name="id" select="generate-id()"/>

    <fieldset id="{$id}">
      <legend title="{fi:hint}">
        <xsl:apply-templates select="fi:label/node()"/>
        <select name="{$state-widget}" onchange="forms_showTab('{$id}', this.selectedIndex, {count(fi:items/*)}, '{$state-widget}')">
          <xsl:for-each select="fi:items/fi:*">
            <xsl:variable name="pos" select="position() - 1"/>
            <option>
              <xsl:attribute name="value">
                <xsl:choose>
                  <xsl:when test="fi:value">
                    <xsl:value-of select="fi:value"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="$pos"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
              <xsl:if test="$active = $pos">
                <xsl:attribute name="selected">selected</xsl:attribute>
              </xsl:if>
              <xsl:copy-of select="fi:label/node()"/>
            </option>
          </xsl:for-each>
        </select>
        <xsl:if test="fi:items/*//fi:validation-message">
          <span class="forms-validation-message">&#160;!&#160;</span>
        </xsl:if>
      </legend>
      
      <xsl:for-each select="fi:items/fi:*">
        <xsl:variable name="pos" select="position() - 1"/>
        <div id="{$id}_items_{$pos}">
          <xsl:if test="$active != $pos">
            <xsl:attribute name="style">display:none</xsl:attribute>
          </xsl:if>
          <xsl:apply-templates select="."/>
        </div>
      </xsl:for-each>
    </fieldset>
  </xsl:template>-->

  <!--
    fi:group of type fieldset : enclose items in a fieldset frame
  
  <xsl:template match="fi:group[fi:styling/@type='fieldset']">
    <fieldset>
      <xsl:apply-templates select="." mode="styling"/>
      <legend title="{fi:hint}"><xsl:copy-of select="fi:label/node()"/></legend>
      <xsl:apply-templates mode="group-layout" select="."/>
    </fieldset>
  </xsl:template>-->

  <!--
    Column group items layout
  
  <xsl:template match="fi:group[fi:styling/@layout='column']" mode="group-layout">
    <table border="0" summary="{fi:hint}">
      <tbody>
        <xsl:apply-templates select="fi:items/*" mode="group-column-content"/>
      </tbody>
    </table>
  </xsl:template>-->

  <!--
    Default column layout : label above and input below
  
  <xsl:template match="fi:*" mode="group-column-content">
    <tr>
      <td><xsl:apply-templates select="." mode="label"/></td>
    </tr>
    <tr>
      <td><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template>

  <xsl:template match="fi:action" mode="group-column-content">
    <tr>
      <td><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template>-->
  

  <!--
    Columns group items layout
  
  <xsl:template match="fi:group[fi:styling/@layout='columns']" mode="group-layout">
    <table border="0" summary="{fi:hint}">
      <tbody>
        <xsl:apply-templates select="fi:items/*" mode="group-columns-content"/>
      </tbody>
    </table>
  </xsl:template>-->

  <!--
    Default columns layout : label left and input right
  
  <xsl:template match="fi:*" mode="group-columns-content">
    <tr>
      <td><xsl:apply-templates select="." mode="label"/></td>
      <td><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template>-->

  <!--
    Row group items layout
  
  <xsl:template match="fi:group[fi:styling/@layout='row']" mode="group-layout">
    <table border="0" summary="{fi:hint}">
      <tbody>
        <tr>
          <xsl:apply-templates select="fi:items/*" mode="group-row-content"/>
        </tr>
      </tbody>
    </table>
  </xsl:template>-->

  <!--
    Default row layout : label left and input right
 
  <xsl:template match="fi:*" mode="group-row-content">
    <td><xsl:apply-templates select="." mode="label"/></td>
    <td><xsl:apply-templates select="."/></td>
  </xsl:template>

  <xsl:template match="fi:action" mode="group-row-content">
    <td><xsl:apply-templates select="."/></td>
  </xsl:template> -->
  
  <!--
    Rows group items layout
  
  <xsl:template match="fi:group[fi:styling/@layout='rows']" mode="group-layout">
    <table border="0" summary="{fi:hint}">
      <tbody>
        <tr>
          <xsl:apply-templates select="fi:items/*" mode="group-rows-labels"/>
        </tr>
        <tr>
          <xsl:apply-templates select="fi:items/*" mode="group-rows-content"/>
        </tr>
      </tbody>
    </table>
  </xsl:template>-->

  <!--
    Default rows layout : label above and input below
  
  <xsl:template match="fi:*" mode="group-rows-labels">
    <td><xsl:apply-templates select="." mode="label"/></td>
  </xsl:template>

  <xsl:template match="fi:action" mode="group-rows-labels">
    <td>&#160;</td>
  </xsl:template>

  <xsl:template match="fi:*" mode="group-rows-content">
    <td><xsl:apply-templates select="."/></td>
  </xsl:template>-->

  <!-- boolean field : checkbox and label on a single line
  <xsl:template match="fi:booleanfield" mode="group-columns-content">
    <tr>
      <td colspan="2">
        <xsl:apply-templates select="."/>
        <xsl:apply-templates select="." mode="label"/>
      </td>
    </tr>
  </xsl:template> -->

  <!-- action : on a single line
  <xsl:template match="fi:action" mode="group-columns-content">
    <tr>
      <td colspan="2"><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template> -->

  <!-- any other element : on a single line 
  <xsl:template match="*" mode="group-columns-content">
    <tr>
      <td colspan="2"><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template>-->

  <!-- double-list multivaluefield : lists under the label
  <xsl:template match="fi:multivaluefield[fi:styling/@list-type='double-listbox']"
                mode="group-columns-content">
    <tr>
      <td colspan="2"><xsl:apply-templates select="." mode="label"/></td>
    </tr>
    <tr>
      <td colspan="2"><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template> -->

  <!-- nested group 
  <xsl:template match="fi:group" mode="group-columns-content">
    <tr>
      <td colspan="2"><xsl:apply-templates select="."/></td>
    </tr>
  </xsl:template>-->

 <!--<xsl:template match="@*|node()" priority="-1">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>-->

  <!-- Location of the resources directory, where JS libs and icons are stored 
  <xsl:param name="resources-uri">resources</xsl:param>

  <xsl:template match="head" mode="forms-field">
    <script src="{$resources-uri}/forms-lib.js" type="text/javascript"/>
    <link rel="stylesheet" type="text/css" href="main.css"/>
  </xsl:template>

  <xsl:template match="body" mode="forms-field">
    <xsl:copy-of select="@*"/>
    <xsl:attribute name="onload">forms_onload(); <xsl:value-of select="@onload"/></xsl:attribute>
  </xsl:template>-->

  <!--+
      | fi:field with a selection list (not 'radio' style)
      | Rendering depends on the attributes of fi:styling :
      | - if @list-type is "listbox" : produce a list box with @listbox-size visible
      |   items (default 5)
      | - otherwise, produce a dropdown menu
      +
      + dropdown or listbox
      + 
      
  <xsl:template match="fi:field[fi:selection-list]" priority="1">
    <xsl:variable name="value" select="fi:value"/>

    <select title="{fi:hint}" id="{@id}" name="{@id}">
      <xsl:apply-templates select="." mode="styling"/>
      <xsl:for-each select="fi:selection-list/fi:item">
        <option value="{@value}">
          <xsl:if test="@value = $value">
            <xsl:attribute name="selected">selected</xsl:attribute>
          </xsl:if>
          <xsl:copy-of select="fi:label/node()"/>
        </option>
      </xsl:for-each>
    </select>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>-->

  <!--+
      | fi:field with a selection list and @type 'output'
      +
  <xsl:template match="fi:field[fi:selection-list][fi:styling/@type='output']" priority="3">
    <xsl:variable name="value" select="fi:value"/>
    <xsl:variable name="selected" select="fi:selection-list/fi:item[@value = $value]"/>
    <xsl:choose>
      <xsl:when test="$selected/fi:label">
        <xsl:copy-of select="$selected/fi:label/node()"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>-->

  <!--+
      | fi:field with @type 'textarea'
      +
  <xsl:template match="fi:field[fi:styling/@type='textarea']">
    <textarea id="{@id}" name="{@id}" title="{fi:hint}">
      <xsl:apply-templates select="." mode="styling"/>
      <xsl:copy-of select="translate(fi:value/node(), '&#13;', '')"/>
    </textarea>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>-->

  <!--+
      | fi:field with @type 'output' and fi:output are both rendered as text
      +
  <xsl:template match="fi:output | fi:field[fi:styling/@type='output']" priority="2">
    <xsl:copy-of select="fi:value/node()"/>
  </xsl:template>-->


  <!--+
      | fi:repeater
      +
  <xsl:template match="fi:repeater">
    <input type="hidden" name="{@id}.size" value="{@size}"/>
    <table border="1">
      <tr>
        <xsl:for-each select="fi:headings/fi:heading">
          <th><xsl:value-of select="."/></th>
        </xsl:for-each>
      </tr>
      <xsl:apply-templates select="fi:repeater-row"/>
    </table>
  </xsl:template>-->

  <!--+
      | fi:repeater-row
      +
  <xsl:template match="fi:repeater-row">
    <tr>
      <xsl:for-each select="*">
        <td>
          <xsl:apply-templates select="."/>
        </td>
      </xsl:for-each>
    </tr>
  </xsl:template>-->

  <!--+
      | fi:repeater-size
      +
  <xsl:template match="fi:repeater-size">
    <input type="hidden" name="{@id}.size" value="{@size}"/>
  </xsl:template>-->

  <!--+
      | fi:form
      +
  <xsl:template match="fi:form">
    <table border="1">
      <xsl:for-each select="fi:widgets/*">
        <tr>
          <xsl:choose>
            <xsl:when test="self::fi:repeater">
              <td colspan="2">
                <xsl:apply-templates select="."/>
              </td>
            </xsl:when>
            <xsl:when test="self::fi:booleanfield">
              <td>&#160;</td>
              <td>
                <xsl:apply-templates select="."/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="." mode="label"/>
              </td>
            </xsl:when>
            <xsl:otherwise>
              <td>
                <xsl:apply-templates select="." mode="label"/>
              </td>
              <td>
                <xsl:apply-templates select="."/>
              </td>
            </xsl:otherwise>
          </xsl:choose>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <xsl:template match="fi:aggregatefield">
    <input id="{@id}" name="{@id}" value="{fi:value}" title="{fi:hint}">
      <xsl:apply-templates select="." mode="styling"/>
    </input>
    <xsl:apply-templates select="." mode="common"/>
  </xsl:template>

  <xsl:template match="fi:messages">
    <xsl:if test="fi:message">
      <xsl:apply-templates select="." mode="label"/>:
      <ul>
        <xsl:for-each select="fi:message">
          <li><xsl:apply-templates/></li>
        </xsl:for-each>
      </ul>
    </xsl:if>
  </xsl:template>

  <xsl:template match="fi:validation-errors">
    <xsl:variable name="header">
      <xsl:choose>
        <xsl:when test="header">
          <xsl:copy-of select="header"/>
        </xsl:when>
        <xsl:otherwise>
          <p class="forms-validation-errors">The following errors have been detected (marked with !):</p>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="footer">
      <xsl:choose>
        <xsl:when test="footer">
          <xsl:copy-of select="footer"/>
        </xsl:when>
        <xsl:otherwise>
          <p class="forms-validation-errors">Please, correct them and re-submit the form.</p>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="messages" select="ancestor::fi:form-template//fi:validation-message"/>
    <xsl:if test="$messages">
      <xsl:copy-of select="$header"/>
      <ul>
        <xsl:for-each select="$messages">
          <li class="forms-validation-error">
            <xsl:variable name="label">
              <xsl:apply-templates select=".." mode="label"/>
            </xsl:variable>
            <xsl:if test="$label">
              <xsl:copy-of select="$label"/><xsl:text>: </xsl:text>
            </xsl:if>
            <xsl:value-of select="."/>
          </li>
        </xsl:for-each>
      </ul>
      <xsl:copy-of select="$footer"/>
    </xsl:if>
  </xsl:template>-->

</xsl:stylesheet>
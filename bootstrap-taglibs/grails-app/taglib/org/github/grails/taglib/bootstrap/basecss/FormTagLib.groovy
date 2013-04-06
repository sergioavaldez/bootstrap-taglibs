package org.github.grails.taglib.bootstrap.basecss

import static org.github.grails.taglib.bootstrap.Attribute.*;

class FormTagLib {
	
	static namespace = "bs"
	
	private static final String IN_SEARCH_FORM 				= 'inSearchForm'
	private static final String IN_HORIZONTAL_FORM 			= 'inHorizontalForm'
	private static final String CURRENT_FIELD_ATTRS 		= null
	private static final boolean FIELD_EXTENSION_STARTED 	= false
	
	public static final String CURRENT_FORM_URL 			= 'currentFormURL'
	
	def form = {attrs, body ->
		def baseClass = "${(attrs.'class'?:'')} form"
		 
		def type = attrs.remove('type') 
		
		pageScope."$CURRENT_FORM_URL" = g.createLink(attrs)
		
		if(type == "search" || attrs.remove('search') == 'true') {
			baseClass = baseClass.concat('-search')
			pageScope."$IN_SEARCH_FORM" = true
		} else if(type == "inline" || attrs.inline == 'true') {
			baseClass = baseClass.concat('-inline')
			attrs.remove('inline')
		}else if(type == "horizontal" || attrs.remove('horizontal') == 'true') {
			baseClass = baseClass.concat('-horizontal')
			attrs.remove('horizontal')
			pageScope."$IN_HORIZONTAL_FORM" = true
		}
		
		attrs.'class' = baseClass.trim()
		
		//def formTagLib = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib')
		
		if(attrs.remove('upload') == "true") {
			out << g.uploadForm(attrs) {
				body()
			}
		} else {
			out << g.form(attrs){body()}
		}
		
		pageScope."$IN_SEARCH_FORM" 	= null
		pageScope."$IN_HORIZONTAL_FORM" = null
		pageScope."$CURRENT_FORM_URL"	= null
		
	}
	
	/**
	* A helper tag for creating checkboxes.
	*
	* @attr name REQUIRED the name of the checkbox
	* @attr value  the value of the checkbox
	* @attr checked if evaluates to true sets to checkbox to checked
	* @attr disabled if evaluates to true sets to checkbox to disabled
	* @attr readonly if evaluates to true, sets to checkbox to read only
	* @attr id DOM element id; defaults to name
	* @attr inline Boolean value that indicates if the checkboxes will be in line.
	*/
	def checkBox = {attrs, body ->
		attrs.'component' = 'checkbox'
		checkRadioImpl(attrs, body, out)
	}
	
	/**
	* A helper tag for creating radio buttons.
	*
	*
	* @attr value REQUIRED The value of the radio button
	* @attr name REQUIRED The name of the radio button
	* @attr checked boolean to indicate that the radio button should be checked
	* @attr disabled boolean to indicate that the radio button should be disabled
	* @attr readonly boolean to indicate that the radio button should not be editable
	* @attr id the DOM element id
	* @attr inline Boolean value that indicates if the radios will be in line.
	*/
	def radio = {attrs, body ->
		attrs.'component' = 'radio'
		checkRadioImpl(attrs, body, out)
	}
		
	def checkRadioImpl(attrs, body, out) {
		def label 		= attrs.remove 'label'
		def component 	= attrs.remove 'component'
		def useIcon		= attrs.remove('useIcon') == 'true'
		def value 		= attrs.'value'
		
		pageScope."$CURRENT_FIELD_ATTRS" = attrs
		
		if(useIcon) {
			addToClass(attrs, "hide")
		}
		
		def checked = false
        def checkedAttributeWasSpecified = false
        if (attrs.containsKey('checked')) {
            checkedAttributeWasSpecified = true
            checked = attrs.'checked'
        }
		
        if (checked instanceof String) { 
			checked = Boolean.valueOf(checked)
        }
		
		if (value == null) value = false
		
		if (!checkedAttributeWasSpecified && (value as boolean)) {
			checked = true
		}
		
		if(attrs.remove('inline') == 'true') {
			addToClass(attrs, "inline")
		}
		
		defaultClasses(attrs, "")
		
		out << "<label class=\"$component\">"
		
		if(component == 'radio') {
			out << g.radio(attrs)
		} else if(component == 'checkbox') {
			out << g.checkBox(attrs)
		}
		
		if(useIcon) {
			out << bs.icon(icon: (checked ? 'check' : 'check-empty'))
		}
		
		out << label
		out << body()
		out << "</label>"
	}
	
	/**
	 * Creates a new text field.
	 * 
	 * @attr name REQUIRED Field name also used as id if this attribute is not present.
	 * @attr helpType Inline and block level support for help text that appears around form controls, 
	 * <strong>applicable only in horizontal form<strong>.
	 * @attr helpText Text to be showed as help.
	 * @attr validationState styles for error, warning, info, and success messages
	 * <strong>applicable only in horizontal form<strong>.
	 * @attr uneditable Present data in a form that's not editable without using actual form markup.
	 * @attr size Relative field sizing mini, small, medium, large, xlarge, xxlarge.
	 * @attr span Relative field grid sizing from 1 to 12.
	 * @attr avoidHorizontal Boolean value that indicates that this will no take the horizontal way in horizontal form.
	 * @attr append Text to be append to field.
	 * @attr prepend Text to be prepend to field.  
	 */
	def textField = {attrs, body ->
		def field = g.textField(attrs){body()}
		if(attrs.remove('readonly') == true){
			field = "<span class=\"uneditable-input\">${attrs.value}</span>"
		}
		fieldImplement(field, attrs, out)
	}
	
	/**
	 * Creates a new text field.
	 * 
	 * @attr name REQUIRED Field name also used as id if this attribute is not present.
	 * @attr uneditable Present data in a form that's not editable without using actual form markup.
	 * @attr size Relative field sizing mini, small, medium, large, xlarge, xxlarge.
	 * @attr span Relative field grid sizing from 1 to 12.
	 */
	def passwordField = {attrs, body ->
		fieldImplement(g.passwordField(attrs){body()}, attrs, out)
	}
	
	def hiddenField = {attrs ->
		g.hiddenField(attrs)
	}
	
	def fieldImplement(def field, def attrs, def out) {
		pageScope."$CURRENT_FIELD_ATTRS" = attrs
		
		def baseClass = "${attrs.remove('class') ?: ''}"
		
		if(attrs.size) {
			addToClass(attrs, "input-${attrs.remove('size')}")
		}
		
		if(attrs.span) {
			addToClass(attrs, "span${attrs.remove('span')}")
		}
		
		if(attrs.uneditable == true) {
			addToClass(attrs, "uneditable-input")
		}
		
		defaultClasses(attrs, "")
		
		out << field
	}
	
	/**
	* A helper tag for creating HTML selects.<br/>
	*
	* Examples:<br/>
	* &lt;g:select name="user.age" from="${18..65}" value="${age}" /&gt;<br/>
	* &lt;g:select name="user.company.id" from="${Company.list()}" value="${user?.company.id}" optionKey="id" /&gt;<br/>
	*
	* @attr name REQUIRED the select name
	* @attr id the DOM element id - uses the name attribute if not specified
	* @attr from REQUIRED The list or range to select from
	* @attr keys A list of values to be used for the value attribute of each "option" element.
	* @attr optionKey By default value attribute of each &lt;option&gt; element will be the result of a "toString()" call on each element. Setting this allows the value to be a bean property of each element in the list.
	* @attr optionValue By default the body of each &lt;option&gt; element will be the result of a "toString()" call on each element in the "from" attribute list. Setting this allows the value to be a bean property of each element in the list.
	* @attr value The current selected value that evaluates equals() to true for one of the elements in the from list.
	* @attr multiple boolean value indicating whether the select a multi-select (automatically true if the value is a collection, defaults to false - single-select)
	* @attr valueMessagePrefix By default the value "option" element will be the result of a "toString()" call on each element in the "from" attribute list. Setting this allows the value to be resolved from the I18n messages. The valueMessagePrefix will be suffixed with a dot ('.') and then the value attribute of the option to resolve the message. If the message could not be resolved, the value is presented.
	* @attr noSelection A single-entry map detailing the key and value to use for the "no selection made" choice in the select box. If there is no current selection this will be shown as it is first in the list, and if submitted with this selected, the key that you provide will be submitted. Typically this will be blank - but you can also use 'null' in the case that you're passing the ID of an object
	* @attr disabled boolean value indicating whether the select is disabled or enabled (defaults to false - enabled)
	* @attr readonly boolean value indicating whether the select is read only or editable (defaults to false - editable)
	*/
	def select = {attrs, body ->
		fieldImplement(g.select(attrs){body()}, attrs, out)
	}
	
	/**
	* General linking to controllers, actions etc. Examples:<br/>
	*
	* &lt;g:link action="myaction"&gt;link 1&lt;/gr:link&gt;<br/>
	* &lt;g:link controller="myctrl" action="myaction"&gt;link 2&lt;/gr:link&gt;<br/>
	*
	* @attr controller The name of the controller to use in the link, if not specified the current controller will be linked
	* @attr action The name of the action to use in the link, if not specified the default action will be linked
	* @attr uri relative URI
	* @attr url A map containing the action,controller,id etc.
	* @attr base Sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behaviour of the absolute property, if both are specified.
	* @attr absolute If set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:&lt;port&gt; if no value in Config and not running in production.
	* @attr id The id to use in the link
	* @attr fragment The link fragment (often called anchor tag) to use
	* @attr params A map containing URL query parameters
	* @attr mapping The named URL mapping to use to rewrite the link
	* @attr event Webflow _eventId parameter
	* @attr elementId DOM element id
	* @attr decorate Can be one of these primary, info, success, warning, danger, inverse or link.
	* @attr size Can be one of these large, small or mini.
	* @attr block Boolean value.
	*/
	/*
	def buttonLink = {attrs, body ->
		attrs.'component' = "buttonLink"
		buttonImpl(attrs, body, out)
	}
	
	def button = {attrs, body->
		attrs.'component' = "button"
		buttonImpl(attrs, body, out)
	}
	
	def actionSubmit = {attrs, body->
		attrs.'component' = "actionSubmit"
		buttonImpl(attrs, body, out)
	}
	*/
	def fieldExtension = {attrs, body->
		pageScope."$FIELD_EXTENSION_STARTED" = true
		out << "<div class=\"${(attrs.remove('prepend') == 'true' ? 'input-prepend':'').trim()} ${(attrs.remove('append') == 'true' ? 'input-append':'').trim()}\" >\n${body()}\n</div>"
		pageScope."$FIELD_EXTENSION_STARTED" = false
	}
	
	def buttonImpl(attrs, body, out) {
		def component = attrs.remove 'component'
		
		if(attrs.type?.empty) {
			attrs.type = "button"
		}
				
		if(attrs.'decorate') {
			addToClass(attrs, "btn-${attrs.remove('decorate')}")
		}
		
		if(attrs.'size') {
			addToClass(attrs, "btn-${attrs.remove('size')}")
		}
		
		if((attrs.remove('block') == 'true')) {
			addToClass(attrs, "btn-block")
		}
		
		
		
		if(booleanToAttribute(attrs, "disabled")) {
			addToClass(attrs, "disabled")
		}
		
		defaultClasses(attrs, "btn")
		
		if(component == "button") {
			out << "<button " << outputAttributes(attrs, out) << ">"
			out << body().trim()
			out << "</button>"		
		} else if(component == "buttonLink") {
			out << link(attrs) {
				body().trim()
			}
		} else if(component == "actionSubmit") {
			if (!attrs.value) {
	            throwTagError("Tag [actionSubmit] is missing required attribute [value]")
	        }
	
	        // Strip out any 'name' attribute, since this tag overrides it.
	        if (attrs.name) {
	            log.warn "[actionSubmit] 'name' attribute will be ignored"
	            attrs.remove('name')
	        }
	
	        // add action and value
	        def value = attrs.remove('value')
	        def action = attrs.remove('action') ?: value
	        booleanToAttribute(attrs, 'disabled')
	
	        out << "<button type=\"submit\" name=\"_action_${action}\" value=\"${value}\" " << outputAttributes(attrs, out) << ">"

	        out << body()
	
	        // close tag
	        out << '</button>'
		}
	}
	
	/**
	 * @attr label Label to shown in field.
	 */
	def horizontalFieldMask = {attrs, body->
		def bodyP = body()
		def fieldAttrs = pageScope."$CURRENT_FIELD_ATTRS"
		
		pageScope."$CURRENT_FIELD_ATTRS" = null
		//ValidarionState implement
		out << "<div id=\"${fieldAttrs.id ?: fieldAttrs.name}-control-group\" class=\"control-group\">"
		out << "\t<label id=\"${fieldAttrs.id ?: fieldAttrs.name}-control-label\" class=\"control-label\" for=\"${fieldAttrs.name}\">${fieldAttrs.remove('label')?:''}</label>"
		out << "\t<div id=\"${fieldAttrs.id ?: fieldAttrs.name}-controls\" class=\"controls\">"
		
		out << bodyP	
		
		out << "\t</div>"
		out << "</div>"
	}
	
	def helpTextInline = {attrs, body->
		out << "<span class=\"help-inline\">${body()}</span>"
	}
	
	def helpTextBlock = {attrs, body->
		out << "<span class=\"help-block\">${body()}</span>"
	}
	
	def fieldAddOn = {attrs, body->
		if(!pageScope."$FIELD_EXTENSION_STARTED") {
			throwTagError("Tag [fieldAddOn] should be placed in [fieldExtension] tag.")
		}
		out << "<span class=\"add-on\">${body()}</span>"
	}
	
	def fieldImpl(out, attrs, body) {
		def size = attrs.remove('size')
		def baseClass = attrs.'class' ? attrs.remove('class') : ''
		def showHelp = attrs.remove('showHelp')
		def helpText = attrs.remove('helpText')
		def validationState = attrs.'validationState' ? ' ' + attrs.remove('validationState') : ''
		def fieldType = attrs.remove("fieldType")
		def uneditable = attrs.boolean('uneditable')
		def span = attrs.remove('span')
		def avoidHorizontal = attrs.remove('avoidHorizontal')
		def append = attrs.remove('append')
		def prepend = attrs.remove('prepend')
		
		if(size) {
			addToClass(attrs, "input-${size}")
		}
		
		if(span) {
			addToClass(attrs, "span${span}")
		}
		
		if(attrs.remove('uneditable') == 'true') {
			addToClass(attrs, "uneditable-input")
		}
		
		attrs.'class' = baseClass.trim();
		//def formTagLib = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib')
		if(pageScope."$IN_HORIZONTAL_FORM" && avoidHorizontal != 'true') {
			out << "<div id=\"${attrs.id ?: attrs.name}-control-group\" class=\"control-group ${validationState}\">"
			out << "\t<label id=\"${attrs.id ?: attrs.name}-control-label\" class=\"control-label\" for=\"${attrs.name}\">${attrs.remove('label')?:''}</label>"
			out << "\t<div id=\"${attrs.id ?: attrs.name}-controls\" class=\"controls\">"
			
			if(append || prepend) {
				out << "<div class=\"${prepend ? 'input-prepend':''} ${append ? 'input-append':''}\">"
			}
			if(prepend){
				out << "<span class=\"add-on\">${prepend}</span>"	
			}
			if(uneditable) {
				out << "<span class=\"${baseClass}\">${attrs.value ? attrs.value : ''}</span>"
			} else if(fieldType == "text") {
				out << g.textField.call(attrs)
			} else if(fieldType == "password") {
				out << g.passwordField.call(attrs)
			} else if (fieldType == "textarea") {
				out << g.textArea.call(attrs, body)
			} else if(fieldType == "select") {
				out << g.select.call(attrs)
			}
			
			if(append){
				out << "<span class=\"add-on\">${append}</span>"
			}
			if(append || prepend) {
				out << "</div>"
			}
			
			if(body) {
				out << body()
			}
			
			if(showHelp == 'block') {
				out << "<span class=\"help-block\">${helpText}</span>"
			} else if(showHelp == 'inline') {
				out << "<span class=\"help-inline\">${helpText}</span>"
			}
			out << "\t</div>"
			out << "</div>"
		} else {
			if(append || prepend) {
				out << "<div class=\"${prepend ? 'input-prepend':''} ${append ? 'input-append':''}\">"
			}
			if(prepend){
				out << "<span class=\"add-on\">${prepend}</span>"	
			}
			if(uneditable) {
				out << "<span class=\"${baseClass}\">${attrs.value ? attrs.value : ''}</span>"
			} else if(fieldType == "text") {
				out << g.textField(attrs)
			} else if(fieldType == "password") {
				out << g.passwordField(attrs)
			} else if (fieldType == "textarea") {
				out << g.textArea(attrs, body)
			} else if(fieldType == "select") {
				out << g.select(attrs)
			}
			
			if(append){
				out << "<span class=\"add-on\">${append}</span>"
			}
			if(append || prepend) {
				out << "</div>"
			}			
		}
	}
	
}

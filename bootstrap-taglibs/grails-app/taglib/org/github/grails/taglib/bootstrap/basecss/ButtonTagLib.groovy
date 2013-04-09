package org.github.grails.taglib.bootstrap.basecss

import org.github.grails.taglib.bootstrap.BaseTagLib;

class ButtonTagLib extends BaseTagLib {

	static namespace = "bs"
	
	/**
	 * Wrap a series of buttons.
	 * 
	 * @attr vertical Boolean value that indicates when the button group is vertical
	 * 		by the fault the value is <code>false</code>.
	 * @attr pull Float an element to <code>left</code> or <code>right<code>
	 * @attr muted Change an element's color to <code>#999</code>
	 * @attr clearfix Clear the <code>float</code> on this element
	 */
	Closure buttonGroup = {attrs, body ->
		
		if(Boolean.valueOf(attrs.remove('vertical'))) {
			addToClass(attrs, "btn-group-vertical")
		}
		
		defaultClasses(attrs, "btn-group")
		
		out << "<div " << outputAttributes(attrs, out) << " >"
		out << body()
		out << "</div>"
	}
	
	/**
	* Combine sets of button groups for more complex component. 
	*
	* @attr pull Float an element to <code>left</code> or <code>right<code>
	* @attr muted Change an element's color to <code>#999</code>
	* @attr clearfix Clear the <code>float</code> on this element
	*/
	Closure buttonToolbar = {attrs, body ->
		defaultClasses(attrs, "btn-toolbar")
		
		out << "<div " << outputAttributes(attrs, out) << " >"
		out << body()
		out << "</div>"
	}
	
	Closure menuButton = {attrs, body->
		attrs.dropdown = true
		g.button(attrs){body()}
	}
	
	/**
	* Creates a submit button that submits to an action in the controller specified by the form action.<br/>
	* The name of the action attribute is translated into the action name, for example "Edit" becomes
	* "_action_edit" or "List People" becomes "_action_listPeople".<br/>
	* If the action attribute is not specified, the value attribute will be used as part of the action name.
	*
	* &lt;bs:actionSubmit value="Edit" /&gt;<br/>
	* &lt;bs:actionSubmit action="Edit" value="Some label for editing" /&gt;<br/>
	*
	* @attr value REQUIRED The title of the button and name of action when not explicitly defined.
	* @attr action The name of the action to be executed, otherwise it is derived from the value.
	* @attr disabled Makes the button to be disabled. Will be interpreted as a Groovy Truth.
	* @attr pull Float an element to <code>left</code> or <code>right<code>.
	* @attr muted Change an element's color to <code>#999</code>.
	* @attr clearfix Clear the <code>float</code> on this element.
	*/
   Closure actionSubmit = {attrs, body->
	   def baseClass = "${(attrs.'class'?:'')} btn"
	   
	   attrs.type = 'actionSubmit'
	   
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
	   
	   attrs.name = "_action_${action}"
	   attrs.component = "button"
	   
	   buttonImpl(attrs, body, out)
   }
	
   /**
   * Creates a submit button.
   *
   * @attr name REQUIRED the field name
   * @attr value the button text
   * @attr type input type; defaults to 'submit'
   * @attr event the webflow event id
   */
	def submitButton = {attrs, body ->
		attrs.'component' = "actionSubmit"
		buttonImpl(attrs, body, out)
	}
	
	/**
	 * @attr size One of these values: <code>large</code>, <code>small</code> or <code>mini</code>
	 * @attr dropdown Boolean value <code>false</code> by default.
	 * @attr block Boolean value, create block level buttons—those that span the full width of a parent. <code>false</code> by default. 
	 * @attr disabled Boolean value <code>false</code> by default.
	 * @attr decorate One of these values: <code>primary</code>, <code>info</code>, <code>success</code>, <code>warning</code>, <code>danger</code>, <code>inverse</code>, <code>link</code>
	 * @attr pull Float an element to <code>left</code> or <code>right<code>
	 * @attr muted Change an element's color to <code>#999</code>
	 * @attr clearfix Clear the <code>float</code> on this element
	 */
	def button = {attrs, body->
		attrs.component = 'button'
		attrs.type = attrs.type ?: 'button'
		
		buttonImpl(attrs, body, out);
	}
	
	/**
	 * @attr pull Float an element to <code>left</code> or <code>right<code>
	 * @attr muted Change an element's color to <code>#999</code>
	 * @attr clearfix Clear the <code>float</code> on this element 
	 */
	def buttonLink = {attrs, body->
		attrs.component = 'link'
		buttonImpl(attrs, body, out);
	}
	
	def buttonImpl(attrs, body, out){
		def component = attrs.remove 'component'
		boolean disabled 	= Boolean.valueOf(attrs.'disabled')
		boolean block 		= Boolean.valueOf(attrs.remove('block'))
		boolean dropdown 	= Boolean.valueOf(attrs.remove('dropdown'))
				
		if(attrs?.type?.empty) {
			attrs.type = "button"
		}
				
		if(attrs.'decorate') {
			addToClass(attrs, "btn-${attrs.remove('decorate')}")
		}
		
		if(attrs.'size') {
			addToClass(attrs, "btn-${attrs.remove('size')}")
		}
		
		if(disabled) {
			addToClass(attrs, "disabled")
		}
		booleanToAttribute(attrs, 'disabled')
		
		if(block) {
			addToClass(attrs, "btn-block")
		}
		
		if(dropdown) {
			attrs.'data-toggle' = 'dropdown'
			addToClass(attrs, "dropdown-toggle")
			
			if(component == "buttonLink") {
				attrs.url = "#"
			}
		}
		
		defaultClasses(attrs, "btn")
		
		if(component == "button") {
			out << "<button " << outputAttributes(attrs, out) << ">"
			out << body()
			out << "</button>"
		} else if(component == "link") {
			if(attrs.disabled == 'disabled') {
				attrs.url = "#"
				attrs.onclick = "return false;"
			}
			out << link(attrs) {
				body()
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
	
}
